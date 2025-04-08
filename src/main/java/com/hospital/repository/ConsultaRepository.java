package com.hospital.repository;

import com.hospital.model.Consulta;
import com.hospital.model.enums.StatusConsulta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ConsultaRepository implements PanacheRepository<Consulta> {

    public List<Consulta> listarPorMedico(Long medicoId) {
        return list("medico.id = ?1", Sort.by("dataHora"), medicoId);
    }

    public List<Consulta> listarPorPaciente(Long pacienteId) {
        return list("paciente.id = ?1", Sort.by("dataHora"), pacienteId);
    }

    public List<Consulta> listarPorIntervaloData(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return list("dataHora >= ?1 AND dataHora <= ?2", Sort.by("dataHora"), dataInicio, dataFim);
    }

   public List<Consulta> listarPorStatus(StatusConsulta status) {
       return list("status = ?1", Sort.by("dataHora"), status);
   }

   public boolean verificarDisponibilidadeMedico(Long medicoId, LocalDateTime dataHora, LocalDateTime dataHoraFim) {
       String query = "medico.id = ?1 AND dataHora BETWEEN ?2 AND ?3 AND status != ?4";
       return count(query, medicoId, dataHora, dataHoraFim, StatusConsulta.CANCELADA) == 0;
   }
}
