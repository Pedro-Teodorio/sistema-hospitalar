package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link com.hospital.model.Prontuario}
 */
public class ProntuarioDTO implements Serializable {
    private final Long id;
    private final Long consultaId;
    @Size(message = "A anamnese deve ter entre 10 e 2000 caracteres", min = 10, max = 2000)
    @NotBlank(message = "A anamnese é obrigatória")
    private final String anamnese;
    @Size(message = "O diagnóstico deve ter no máximo 500 caracteres", max = 500)
    private final String diagnostico;
    @Size(message = "O plano de tratamento deve ter no máximo 1000 caracteres", max = 1000)
    private final String planoTratamento;
    @NotNull(message = "A data de criação é obrigatória")
    private final LocalDateTime dataCriacao;
    private final LocalDateTime dataAtualizacao;

    public ProntuarioDTO(Long id, Long consultaId, String anamnese, String diagnostico, String planoTratamento, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.consultaId = consultaId;
        this.anamnese = anamnese;
        this.diagnostico = diagnostico;
        this.planoTratamento = planoTratamento;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Long getId() {
        return id;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getPlanoTratamento() {
        return planoTratamento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProntuarioDTO entity = (ProntuarioDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.consultaId, entity.consultaId) &&
                Objects.equals(this.anamnese, entity.anamnese) &&
                Objects.equals(this.diagnostico, entity.diagnostico) &&
                Objects.equals(this.planoTratamento, entity.planoTratamento) &&
                Objects.equals(this.dataCriacao, entity.dataCriacao) &&
                Objects.equals(this.dataAtualizacao, entity.dataAtualizacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consultaId, anamnese, diagnostico, planoTratamento, dataCriacao, dataAtualizacao);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "consultaId = " + consultaId + ", " +
                "anamnese = " + anamnese + ", " +
                "diagnostico = " + diagnostico + ", " +
                "planoTratamento = " + planoTratamento + ", " +
                "dataCriacao = " + dataCriacao + ", " +
                "dataAtualizacao = " + dataAtualizacao + ")";
    }
}