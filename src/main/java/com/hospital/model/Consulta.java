package com.hospital.model;

import com.hospital.model.enums.StatusConsulta;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consultas")
@Schema(
        description = "Representa uma consulta médica.",
        title = "Consulta",
        oneOf = {Consulta.class})
public class Consulta extends PanacheEntity {

    @NotNull(message = "A data e hora da consulta são obrigatórias")
    @Future(message = "A data da consulta deve ser no futuro")
    @Schema(description = "Data e hora da consulta")
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status da consulta é obrigatório")
    @Schema(description = "Status atual da consulta")
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    @NotNull(message = "O médico é obrigatório")
    @Schema(description = "Médico responsável pela consulta")
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    @NotNull(message = "O paciente é obrigatório")
    @Schema(description = "Paciente que está realizando a consulta")
    private Paciente paciente;

    @Size(max = 500, message = "A observação deve ter no máximo 500 caracteres")
    @Schema(description = "Observações adicionais sobre a consulta")
    private String observacao;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL)
    @Schema(description = "Prontuário médico associado à consulta")
    private Prontuario prontuario;

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL)
    @Schema(description = "Lista de receitas associadas à consulta")
    private List<Receita> receitas = new ArrayList<>();

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL)
    @Schema(description = "Lista de exames associados à consulta")
    private List<Exame> exames = new ArrayList<>();

    // Getters e Setters
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public void setProntuario(Prontuario prontuario) {
        this.prontuario = prontuario;
    }

    public List<Receita> getReceitas() {
        return receitas;
    }

    public void setReceitas(List<Receita> receitas) {
        this.receitas = receitas;
    }

    public List<Exame> getExames() {
        return exames;
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }
}