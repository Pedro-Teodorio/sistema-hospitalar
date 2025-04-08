package com.hospital.service;

import com.hospital.dto.ConsultaDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Consulta;
import com.hospital.model.Medico;
import com.hospital.model.Paciente;
import com.hospital.model.enums.StatusConsulta;
import com.hospital.repository.ConsultaRepository;
import com.hospital.repository.MedicoRepository;
import com.hospital.repository.PacienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@ApplicationScoped
public class ConsultaService {

    @Inject
    ConsultaRepository consultaRepository;

    @Inject
    MedicoRepository medicoRepository;

    @Inject
    PacienteRepository pacienteRepository;

    public List<Consulta> listarTodas() {
        return consultaRepository.listAll();
    }

    public Consulta buscarPorId(Long id) {
        return consultaRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Consulta", id));
    }

    public List<Consulta> listarPorMedico(Long medicoId) {
        return consultaRepository.listarPorMedico(medicoId);
    }

    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return consultaRepository.listarPorPaciente(pacienteId);
    }

    public List<Consulta> listarPorStatus(StatusConsulta status) {
        return consultaRepository.listarPorStatus(status);
    }

    public List<Consulta> listarPorIntervaloData(LocalDateTime inicio, LocalDateTime fim) {
        return consultaRepository.listarPorIntervaloData(inicio, fim);
    }

    @Transactional
    public Consulta criar(ConsultaDTO consultaDTO) {
        // Verificar se o médico existe
        Medico medico = medicoRepository.findByIdOptional(consultaDTO.getMedicoId())
                .orElseThrow(() -> new EntityNotFoundException("Médico", consultaDTO.getMedicoId()));

        // Verificar se o paciente existe
        Paciente paciente = pacienteRepository.findByIdOptional(consultaDTO.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente", consultaDTO.getPacienteId()));

        // Validar data da consulta
        if (consultaDTO.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data da consulta não pode ser no passado");
        }

        // Verificar disponibilidade do médico (considerando uma consulta de 30 minutos)
        LocalDateTime fimConsulta = consultaDTO.getDataHora().plusMinutes(30);
        if (!consultaRepository.verificarDisponibilidadeMedico(
                consultaDTO.getMedicoId(), consultaDTO.getDataHora(), fimConsulta)) {
            throw new BusinessException("O médico já possui uma consulta agendada neste horário");
        }

        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setDataHora(consultaDTO.getDataHora());
        consulta.setStatus(consultaDTO.getStatus() != null ? consultaDTO.getStatus() : StatusConsulta.AGENDADA);
        consulta.setObservacao(consultaDTO.getObservacao());

        consultaRepository.persist(consulta);
        return consulta;
    }

    @Transactional
    public Consulta atualizar(Long id, ConsultaDTO consultaDTO) {
        Consulta consulta = buscarPorId(id);

        // Não permitir alteração se a consulta já foi realizada ou cancelada
        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível alterar uma consulta que já foi realizada");
        }

        // Verificar se o médico existe
        if (!consulta.getMedico().id.equals(consultaDTO.getMedicoId())) {
            Medico medico = medicoRepository.findByIdOptional(consultaDTO.getMedicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Médico", consultaDTO.getMedicoId()));
            consulta.setMedico(medico);
        }

        // Verificar se o paciente existe
        if (!consulta.getPaciente().id.equals(consultaDTO.getPacienteId())) {
            Paciente paciente = pacienteRepository.findByIdOptional(consultaDTO.getPacienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Paciente", consultaDTO.getPacienteId()));
            consulta.setPaciente(paciente);
        }

        // Validar data da consulta
        if (consultaDTO.getDataHora().isBefore(LocalDateTime.now())) {
            throw new BusinessException("A data da consulta não pode ser no passado");
        }

        // Verificar disponibilidade do médico (considerando uma consulta de 30 minutos)
        if (!consulta.getDataHora().equals(consultaDTO.getDataHora())) {
            LocalDateTime fimConsulta = consultaDTO.getDataHora().plus(30, ChronoUnit.MINUTES);
            if (!consultaRepository.verificarDisponibilidadeMedico(
                    consultaDTO.getMedicoId(), consultaDTO.getDataHora(), fimConsulta)) {
                throw new BusinessException("O médico já possui uma consulta agendada neste horário");
            }
        }

        consulta.setDataHora(consultaDTO.getDataHora());
        consulta.setStatus(consultaDTO.getStatus());
        consulta.setObservacao(consultaDTO.getObservacao());

        return consulta;
    }

    @Transactional
    public Consulta cancelarConsulta(Long id) {
        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível cancelar uma consulta que já foi realizada");
        }

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new BusinessException("Esta consulta já está cancelada");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        return consulta;
    }

    @Transactional
    public Consulta realizarConsulta(Long id) {
        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new BusinessException("Não é possível realizar uma consulta cancelada");
        }

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new BusinessException("Esta consulta já foi realizada");
        }

        consulta.setStatus(StatusConsulta.REALIZADA);
        return consulta;
    }

    @Transactional
    public void excluir(Long id) {
        Consulta consulta = buscarPorId(id);

        if (consulta.getStatus() == StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível excluir uma consulta que já foi realizada");
        }

        if (consulta.getProntuario() != null) {
            throw new BusinessException("Não é possível excluir a consulta pois ela possui um prontuário associado");
        }

        if (!consulta.getReceitas().isEmpty()) {
            throw new BusinessException("Não é possível excluir a consulta pois ela possui receitas associadas");
        }

        if (!consulta.getExames().isEmpty()) {
            throw new BusinessException("Não é possível excluir a consulta pois ela possui exames associados");
        }

        consultaRepository.delete(consulta);
    }

    public ConsultaDTO toDTO(Consulta consulta) {
        return new ConsultaDTO(
                consulta.id,
                consulta.getDataHora(),
                consulta.getStatus(),
                consulta.id,
                consulta.id,
                consulta.getObservacao()
        );
    }

    public List<ConsultaDTO> toDTOList(List<Consulta> consultas) {
        return consultas.stream()
                .map(this::toDTO)
                .toList();
    }

}
