package com.hospital.dto;

import com.hospital.model.enums.StatusConsulta;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link com.hospital.model.Consulta}
 */
public class ConsultaDTO implements Serializable {
    private final Long id;
    @NotNull(message = "A data e hora da consulta são obrigatórias")
    @Future(message = "A data da consulta deve ser no futuro")
    private final LocalDateTime dataHora;
    @NotNull(message = "O status da consulta é obrigatório")
    private final StatusConsulta status;
    private final Long medicoId;
    private final Long pacienteId;
    @Size(message = "A observação deve ter no máximo 500 caracteres", max = 500)
    private final String observacao;

    public ConsultaDTO(Long id, LocalDateTime dataHora, StatusConsulta status, Long medicoId, Long pacienteId, String observacao) {
        this.id = id;
        this.dataHora = dataHora;
        this.status = status;
        this.medicoId = medicoId;
        this.pacienteId = pacienteId;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public String getObservacao() {
        return observacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultaDTO entity = (ConsultaDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.dataHora, entity.dataHora) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.medicoId, entity.medicoId) &&
                Objects.equals(this.pacienteId, entity.pacienteId) &&
                Objects.equals(this.observacao, entity.observacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataHora, status, medicoId, pacienteId, observacao);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "dataHora = " + dataHora + ", " +
                "status = " + status + ", " +
                "medicoId = " + medicoId + ", " +
                "pacienteId = " + pacienteId + ", " +
                "observacao = " + observacao + ")";
    }
}