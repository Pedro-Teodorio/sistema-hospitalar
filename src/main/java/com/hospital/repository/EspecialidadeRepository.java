package com.hospital.repository;

import com.hospital.model.Especialidade;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EspecialidadeRepository implements PanacheRepository<Especialidade> {

    public Optional<Especialidade> buscarPorNome(String nome) {
        return find("nome", nome).firstResultOptional();
    }

    public List<Especialidade> listarPorMedico(Long medicoId) {
        return list("SELECT e FROM Especialidade e JOIN e.medicos m WHERE m.id = ?1 ORDER BY e.nome", medicoId);
    }
}