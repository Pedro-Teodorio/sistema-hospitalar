package com.hospital.controller;

import com.hospital.dto.ReceitaDTO;
import com.hospital.model.Receita;
import com.hospital.service.ReceitaService;
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

@Path("/api/v1/receitas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Receita", description = "Operações relacionadas às receitas médicas")
public class ReceitaController {

    @Inject
    ReceitaService receitaService;

    @GET
    @Operation(summary = "Listar todas as receitas", description = "Retorna uma lista de todas as receitas cadastradas no sistema")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de receitas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class)))
    })
    public Response listarTodas() {
        List<Receita> receitas = receitaService.listarTodas();
        return Response.ok(receitas.stream()
                .map(receitaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar receita por ID", description = "Retorna uma receita específica com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Receita encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class))),
            @APIResponse(responseCode = "404", description = "Receita não encontrada")
    })
    public Response buscarPorId(
            @Parameter(description = "ID da receita", required = true)
            @PathParam("id") Long id) {
        Receita receita = receitaService.buscarPorId(id);
        return Response.ok(receitaService.toDTO(receita)).build();
    }

    @GET
    @Path("/consulta/{consultaId}")
    @Operation(summary = "Listar receitas por consulta", description = "Retorna uma lista de receitas associadas a uma consulta específica")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de receitas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class)))
    })
    public Response listarPorConsulta(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("consultaId") Long consultaId) {
        List<Receita> receitas = receitaService.listarPorConsulta(consultaId);
        return Response.ok(receitas.stream()
                .map(receitaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/paciente/{pacienteId}")
    @Operation(summary = "Listar receitas por paciente", description = "Retorna uma lista de receitas associadas a um paciente específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de receitas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class)))
    })
    public Response listarPorPaciente(
            @Parameter(description = "ID do paciente", required = true)
            @PathParam("pacienteId") Long pacienteId) {
        List<Receita> receitas = receitaService.listarPorPaciente(pacienteId);
        return Response.ok(receitas.stream()
                .map(receitaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/medicamento")
    @Operation(summary = "Buscar receitas por medicamento", description = "Retorna uma lista de receitas que contêm o medicamento especificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de receitas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class)))
    })
    public Response buscarPorMedicamento(
            @Parameter(description = "Nome ou parte do nome do medicamento", required = true)
            @QueryParam("nome") String nome) {
        List<Receita> receitas = receitaService.listarPorMedicamento(nome);
        return Response.ok(receitas.stream()
                .map(receitaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Operation(summary = "Criar receita", description = "Cria uma nova receita com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Receita criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos ou consulta não realizada")
    })
    public Response criar(
            @RequestBody(description = "Dados da receita", required = true,
                    content = @Content(schema = @Schema(implementation = ReceitaDTO.class)))
            @Valid ReceitaDTO receitaDTO,
            @Context UriInfo uriInfo) {

        Receita receita = receitaService.criar(receitaDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(receita.id.toString()).build();
        return Response.created(location).entity(receitaService.toDTO(receita)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar receita", description = "Atualiza os dados de uma receita existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Receita atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReceitaDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Receita não encontrada")
    })
    public Response atualizar(
            @Parameter(description = "ID da receita", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados da receita", required = true,
                    content = @Content(schema = @Schema(implementation = ReceitaDTO.class)))
            @Valid ReceitaDTO receitaDTO) {

        Receita receita = receitaService.atualizar(id, receitaDTO);
        return Response.ok(receitaService.toDTO(receita)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir receita", description = "Exclui uma receita existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Receita excluída com sucesso"),
            @APIResponse(responseCode = "404", description = "Receita não encontrada")
    })
    public Response excluir(
            @Parameter(description = "ID da receita", required = true)
            @PathParam("id") Long id) {

        receitaService.excluir(id);
        return Response.noContent().build();
    }
}
