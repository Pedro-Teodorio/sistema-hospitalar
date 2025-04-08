package com.hospital.service;

import com.hospital.dto.MedicoDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Especialidade;
import com.hospital.model.Medico;
import com.hospital.repository.EspecialidadeRepository;
import com.hospital.repository.MedicoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class MedicoService {

    @Inject
    MedicoRepository medicoRepository;

    @Inject
    EspecialidadeRepository especialidadeRepository;

    public List<Medico> listarTodos() {
        return medicoRepository.listAll();
    }

    public Medico buscarPorId(Long id) {
        return medicoRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico", id));
    }

    public List<Medico> buscarPorNome(String nome) {
        return medicoRepository.buscarPorNome(nome);
    }

    public List<Medico> listarPorEspecialidade(Long idEspecialidade) {
        return medicoRepository.listarPorEspecialidade(idEspecialidade);
    }

    @Transactional
    public Medico criar(MedicoDTO medicoDTO) {
        Optional<Medico> medicoExistente = medicoRepository.buscarPorCrm(medicoDTO.getCrm());
        if (medicoExistente.isPresent()) {
            throw new BusinessException("Médico já cadastrado com o CRM: " + medicoDTO.getCrm());
        }

        Medico medico = new Medico();
        medico.setNome(medicoDTO.getNome());
        medico.setCrm(medicoDTO.getCrm());
        medico.setEmail(medicoDTO.getEmail());
        medico.setTelefone(medicoDTO.getTelefone());

        Set<Especialidade> especialidades = new HashSet<>();
        for (Long especialidadeId : medicoDTO.getEspecialidadeIds()) {
            Especialidade especialidade = especialidadeRepository.findByIdOptional(especialidadeId)
                    .orElseThrow(() -> new EntityNotFoundException("Especialidade", especialidadeId));
            especialidades.add(especialidade);
        }
        medico.setEspecialidades(especialidades);

        medicoRepository.persist(medico);
        return medico;
    }

    @Transactional
    public Medico atualizar(Long id, MedicoDTO medicoDTO) {
        Medico medico = buscarPorId(id);

        if (!medico.getCrm().equals(medicoDTO.getCrm())) {
            Optional<Medico> existingMedico = medicoRepository.buscarPorCrm(medicoDTO.getCrm());
            if (existingMedico.isPresent()) {
                throw new BusinessException("Já existe um médico cadastrado com o CRM: " + medicoDTO.getCrm());
            }
        }

        medico.setNome(medicoDTO.getNome());
        medico.setCrm(medicoDTO.getCrm());
        medico.setEmail(medicoDTO.getEmail());
        medico.setTelefone(medicoDTO.getTelefone());

        // Atualizar especialidades
        Set<Especialidade> especialidades = new HashSet<>();
        for (Long especialidadeId : medicoDTO.getEspecialidadeIds()) {
            Especialidade especialidade = especialidadeRepository.findByIdOptional(especialidadeId)
                    .orElseThrow(() -> new EntityNotFoundException("Especialidade", especialidadeId));
            especialidades.add(especialidade);
        }
        medico.setEspecialidades(especialidades);

        return medico;
    }

    @Transactional
    public void excluir(Long id) {
        Medico medico = buscarPorId(id);
        if (!medico.getConsultas().isEmpty()) {
            throw new BusinessException("Não é possível excluir o médico pois ele possui consultas associadas");
        }
        medicoRepository.delete(medico);
    }

    // Métodos auxiliares
    public MedicoDTO toDTO(Medico medico) {

        return new MedicoDTO(
                medico.id,
                medico.getNome(),
                medico.getCrm(),
                medico.getEmail(),
                medico.getTelefone(),
                medico.getEspecialidades().stream()
                        .map(e -> e.id)
                        .collect(Collectors.toSet())
        );
    }
}
