package com.hospital.model;

import com.hospital.model.enums.TipoExame;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Schema(description = "Representa um exame médico")
public class Exame extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "consulta_id")
    @NotNull(message = "A consulta é obrigatória")
    @Schema(description = "Consulta associada ao exame")
    private Consulta consulta;

    @NotBlank(message = "O nome do exame é obrigatório")
    @Size(min = 3, max = 100, message = "O nome do exame deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome do exame")
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de exame é obrigatório")
    @Schema(description = "Tipo de exame")
    private TipoExame tipo;

    @Size(max = 500, message = "As instruções devem ter no máximo 500 caracteres")
    @Schema(description = "Instruções para o exame")
    private String instrucoes;

    @NotNull(message = "A data de solicitação é obrigatória")
    @Schema(description = "Data de solicitação do exame")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Schema(description = "Data de resultado do exame")
    private LocalDateTime dataResultado;

    @Size(max = 1000, message = "O resultado deve ter no máximo 1000 caracteres")
    @Schema(description = "Resultado do exame")
    private String resultado;

    // Getters e Setters
    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoExame getTipo() {
        return tipo;
    }

    public void setTipo(TipoExame tipo) {
        this.tipo = tipo;
    }

    public String getInstrucoes() {
        return instrucoes;
    }

    public void setInstrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataResultado() {
        return dataResultado;
    }

    public void setDataResultado(LocalDateTime dataResultado) {
        this.dataResultado = dataResultado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}