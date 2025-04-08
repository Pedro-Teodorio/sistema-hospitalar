package com.hospital.service;

import com.hospital.dto.ReceitaDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Consulta;
import com.hospital.model.Receita;
import com.hospital.model.enums.StatusConsulta;
import com.hospital.repository.ConsultaRepository;
import com.hospital.repository.ReceitaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ReceitaService {

    @Inject
    ReceitaRepository receitaRepository;

    @Inject
    ConsultaRepository consultaRepository;

    public List<Receita> listarTodas() {
        return receitaRepository.listAll();
    }

    public Receita buscarPorId(Long id) {
        return receitaRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita", id));
    }

    public List<Receita> listarPorConsulta(Long consultaId) {
        return receitaRepository.listarPorConsultaId(consultaId);
    }

    public List<Receita> listarPorPaciente(Long pacienteId) {
        return receitaRepository.listarPorPacienteId(pacienteId);
    }

    public List<Receita> listarPorMedicamento(String medicamento) {
        return receitaRepository.listarPorMedicamento(medicamento);
    }

    @Transactional
    public Receita criar(ReceitaDTO receitaDTO) {
        // Verificar se a consulta existe
        Consulta consulta = consultaRepository.findByIdOptional(receitaDTO.getConsultaId())
                .orElseThrow(() -> new EntityNotFoundException("Consulta", receitaDTO.getConsultaId()));

        // Verificar se a consulta foi realizada
        if (consulta.getStatus() != StatusConsulta.REALIZADA) {
            throw new BusinessException("Não é possível criar uma receita para uma consulta não realizada");
        }

        Receita receita = new Receita();
        receita.setConsulta(consulta);
        receita.setMedicamento(receitaDTO.getMedicamento());
        receita.setPosologia(receitaDTO.getPosologia());
        receita.setObservacoes(receitaDTO.getObservacoes());
        receita.setDataEmissao(LocalDateTime.now());
        receita.setDataValidade(receitaDTO.getDataValidade());

        receitaRepository.persist(receita);
        return receita;
    }

    @Transactional
    public Receita atualizar(Long id, ReceitaDTO receitaDTO) {
        Receita receita = buscarPorId(id);

        // Não permite alterar a consulta associada
        if (!receita.getConsulta().id.equals(receitaDTO.getConsultaId())) {
            throw new BusinessException("Não é possível alterar a consulta associada à receita");
        }

        receita.setMedicamento(receitaDTO.getMedicamento());
        receita.setPosologia(receitaDTO.getPosologia());
        receita.setObservacoes(receitaDTO.getObservacoes());
        receita.setDataValidade(receitaDTO.getDataValidade());

        return receita;
    }

    @Transactional
    public void excluir(Long id) {
        Receita receita = buscarPorId(id);
        receitaRepository.delete(receita);
    }

    public ReceitaDTO toDTO(Receita receita) {
        return new ReceitaDTO(
                receita.id,
                receita.getConsulta().id,
                receita.getMedicamento(),
                receita.getPosologia(),
                receita.getObservacoes(),
                receita.getDataEmissao(),
                receita.getDataValidade()
        );
    }

    public List<ReceitaDTO> toDTO(List<Receita> receitas) {
        return receitas.stream()
                .map(this::toDTO)
                .toList();
    }
}
