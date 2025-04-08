package com.hospital.service;

import com.hospital.dto.ExameDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Consulta;
import com.hospital.model.Exame;
import com.hospital.model.enums.StatusConsulta;
import com.hospital.model.enums.TipoExame;
import com.hospital.repository.ConsultaRepository;
import com.hospital.repository.ExameRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ExameService {

    @Inject
    ExameRepository exameRepository;

    @Inject
    ConsultaRepository consultaRepository;

    public List<Exame> listarTodos() {
        return exameRepository.listAll();
    }

    public Exame buscarPorId(Long id) {
        return exameRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Exame", id));
    }

    public List<Exame> listarPorConsulta(Long consultaId) {
        return exameRepository.listarPorConsultaId(consultaId);
    }

    public List<Exame> listarPorPaciente(Long pacienteId) {
        return exameRepository.listarPorPacienteId(pacienteId);
    }

    public List<Exame> listarPorTipo(TipoExame tipo) {
        return exameRepository.listarPorTipo(tipo);
    }

    public List<Exame> listarSemResultado() {
        return exameRepository.listarSemResultado();
    }

    @Transactional
    public Exame criar(ExameDTO exameDTO) {
        // Verificar se a consulta existe
        Consulta consulta = consultaRepository.findByIdOptional(exameDTO.getConsultaId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta", exameDTO.getConsultaId()));

        // Verificar se a consulta foi realizada
        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível solicitar exame para uma consulta não realizada");
        }

        Exame exame = new Exame();
        exame.setConsulta(consulta);
        exame.setNome(exameDTO.getNome());
        exame.setTipo(exameDTO.getTipo());
        exame.setInstrucoes(exameDTO.getInstrucoes());
        exame.setDataSolicitacao(LocalDateTime.now());

        // Resultado opcional no momento da criação (pode ser adicionado depois)
        if (exameDTO.getResultado() != null && !exameDTO.getResultado().isEmpty()) {
            exame.setResultado(exameDTO.getResultado());
            exame.setDataResultado(LocalDateTime.now());
        }

        exameRepository.persist(exame);
        return exame;
    }

    @Transactional
    public Exame atualizar(Long id, ExameDTO exameDTO) {
        Exame exame = buscarPorId(id);

        // Não permite alterar a consulta associada
        if (!exame.getConsulta().id.equals(exameDTO.getConsultaId())) {
            throw new BusinessException("Não é possível alterar a consulta associada ao exame");
        }

        exame.setNome(exameDTO.getNome());
        exame.setTipo(exameDTO.getTipo());
        exame.setInstrucoes(exameDTO.getInstrucoes());

        // Atualizar resultado se fornecido
        if (exameDTO.getResultado() != null && !exameDTO.getResultado().isEmpty()) {
            exame.setResultado(exameDTO.getResultado());
            // Se está adicionando resultado pela primeira vez
            if (exame.getDataResultado() == null) {
                exame.setDataResultado(LocalDateTime.now());
            }
        }

        return exame;
    }

    @Transactional
    public Exame registrarResultado(Long id, String resultado) {
        Exame exame = buscarPorId(id);

        if (resultado == null || resultado.isEmpty()) {
            throw new BusinessException("O resultado do exame não pode estar vazio");
        }

        exame.setResultado(resultado);
        exame.setDataResultado(LocalDateTime.now());

        return exame;
    }

    @Transactional
    public void excluir(Long id) {
        Exame exame = buscarPorId(id);
        exameRepository.delete(exame);
    }

    public ExameDTO toDTO(Exame exame) {
        return new ExameDTO(
                exame.id,
                exame.getConsulta().id,
                exame.getNome(),
                exame.getTipo(),
                exame.getInstrucoes(),
                exame.getDataSolicitacao(),
                exame.getDataResultado(),
                exame.getResultado()
        );
    }

    public List<ExameDTO> toDTOList(List<Exame> exames) {
        return exames.stream()
                .map(this::toDTO)
                .toList();
    }
}
