package com.hospital.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO for {@link com.hospital.model.Paciente}
 */
public class PacienteDTO implements Serializable {
    private final Long id;
    @Size(message = "O nome deve ter entre 3 e 100 caracteres", min = 3, max = 100)
    @NotBlank(message = "O nome do paciente é obrigatório")
    private final String nome;
    @Pattern(message = "CPF deve conter 11 dígitos", regexp = "\\d{11}")
    @NotBlank(message = "O CPF é obrigatório")
    private final String cpf;
    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private final LocalDate dataNascimento;
    @Email(message = "Email inválido")
    @NotBlank(message = "O email é obrigatório")
    private final String email;
    @Pattern(message = "Telefone deve conter entre 10 e 11 dígitos", regexp = "\\d{10,11}")
    @NotBlank(message = "O telefone é obrigatório")
    private final String telefone;
    @Size(message = "O endereço deve ter no máximo 200 caracteres", max = 200)
    @NotBlank(message = "O endereço é obrigatório")
    private final String endereco;

    public PacienteDTO(Long id, String nome, String cpf, LocalDate dataNascimento, String email, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PacienteDTO entity = (PacienteDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.nome, entity.nome) &&
                Objects.equals(this.cpf, entity.cpf) &&
                Objects.equals(this.dataNascimento, entity.dataNascimento) &&
                Objects.equals(this.email, entity.email) &&
                Objects.equals(this.telefone, entity.telefone) &&
                Objects.equals(this.endereco, entity.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cpf, dataNascimento, email, telefone, endereco);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "nome = " + nome + ", " +
                "cpf = " + cpf + ", " +
                "dataNascimento = " + dataNascimento + ", " +
                "email = " + email + ", " +
                "telefone = " + telefone + ", " +
                "endereco = " + endereco + ")";
    }
}