package com.hospital.controller;

import com.hospital.dto.ExameDTO;
import com.hospital.model.Exame;
import com.hospital.model.enums.TipoExame;
import com.hospital.service.ExameService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

@Path("/api/v1/exames")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Exame", description = "Operações relacionadas aos exames médicos")
public class ExameController {

    @Inject
    ExameService exameService;

    @GET
    @Operation(summary = "Listar todos os exames", description = "Retorna uma lista de todos os exames cadastrados no sistema")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de exames",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class)))
    })
    public Response listarTodos() {
        List<Exame> exames = exameService.listarTodos();
        return Response.ok(exames.stream()
                .map(exameService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar exame por ID", description = "Retorna um exame específico com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Exame encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class))),
            @APIResponse(responseCode = "404", description = "Exame não encontrado")
    })
    public Response buscarPorId(
            @Parameter(description = "ID do exame", required = true)
            @PathParam("id") Long id) {
        Exame exame = exameService.buscarPorId(id);
        return Response.ok(exameService.toDTO(exame)).build();
    }

    @GET
    @Path("/consulta/{consultaId}")
    @Operation(summary = "Listar exames por consulta", description = "Retorna uma lista de exames associados a uma consulta específica")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de exames",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class)))
    })
    public Response listarPorConsulta(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("consultaId") Long consultaId) {
        List<Exame> exames = exameService.listarPorConsulta(consultaId);
        return Response.ok(exames.stream()
                .map(exameService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/paciente/{pacienteId}")
    @Operation(summary = "Listar exames por paciente", description = "Retorna uma lista de exames associados a um paciente específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de exames",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class)))
    })
    public Response listarPorPaciente(
            @Parameter(description = "ID do paciente", required = true)
            @PathParam("pacienteId") Long pacienteId) {
        List<Exame> exames = exameService.listarPorPaciente(pacienteId);
        return Response.ok(exames.stream()
                .map(exameService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/tipo/{tipo}")
    @Operation(summary = "Listar exames por tipo", description = "Retorna uma lista de exames do tipo especificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de exames",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class)))
    })
    public Response listarPorTipo(
            @Parameter(description = "Tipo de exame (LABORATORIAL, IMAGEM, OUTROS)", required = true)
            @PathParam("tipo") TipoExame tipo) {
        List<Exame> exames = exameService.listarPorTipo(tipo);
        return Response.ok(exames.stream()
                .map(exameService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/pendentes")
    @Operation(summary = "Listar exames sem resultado", description = "Retorna uma lista de exames que ainda não possuem resultado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de exames",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class)))
    })
    public Response listarSemResultado() {
        List<Exame> exames = exameService.listarSemResultado();
        return Response.ok(exames.stream()
                .map(exameService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Operation(summary = "Solicitar exame", description = "Solicita um novo exame com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Exame solicitado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos ou consulta não realizada")
    })
    public Response solicitar(
            @RequestBody(description = "Dados do exame", required = true,
                    content = @Content(schema = @Schema(implementation = ExameDTO.class)))
            @Valid ExameDTO exameDTO,
            @Context UriInfo uriInfo) {

        Exame exame = exameService.criar(exameDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(exame.id.toString()).build();
        return Response.created(location).entity(exameService.toDTO(exame)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar exame", description = "Atualiza os dados de um exame existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Exame atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Exame não encontrado")
    })
    public Response atualizar(
            @Parameter(description = "ID do exame", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados do exame", required = true,
                    content = @Content(schema = @Schema(implementation = ExameDTO.class)))
            @Valid ExameDTO exameDTO) {

        Exame exame = exameService.atualizar(id, exameDTO);
        return Response.ok(exameService.toDTO(exame)).build();
    }

    @PUT
    @Path("/{id}/resultado")
    @Operation(summary = "Registrar resultado do exame", description = "Registra o resultado de um exame existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Resultado registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExameDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Exame não encontrado")
    })
    public Response registrarResultado(
            @Parameter(description = "ID do exame", required = true)
            @PathParam("id") Long id,
            @Parameter(description = "Resultado do exame", required = true)
            @QueryParam("resultado") @NotBlank String resultado) {

        Exame exame = exameService.registrarResultado(id, resultado);
        return Response.ok(exameService.toDTO(exame)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir exame", description = "Exclui um exame existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Exame excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Exame não encontrado")
    })
    public Response excluir(
            @Parameter(description = "ID do exame", required = true)
            @PathParam("id") Long id) {

        exameService.excluir(id);
        return Response.noContent().build();
    }
}