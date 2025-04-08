package com.hospital.controller;

import com.hospital.dto.ConsultaDTO;
import com.hospital.model.Consulta;
import com.hospital.model.enums.StatusConsulta;
import com.hospital.service.ConsultaService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/v1/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Consulta", description = "Operações relacionadas às consultas médicas")
public class ConsultaController {

    @Inject
    ConsultaService consultaService;

    @GET
    @Operation(summary = "Listar todas as consultas", description = "Retorna uma lista de todas as consultas cadastradas no sistema")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de consultas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class)))
    })
    public Response listarTodas() {
        List<Consulta> consultas = consultaService.listarTodas();
        return Response.ok(consultas.stream()
                .map(consultaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar consulta por ID", description = "Retorna uma consulta específica com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Consulta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class))),
            @APIResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public Response buscarPorId(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("id") Long id) {
        Consulta consulta = consultaService.buscarPorId(id);
        return Response.ok(consultaService.toDTO(consulta)).build();
    }

    @GET
    @Path("/medico/{medicoId}")
    @Operation(summary = "Listar consultas por médico", description = "Retorna uma lista de consultas associadas a um médico específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de consultas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class)))
    })
    public Response listarPorMedico(
            @Parameter(description = "ID do médico", required = true)
            @PathParam("medicoId") Long medicoId) {
        List<Consulta> consultas = consultaService.listarPorMedico(medicoId);
        return Response.ok(consultas.stream()
                .map(consultaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/paciente/{pacienteId}")
    @Operation(summary = "Listar consultas por paciente", description = "Retorna uma lista de consultas associadas a um paciente específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de consultas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class)))
    })
    public Response listarPorPaciente(
            @Parameter(description = "ID do paciente", required = true)
            @PathParam("pacienteId") Long pacienteId) {
        List<Consulta> consultas = consultaService.listarPorPaciente(pacienteId);
        return Response.ok(consultas.stream()
                .map(consultaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/status/{status}")
    @Operation(summary = "Listar consultas por status", description = "Retorna uma lista de consultas com o status especificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de consultas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class)))
    })
    public Response listarPorStatus(
            @Parameter(description = "Status da consulta (AGENDADA, REALIZADA, CANCELADA)", required = true)
            @PathParam("status") StatusConsulta status) {
        List<Consulta> consultas = consultaService.listarPorStatus(status);
        return Response.ok(consultas.stream()
                .map(consultaService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/periodo")
    @Operation(summary = "Listar consultas por período", description = "Retorna uma lista de consultas dentro do período especificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de consultas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class))),
            @APIResponse(responseCode = "400", description = "Parâmetros de data inválidos")
    })
    public Response listarPorPeriodo(
            @Parameter(description = "Data e hora de início (formato ISO: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @QueryParam("inicio") String inicio,
            @Parameter(description = "Data e hora de fim (formato ISO: yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @QueryParam("fim") String fim) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime dataInicio = LocalDateTime.parse(inicio, formatter);
            LocalDateTime dataFim = LocalDateTime.parse(fim, formatter);

            List<Consulta> consultas = consultaService.listarPorIntervaloData(dataInicio, dataFim);
            return Response.ok(consultas.stream()
                    .map(consultaService::toDTO)
                    .collect(Collectors.toList())).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de data inválido. Use o formato ISO: yyyy-MM-dd'T'HH:mm:ss").build();
        }
    }

    @POST
    @Operation(summary = "Agendar consulta", description = "Agenda uma nova consulta com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Consulta agendada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos ou conflito de horário")
    })
    public Response agendar(
            @RequestBody(description = "Dados da consulta", required = true,
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class)))
            @Valid ConsultaDTO consultaDTO,
            @Context UriInfo uriInfo) {

        Consulta consulta = consultaService.criar(consultaDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(consulta.id.toString()).build();
        return Response.created(location).entity(consultaService.toDTO(consulta)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar consulta", description = "Atualiza os dados de uma consulta existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Consulta atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos ou consulta já realizada/cancelada"),
            @APIResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public Response atualizar(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados da consulta", required = true,
                    content = @Content(schema = @Schema(implementation = ConsultaDTO.class)))
            @Valid ConsultaDTO consultaDTO) {

        Consulta consulta = consultaService.atualizar(id, consultaDTO);
        return Response.ok(consultaService.toDTO(consulta)).build();
    }

    @PUT
    @Path("/{id}/cancelar")
    @Operation(summary = "Cancelar consulta", description = "Cancela uma consulta existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Consulta cancelada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class))),
            @APIResponse(responseCode = "400", description = "Consulta já realizada ou já cancelada"),
            @APIResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public Response cancelar(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("id") Long id) {

        Consulta consulta = consultaService.cancelarConsulta(id);
        return Response.ok(consultaService.toDTO(consulta)).build();
    }

    @PUT
    @Path("/{id}/realizar")
    @Operation(summary = "Marcar consulta como realizada", description = "Marca uma consulta como realizada")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Consulta marcada como realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsultaDTO.class))),
            @APIResponse(responseCode = "400", description = "Consulta já realizada ou cancelada"),
            @APIResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public Response realizar(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("id") Long id) {

        Consulta consulta = consultaService.realizarConsulta(id);
        return Response.ok(consultaService.toDTO(consulta)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir consulta", description = "Exclui uma consulta existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Consulta excluída com sucesso"),
            @APIResponse(responseCode = "404", description = "Consulta não encontrada"),
            @APIResponse(responseCode = "400", description = "Não é possível excluir a consulta pois ela já foi realizada ou possui prontuário/receitas/exames associados")
    })
    public Response excluir(
            @Parameter(description = "ID da consulta", required = true)
            @PathParam("id") Long id) {

        consultaService.excluir(id);
        return Response.noContent().build();
    }
}