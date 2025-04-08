package com.hospital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * DTO for {@link com.hospital.model.Medico}
 */
public class MedicoDTO implements Serializable {
    private final Long id;
    @Size(message = "O nome deve ter entre 3 e 100 caracteres", min = 3, max = 100)
    @NotBlank(message = "O nome do médico é obrigatório")
    private final String nome;
    @Pattern(message = "CRM deve conter entre 4 e 6 dígitos", regexp = "\\d{4,6}")
    @NotBlank(message = "O CRM do médico é obrigatório")
    private final String crm;
    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private final String email;
    @Pattern(message = "Telefone deve conter entre 10 e 11 dígitos", regexp = "\\d{10,11}")
    @NotBlank(message = "O telefone é obrigatório")
    private final String telefone;
    private final Set<Long> especialidadeIds;

    public MedicoDTO(Long id, String nome, String crm, String email, String telefone, Set<Long> especialidadeIds) {
        this.id = id;
        this.nome = nome;
        this.crm = crm;
        this.email = email;
        this.telefone = telefone;
        this.especialidadeIds = especialidadeIds;
    }



    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCrm() {
        return crm;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Set<Long> getEspecialidadeIds() {
        return especialidadeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicoDTO entity = (MedicoDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nome, entity.nome) &&
                Objects.equals(this.crm, entity.crm) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.telefone, entity.telefone) &&
                Objects.equals(this.especialidadeIds, entity.especialidadeIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, crm, email, telefone, especialidadeIds);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nome = " + nome + ", " +
                "crm = " + crm + ", " +
                "email = " + email + ", " +
                "telefone = " + telefone + ", " +
                "especialidadeIds = " + especialidadeIds + ")";
    }
}