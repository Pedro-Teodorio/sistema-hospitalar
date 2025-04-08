package com.hospital.dto;

import com.hospital.model.enums.TipoExame;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * DTO for {@link com.hospital.model.Exame}
 */
public class ExameDTO implements Serializable {
    private final Long id;
    private final Long consultaId;
    @Size(message = "O nome do exame deve ter entre 3 e 100 caracteres", min = 3, max = 100)
    @NotBlank(message = "O nome do exame é obrigatório")
    private final String nome;
    @NotNull(message = "O tipo de exame é obrigatório")
    private final TipoExame tipo;
    @Size(message = "As instruções devem ter no máximo 500 caracteres", max = 500)
    private final String instrucoes;
    @NotNull(message = "A data de solicitação é obrigatória")
    private final LocalDateTime dataSolicitacao;
    private final LocalDateTime dataResultado;
    @Size(message = "O resultado deve ter no máximo 1000 caracteres", max = 1000)
    private final String resultado;

    public ExameDTO(Long id, Long consultaId, String nome, TipoExame tipo, String instrucoes, LocalDateTime dataSolicitacao, LocalDateTime dataResultado, String resultado) {
        this.id = id;
        this.consultaId = consultaId;
        this.nome = nome;
        this.tipo = tipo;
        this.instrucoes = instrucoes;
        this.dataSolicitacao = dataSolicitacao;
        this.dataResultado = dataResultado;
        this.resultado = resultado;
    }

    public Long getId() {
        return id;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public String getNome() {
        return nome;
    }

    public TipoExame getTipo() {
        return tipo;
    }

    public String getInstrucoes() {
        return instrucoes;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDateTime getDataResultado() {
        return dataResultado;
    }

    public String getResultado() {
        return resultado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExameDTO entity = (ExameDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.consultaId, entity.consultaId) &&
                Objects.equals(this.nome, entity.nome) &&
                Objects.equals(this.tipo, entity.tipo) &&
                Objects.equals(this.instrucoes, entity.instrucoes) &&
                Objects.equals(this.dataSolicitacao, entity.dataSolicitacao) &&
                Objects.equals(this.dataResultado, entity.dataResultado) &&
                Objects.equals(this.resultado, entity.resultado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consultaId, nome, tipo, instrucoes, dataSolicitacao, dataResultado, resultado);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "consultaId = " + consultaId + ", " +
                "nome = " + nome + ", " +
                "tipo = " + tipo + ", " +
                "instrucoes = " + instrucoes + ", " +
                "dataSolicitacao = " + dataSolicitacao + ", " +
                "dataResultado = " + dataResultado + ", " +
                "resultado = " + resultado + ")";
    }
}