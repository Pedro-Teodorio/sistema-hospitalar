package com.hospital.repository;

import com.hospital.model.Prontuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class ProntuarioRepository implements PanacheRepository<Prontuario> {

    public Optional<Prontuario> buscarPorConsultaId(Long consultaId) {
        return find("consulta.id = ?1", consultaId).firstResultOptional();
    }

    public List<Prontuario> listarPorPacienteId(Long pacienteId) {
        return list("consulta.paciente.id = ?1", Sort.descending("dataCriacao"), pacienteId);
    }
}
