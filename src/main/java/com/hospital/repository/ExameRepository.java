package com.hospital.repository;

import com.hospital.model.Exame;
import com.hospital.model.enums.TipoExame;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ExameRepository implements PanacheRepository<Exame> {

    public List<Exame> listarPorConsultaId(Long consultaId) {
        return list("consulta.id = ?1", Sort.ascending("dataSolicitacao"), consultaId);
    }

    public List<Exame> listarPorPacienteId(Long pacienteId) {
        return list("consulta.paciente.id = ?1", Sort.descending("dataSolicitacao"), pacienteId);
    }

    public List<Exame> listarPorTipo(TipoExame tipo) {
        return list("tipo = ?1", Sort.ascending("dataSolicitacao"), tipo);
    }

    public List<Exame> listarSemResultado() {
        return list("resultado IS NULL", Sort.ascending("dataSolicitacao"));
    }
}