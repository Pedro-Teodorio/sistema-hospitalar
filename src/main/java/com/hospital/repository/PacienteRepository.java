package com.hospital.repository;

import com.hospital.model.Paciente;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PacienteRepository implements PanacheRepository<Paciente> {

    public Optional<Paciente> buscarPorCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional();
    }

    public List<Paciente> buscarPorNome(String nome) {
        return list("nome LIKE ?1", Sort.ascending("nome"), "%" + nome + "%");
    }
}
