package com.hospital.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Table(name = "receitas")
@Schema(
        description = "Representa uma receita médica.",
        title = "Receita",
        oneOf = {Receita.class}
)
public class Receita extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    @NotNull(message = "A consulta é obrigatória")
    @Schema(description = "Consulta associada à receita")
    private Consulta consulta;

    @NotBlank(message = "O medicamento é obrigatório")
    @Size(min = 3, max = 100, message = "O nome do medicamento deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome do medicamento prescrito")
    private String medicamento;

    @NotBlank(message = "A posologia é obrigatória")
    @Size(min = 5, max = 500, message = "A posologia deve ter entre 5 e 500 caracteres")
    @Schema(description = "Posologia do medicamento prescrito")
    private String posologia;

    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres")
    @Schema(description = "Observações adicionais sobre a receita")
    private String observacoes;

    @NotNull(message = "A data de emissão é obrigatória")
    @Schema(description = "Data de emissão da receita")
    private LocalDateTime dataEmissao = LocalDateTime.now();

    @NotNull(message = "A data de validade é obrigatória")
    @Future(message = "A data de validade deve ser no futuro")
    @Schema(description = "Data de validade da receita")
    private LocalDateTime dataValidade;

    // Getters e Setters
    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public LocalDateTime getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDateTime dataValidade) {
        this.dataValidade = dataValidade;
    }
}