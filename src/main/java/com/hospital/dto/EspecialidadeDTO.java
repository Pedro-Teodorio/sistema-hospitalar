package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link com.hospital.model.Especialidade}
 */
public class EspecialidadeDTO implements Serializable {
    private final Long id;
    @Size(message = "O nome da especialidade deve ter no máximo 100 caracteres.", max = 100)
    @NotBlank(message = "O nome da especialidade é obrigatório.")
    private final String nome;
    @Size(message = "A descrição deve ter no máximo 500 caracteres", max = 500)
    @NotBlank(message = "A descrição da especialidade é obrigatória")
    private final String descricao;

    public EspecialidadeDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EspecialidadeDTO entity = (EspecialidadeDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nome, entity.nome) &&
                Objects.equals(this.descricao, entity.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nome = " + nome + ", " +
                "descricao = " + descricao + ")";
    }
}