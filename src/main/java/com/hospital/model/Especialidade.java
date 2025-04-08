package com.hospital.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "especialidades")
@Schema(
        description = "Representa uma especialidade médica.",
        title = "Especialidade",
        oneOf = {Especialidade.class}
)
public class Especialidade extends PanacheEntity {

    @NotBlank(message = "O nome da especialidade é obrigatório.")
    @Size(max = 100, message = "O nome da especialidade deve ter no máximo 100 caracteres.")
    @Column(unique = true)
    @Schema(description = "Nome da especialidade médica.")
    private String nome;

    @NotBlank(message = "A descrição da especialidade é obrigatória")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição da especialidade médica.")
    private String descricao;

    @ManyToMany(mappedBy = "especialidades")
    @Schema(description = "Lista de médicos associados a esta especialidade.")
    private Set<Medico> medicos = new HashSet<>();

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Medico> getMedicos() {
        return medicos;
    }

    public void setMedicos(Set<Medico> medicos) {
        this.medicos = medicos;
    }
}

