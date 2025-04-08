package com.hospital.model;


import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Table(name = "prontuarios")
@Schema(
        description = "Representa um prontuário médico.",
        title = "Prontuário",
        oneOf = {Prontuario.class})
public class Prontuario extends PanacheEntity {

    @OneToOne
    @JoinColumn(name = "consulta_id")
    @NotNull(message = "A consulta é obrigatória")
    @Schema(description = "Consulta associada ao prontuário")
    private Consulta consulta;

    @NotBlank(message = "A anamnese é obrigatória")
    @Size(min = 10, max = 2000, message = "A anamnese deve ter entre 10 e 2000 caracteres")
    @Schema(description = "Anamnese do paciente")
    private String anamnese;

    @Size(max = 500, message = "O diagnóstico deve ter no máximo 500 caracteres")
    @Schema(description = "Diagnóstico do paciente")
    private String diagnostico;

    @Size(max = 1000, message = "O plano de tratamento deve ter no máximo 1000 caracteres")
    @Schema(description = "Plano de tratamento do paciente")
    private String planoTratamento;

    @NotNull(message = "A data de criação é obrigatória")
    @Schema(description = "Data de criação do prontuário")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Schema(description = "Data da última atualização do prontuário")
    private LocalDateTime dataAtualizacao;

    // Getters e Setters
    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPlanoTratamento() {
        return planoTratamento;
    }

    public void setPlanoTratamento(String planoTratamento) {
        this.planoTratamento = planoTratamento;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}