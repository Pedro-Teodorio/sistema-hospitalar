package com.hospital.repository;

import com.hospital.model.Receita;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ReceitaRepository implements PanacheRepository<Receita> {

    public List<Receita> listarPorConsultaId(Long consultaId) {
        return list("consulta.id = ?1", consultaId);
    }

    public List<Receita> listarPorPacienteId(Long pacienteId) {
        return list("consulta.paciente.id = ?1", pacienteId);
    }

    public List<Receita> listarPorMedicamento(String medicamento) {
        return list("medicamento LIKE ?1", "%" + medicamento + "%");
    }

}




