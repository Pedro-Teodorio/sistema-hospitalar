package com.hospital.service;

import com.hospital.dto.EspecialidadeDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Especialidade;
import com.hospital.repository.EspecialidadeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EspecialidadeService {

    @Inject
    EspecialidadeRepository especialidadeRepository;

    public List<Especialidade> listarTodas() {
        return especialidadeRepository.listAll();
    }

    public Especialidade buscarPorId(Long id) {
        return especialidadeRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Especialidade", id));
    }

    public Optional<Especialidade> buscarPorNome(String nome) {
        return especialidadeRepository.buscarPorNome(nome);
    }

    public List<Especialidade> listarPorMedico(Long medicoId) {
        return especialidadeRepository.listarPorMedico(medicoId);
    }

    @Transactional
    public Especialidade criar(EspecialidadeDTO especialidadeDTO) {
        // Verificar se já existe especialidade com o mesmo nome
        Optional<Especialidade> existingEspecialidade = buscarPorNome(especialidadeDTO.getNome());
        if (existingEspecialidade.isPresent()) {
            throw new BusinessException("Já existe uma especialidade cadastrada com o nome: " + especialidadeDTO.getNome());
        }

        Especialidade especialidade = new Especialidade();
        especialidade.setNome(especialidadeDTO.getNome());
        especialidade.setDescricao(especialidadeDTO.getDescricao());

        especialidadeRepository.persist(especialidade);
        return especialidade;
    }

    @Transactional
    public Especialidade atualizar(Long id, EspecialidadeDTO especialidadeDTO) {
        Especialidade especialidade = buscarPorId(id);

        // Verificar se o nome já está em uso por outra especialidade
        if (!especialidade.getNome().equals(especialidadeDTO.getNome())) {
            Optional<Especialidade> existingEspecialidade = especialidadeRepository.buscarPorNome(especialidadeDTO.getNome());
            if (existingEspecialidade.isPresent()) {
                throw new BusinessException("Já existe uma especialidade cadastrada com o nome: " + especialidadeDTO.getNome());
            }
        }

        especialidade.setNome(especialidadeDTO.getNome());
        especialidade.setDescricao(especialidadeDTO.getDescricao());

        return especialidade;
    }

    @Transactional
    public void excluir(Long id) {
        Especialidade especialidade = buscarPorId(id);
        if (!especialidade.getMedicos().isEmpty()) {
            throw new BusinessException("Não é possível excluir a especialidade pois ela está associada a médicos");
        }
        especialidadeRepository.delete(especialidade);
    }

    public EspecialidadeDTO toDTO(Especialidade especialidade) {
        return new EspecialidadeDTO(
                especialidade.id,
                especialidade.getNome(),
                especialidade.getDescricao()
        );
    }

    public List<EspecialidadeDTO> toDTOList(List<Especialidade> especialidades) {
        return especialidades.stream()
                .map(this::toDTO)
                .toList();
    }
}
