package com.hospital.controller;

import com.hospital.dto.PacienteDTO;
import com.hospital.model.Paciente;
import com.hospital.service.PacienteService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/v1/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Paciente", description = "Operações relacionadas aos pacientes")
public class PacienteController {

    @Inject
    PacienteService pacienteService;

    @GET
    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista de todos os pacientes cadastrados no sistema")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de pacientes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class)))
    })
    public Response listarTodos() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return Response.ok(pacientes.stream()
                .map(pacienteService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Retorna um paciente específico com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @APIResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public Response buscarPorId(@PathParam("id") Long id) {
        Paciente paciente = pacienteService.buscarPorId(id);
        if (paciente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pacienteService.toDTO(paciente)).build();
    }

    @GET
    @Path("/busca")
    @Operation(summary = "Buscar pacientes por nome", description = "Retorna uma lista de pacientes cujo nome contenha o termo especificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de pacientes",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class)))
    })
    public Response buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do paciente", required = true)
            @QueryParam("nome") String nome) {
        List<Paciente> pacientes = pacienteService.buscarPorNome(nome);
        return Response.ok(pacientes.stream()
                .map(pacienteService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/cpf/{cpf}")
    @Operation(summary = "Buscar paciente por CPF", description = "Retorna um paciente específico com base no CPF fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @APIResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public Response buscarPorCpf(
            @Parameter(description = "CPF do paciente (apenas números)", required = true)
            @PathParam("cpf") String cpf) {
        Optional<Paciente> paciente = pacienteService.buscarPorCpf(cpf);
        if (paciente.isPresent()) {
            return Response.ok(pacienteService.toDTO(paciente.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Paciente com CPF " + cpf + " não encontrado").build();
    }

    @POST
    @Operation(summary = "Criar paciente", description = "Cria um novo paciente com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Paciente criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response criar(
            @RequestBody(description = "Dados do paciente", required = true,
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class)))
            @Valid PacienteDTO pacienteDTO,
            @Context UriInfo uriInfo) {

        Paciente paciente = pacienteService.criar(pacienteDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(paciente.id.toString()).build();
        return Response.created(location).entity(pacienteService.toDTO(paciente)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar paciente", description = "Atualiza os dados de um paciente existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Paciente atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PacienteDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public Response atualizar(
            @Parameter(description = "ID do paciente", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados do paciente", required = true,
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class)))
            @Valid PacienteDTO pacienteDTO) {

        Paciente paciente = pacienteService.atualizar(id, pacienteDTO);
        return Response.ok(pacienteService.toDTO(paciente)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir paciente", description = "Exclui um paciente existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Paciente não encontrado"),
            @APIResponse(responseCode = "400", description = "Não é possível excluir o paciente pois ele possui consultas associadas")
    })
    public Response excluir(
            @Parameter(description = "ID do paciente", required = true)
            @PathParam("id") Long id) {

        pacienteService.excluir(id);
        return Response.noContent().build();
    }

}
