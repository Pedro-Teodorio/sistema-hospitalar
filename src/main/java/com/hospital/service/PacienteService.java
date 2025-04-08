package com.hospital.service;

import com.hospital.dto.PacienteDTO;
import com.hospital.exception.BusinessException;
import com.hospital.exception.EntityNotFoundException;
import com.hospital.model.Paciente;
import com.hospital.repository.PacienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PacienteService {

    @Inject
    PacienteRepository pacienteRepository;

    public List<Paciente> listarTodos() {
        return pacienteRepository.listAll();
    }

    public Paciente buscarPorId(Long id) {
        return pacienteRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente", id));
    }

    public List<Paciente> buscarPorNome(String nome) {
        return pacienteRepository.buscarPorNome(nome);
    }

    public Optional<Paciente> buscarPorCpf(String cpf) {
        return pacienteRepository.buscarPorCpf(cpf);
    }

    @Transactional
    public Paciente criar(PacienteDTO pacienteDTO) {
        Optional<Paciente> pacienteExistente = pacienteRepository.buscarPorCpf(pacienteDTO.getCpf());
        if (pacienteExistente.isPresent()) {
            throw new EntityNotFoundException("Paciente já cadastrado com o CPF: " + pacienteDTO.getCpf());
        }

        Paciente paciente = new Paciente();
        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setEndereco(pacienteDTO.getEndereco());


        pacienteRepository.persist(paciente);
        return paciente;
    }

    @Transactional
    public Paciente atualizar(Long id, PacienteDTO pacienteDTO) {
        Paciente paciente = buscarPorId(id);

        if (!paciente.getCpf().equals(pacienteDTO.getCpf())) {
            Optional<Paciente> existingPaciente = pacienteRepository.buscarPorCpf(pacienteDTO.getCpf());
            if (existingPaciente.isPresent()) {
                throw new BusinessException("Já existe um paciente cadastrado com o CPF: " + pacienteDTO.getCpf());
            }
        }

        paciente.setNome(pacienteDTO.getNome());
        paciente.setCpf(pacienteDTO.getCpf());
        paciente.setDataNascimento(pacienteDTO.getDataNascimento());
        paciente.setEmail(pacienteDTO.getEmail());
        paciente.setTelefone(pacienteDTO.getTelefone());
        paciente.setEndereco(pacienteDTO.getEndereco());

        return paciente;
    }

    @Transactional
    public void excluir(Long id) {
        Paciente paciente = buscarPorId(id);
        if (!paciente.getConsultas().isEmpty()) {
            throw new BusinessException("Não é possível excluir o paciente pois ele possui consultas associadas");
        }
        pacienteRepository.delete(paciente);
    }

    public PacienteDTO toDTO(Paciente paciente) {
        return new PacienteDTO(
                paciente.id,
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getEmail(),
                paciente.getTelefone(),
                paciente.getEndereco()
        );
    }

    public List<PacienteDTO> toDTOList(List<Paciente> pacientes) {
        return pacientes.stream()
                .map(this::toDTO)
                .toList();
    }
}
