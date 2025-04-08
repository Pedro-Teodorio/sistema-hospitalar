package com.hospital.service;

import com.hospital.dto.ProntuarioDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Consulta;
import com.hospital.model.Prontuario;
import com.hospital.model.enums.StatusConsulta;
import com.hospital.repository.ConsultaRepository;
import com.hospital.repository.ProntuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProntuarioService {

    @Inject
    ProntuarioRepository prontuarioRepository;

    @Inject
    ConsultaRepository consultaRepository;

    public List<Prontuario> listarTodos() {
        return prontuarioRepository.listAll();
    }

    public Prontuario buscarPorId(Long id) {
        return prontuarioRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário", id));
    }

    public Prontuario buscarPorConsultaId(Long consultaId) {
        return prontuarioRepository.buscarPorConsultaId(consultaId)
                .orElseThrow(() -> new EntityNotFoundException("Prontuário para a consulta com ID: " + consultaId));
    }

    public List<Prontuario> listarPorPacienteId(Long pacienteId) {
        return prontuarioRepository.listarPorPacienteId(pacienteId);
    }

    @Transactional
    public Prontuario criar(ProntuarioDTO prontuarioDTO) {
        Consulta consulta = consultaRepository.findByIdOptional(prontuarioDTO.getConsultaId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta", prontuarioDTO.getConsultaId()));

        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível criar um prontuário para uma consulta não realizada");
        }

        Optional<Prontuario> existingProntuario = prontuarioRepository.buscarPorConsultaId(prontuarioDTO.getConsultaId());
        if (existingProntuario.isPresent()) {
            throw new BusinessException("Já existe um prontuário para esta consulta");
        }

        Prontuario prontuario = new Prontuario();
        prontuario.setConsulta(consulta);
        prontuario.setAnamnese(prontuarioDTO.getAnamnese());
        prontuario.setDiagnostico(prontuarioDTO.getDiagnostico());
        prontuario.setPlanoTratamento(prontuarioDTO.getPlanoTratamento());
        prontuario.setDataCriacao(LocalDateTime.now());

        prontuarioRepository.persist(prontuario);
        return prontuario;
    }

    @Transactional
    public Prontuario atualizar(Long id, ProntuarioDTO prontuarioDTO) {
        Prontuario prontuario = buscarPorId(id);

        // Não permite alterar a consulta associada
        if (!prontuario.getConsulta().id.equals(prontuarioDTO.getConsultaId())) {
            throw new BusinessException("Não é possível alterar a consulta associada ao prontuário");
        }

        prontuario.setAnamnese(prontuarioDTO.getAnamnese());
        prontuario.setDiagnostico(prontuarioDTO.getDiagnostico());
        prontuario.setPlanoTratamento(prontuarioDTO.getPlanoTratamento());
        prontuario.setDataAtualizacao(LocalDateTime.now());

        return prontuario;
    }

    @Transactional
    public void excluir(Long id) {
        Prontuario prontuario = buscarPorId(id);
        prontuarioRepository.delete(prontuario);
    }

    public ProntuarioDTO toDTO(Prontuario prontuario) {
        return new ProntuarioDTO(
                prontuario.id,
                prontuario.getConsulta().id,
                prontuario.getAnamnese(),
                prontuario.getDiagnostico(),
                prontuario.getPlanoTratamento(),
                prontuario.getDataCriacao(),
                prontuario.getDataAtualizacao()
        );
    }

    public List<ProntuarioDTO> toDTOList(List<Prontuario> prontuarios) {
        return prontuarios.stream()
                .map(this::toDTO)
                .toList();
    }

}
