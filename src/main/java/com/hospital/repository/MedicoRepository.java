package com.hospital.repository;

import com.hospital.model.Medico;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MedicoRepository implements PanacheRepository<Medico> {

    public List<Medico> listarPorEspecialidade(Long especialidadeId) {
        return list("SELECT m FROM Medico m JOIN m.especialidades e WHERE e.id = ?1 ORDER BY m.nome", especialidadeId);    }

    public Optional<Medico> buscarPorCrm(String crm) {
        return find("crm", crm).firstResultOptional();
    }

    public List<Medico> buscarPorNome(String nome) {
        return list("nome LIKE ?1", Sort.ascending("nome"), "%" + nome + "%");
    }
}
