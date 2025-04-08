package com.hospital.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link com.hospital.model.Receita}
 */
public class ReceitaDTO implements Serializable {
    private final Long id;
    private final Long consultaId;
    @Size(message = "O nome do medicamento deve ter entre 3 e 100 caracteres", min = 3, max = 100)
    @NotBlank(message = "O medicamento é obrigatório")
    private final String medicamento;
    @Size(message = "A posologia deve ter entre 5 e 500 caracteres", min = 5, max = 500)
    @NotBlank(message = "A posologia é obrigatória")
    private final String posologia;
    @Size(message = "As observações devem ter no máximo 500 caracteres", max = 500)
    private final String observacoes;
    @NotNull(message = "A data de emissão é obrigatória")
    private final LocalDateTime dataEmissao;
    @NotNull(message = "A data de validade é obrigatória")
    @Future(message = "A data de validade deve ser no futuro")
    private final LocalDateTime dataValidade;

    public ReceitaDTO(Long id, Long consultaId, String medicamento, String posologia, String observacoes, LocalDateTime dataEmissao, LocalDateTime dataValidade) {
        this.id = id;
        this.consultaId = consultaId;
        this.medicamento = medicamento;
        this.posologia = posologia;
        this.observacoes = observacoes;
        this.dataEmissao = dataEmissao;
        this.dataValidade = dataValidade;
    }

    public Long getId() {
        return id;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public String getPosologia() {
        return posologia;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public LocalDateTime getDataValidade() {
        return dataValidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceitaDTO entity = (ReceitaDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.consultaId, entity.consultaId) &&
                Objects.equals(this.medicamento, entity.medicamento) &&
                Objects.equals(this.posologia, entity.posologia) &&
                Objects.equals(this.observacoes, entity.observacoes) &&
                Objects.equals(this.dataEmissao, entity.dataEmissao) &&
                Objects.equals(this.dataValidade, entity.dataValidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consultaId, medicamento, posologia, observacoes, dataEmissao, dataValidade);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "consultaId = " + consultaId + ", " +
                "medicamento = " + medicamento + ", " +
                "posologia = " + posologia + ", " +
                "observacoes = " + observacoes + ", " +
                "dataEmissao = " + dataEmissao + ", " +
                "dataValidade = " + dataValidade + ")";
    }
}