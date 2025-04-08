package com.hospital.controller;

import com.hospital.dto.ProntuarioDTO;
import com.hospital.model.Prontuario;
import com.hospital.service.ProntuarioService;
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
import java.util.stream.Collectors;

@Path("/api/v1/prontuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Prontuário", description = "Operações relacionadas aos prontuários médicos")
public class ProntuarioController {

    @Inject
    ProntuarioService prontuarioService;

    @GET
    @Operation(summary = "Listar todos os prontuários", description = "Retorna uma lista de todos os prontuários cadastrados no sistema")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de prontuários",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProntuarioDTO.class)))
    })
    public Response listarTodos() {
        List<Prontuario> prontuarios = prontuarioService.listarTodos();
        return Response.ok(prontuarios.stream()
                .map(prontuarioService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar prontuário por ID", description = "Retorna um prontuário específico com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Prontuário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProntuarioDTO.class))),
            @APIResponse(responseCode = "404", description = "Prontuário não encontrado")
    })
    public Response buscarPorId(
            @Parameter(description = "ID do prontuário", required = true)
            @PathParam("id") Long id) {
        Prontuario prontuario = prontuarioService.buscarPorId(id);
        return Response.ok(prontuarioService.toDTO(prontuario)).build();
    }

    @GET
    @Path("/consulta/{consultaId}")
    @Operation(summary = "Buscar prontuário por consulta", description = "Retorna o prontuário associado a uma consulta específica")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Prontuário encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProntuarioDTO.class))),
            @APIResponse(responseCode = "404", description = "Prontuário não encontrado para a consulta especificada")
    })
    public Response buscarPorConsulta(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("consultaId") Long consultaId) {
        Prontuario prontuario = prontuarioService.buscarPorConsultaId(consultaId);
        return Response.ok(prontuarioService.toDTO(prontuario)).build();
    }

    @GET
    @Path("/paciente/{pacienteId}")
    @Operation(summary = "Listar prontuários por paciente", description = "Retorna uma lista de prontuários associados a um paciente específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de prontuários",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProntuarioDTO.class)))
    })
    public Response listarPorPaciente(
            @Parameter(description = "ID do paciente", required = true)
            @PathParam("pacienteId") Long pacienteId) {
        List<Prontuario> prontuarios = prontuarioService.listarPorPacienteId(pacienteId);
        return Response.ok(prontuarios.stream()
                .map(prontuarioService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Operation(summary = "Criar prontuário", description = "Cria um novo prontuário com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Prontuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProntuarioDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos ou já existe um prontuário para a consulta")
    })
    public Response criar(
            @RequestBody(description = "Dados do prontuário", required = true,
                    content = @Content(schema = @Schema(implementation = ProntuarioDTO.class)))
            @Valid ProntuarioDTO prontuarioDTO,
            @Context UriInfo uriInfo) {

        Prontuario prontuario = prontuarioService.criar(prontuarioDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(prontuario.id.toString()).build();
        return Response.created(location).entity(prontuarioService.toDTO(prontuario)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar prontuário", description = "Atualiza os dados de um prontuário existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Prontuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProntuarioDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Prontuário não encontrado")
    })
    public Response atualizar(
            @Parameter(description = "ID do prontuário", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados do prontuário", required = true,
                    content = @Content(schema = @Schema(implementation = ProntuarioDTO.class)))
            @Valid ProntuarioDTO prontuarioDTO) {

        Prontuario prontuario = prontuarioService.atualizar(id, prontuarioDTO);
        return Response.ok(prontuarioService.toDTO(prontuario)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir prontuário", description = "Exclui um prontuário existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Prontuário excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Prontuário não encontrado")
    })
    public Response excluir(
            @Parameter(description = "ID do prontuário", required = true)
            @PathParam("id") Long id) {

        prontuarioService.excluir(id);
        return Response.noContent().build();
    }
}
