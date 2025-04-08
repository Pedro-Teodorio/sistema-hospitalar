package com.hospital.controller;

import com.hospital.dto.MedicoDTO;
import com.hospital.model.Medico;
import com.hospital.service.MedicoService;
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

@Path("/api/v1/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Medico", description = "Endpoints para gerenciamento de médicos")
public class MedicoController {

    @Inject
    MedicoService medicoService;

    @GET
    @Operation(summary = "Listar todos os médicos", description = "Retorna uma lista de todos os médicos cadastrados")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de médicos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class)))
    })
    public Response listarTodos() {
        List<Medico> medicos = medicoService.listarTodos();
        return Response.ok(medicos.stream().map(medicoService::toDTO).collect(Collectors.toList()))
                .build();

    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar médico por ID", description = "Retorna um médico específico com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Médico encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class))),
            @APIResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public Response buscarPorId(
            @Parameter(description = "ID do médico", required = true)
            @PathParam("id") Long id) {
        Medico medico = medicoService.buscarPorId(id);
        return Response.ok(medicoService.toDTO(medico)).build();
    }

    @GET
    @Path("/busca")
    @Operation(summary = "Buscar médicos por nome", description = "Retorna uma lista de médicos cujo nome contenha o termo especificado")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de médicos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class)))
    })
    public Response buscarPorNome(
            @Parameter(description = "Nome ou parte do nome do médico", required = true)
            @QueryParam("nome") String nome) {
        List<Medico> medicos = medicoService.buscarPorNome(nome);
        return Response.ok(medicos.stream()
                .map(medicoService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/especialidade/{especialidadeId}")
    @Operation(summary = "Listar médicos por especialidade", description = "Retorna uma lista de médicos que possuem a especialidade especificada")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de médicos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class)))
    })
    public Response listarPorEspecialidade(
            @Parameter(description = "ID da especialidade", required = true)
            @PathParam("especialidadeId") Long especialidadeId) {
        List<Medico> medicos = medicoService.listarPorEspecialidade(especialidadeId);
        return Response.ok(medicos.stream()
                .map(medicoService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Operation(summary = "Criar médico", description = "Cria um novo médico com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Médico criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response criar(
            @RequestBody(description = "Dados do médico", required = true,
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class)))
            @Valid MedicoDTO medicoDTO,
            @Context UriInfo uriInfo) {

        Medico medico = medicoService.criar(medicoDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(medico.id.toString()).build();
        return Response.created(location).entity(medicoService.toDTO(medico)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar médico", description = "Atualiza os dados de um médico existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Médico atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicoDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public Response atualizar(
            @Parameter(description = "ID do médico", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados do médico", required = true,
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class)))
            @Valid MedicoDTO medicoDTO) {

        Medico medico = medicoService.atualizar(id, medicoDTO);
        return Response.ok(medicoService.toDTO(medico)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir médico", description = "Exclui um médico existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Médico excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Médico não encontrado"),
            @APIResponse(responseCode = "400", description = "Não é possível excluir o médico pois ele possui consultas associadas")
    })
    public Response excluir(
            @Parameter(description = "ID do médico", required = true)
            @PathParam("id") Long id) {

        medicoService.excluir(id);
        return Response.noContent().build();
    }
}
