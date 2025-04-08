package com.hospital.controller;

import com.hospital.dto.EspecialidadeDTO;
import com.hospital.model.Especialidade;
import com.hospital.service.EspecialidadeService;
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

@Path("/api/v1/especialidades")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Especialidade", description = "Endpoints para gerenciamento de especialidades médicas")
public class EspecialidadeController {

    @Inject
    EspecialidadeService especialidadeService;

    @GET
    @Operation(summary = "Listar todas as especialidades", description = "Retorna uma lista de todas as especialidades cadastradas no sistema")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de especialidades",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EspecialidadeDTO.class)))
    })
    public Response listarTodas() {
        List<Especialidade> especialidades = especialidadeService.listarTodas();
        return Response.ok(especialidades.stream()
                .map(especialidadeService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar especialidade por ID", description = "Retorna uma especialidade específica com base no ID fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Especialidade encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EspecialidadeDTO.class))),
            @APIResponse(responseCode = "404", description = "Especialidade não encontrada")
    })
    public Response buscarPorId(
            @Parameter(description = "ID da especialidade", required = true)
            @PathParam("id") Long id) {
        Especialidade especialidade = especialidadeService.buscarPorId(id);
        return Response.ok(especialidadeService.toDTO(especialidade)).build();
    }

    @GET
    @Path("/nome/{nome}")
    @Operation(summary = "Buscar especialidade por nome", description = "Retorna uma especialidade específica com base no nome fornecido")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Especialidade encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EspecialidadeDTO.class))),
            @APIResponse(responseCode = "404", description = "Especialidade não encontrada")
    })
    public Response buscarPorNome(
            @Parameter(description = "Nome da especialidade", required = true)
            @PathParam("nome") String nome) {
        Optional<Especialidade> especialidade = especialidadeService.buscarPorNome(nome);
        if (especialidade.isPresent()) {
            return Response.ok(especialidadeService.toDTO(especialidade.get())).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Especialidade com nome " + nome + " não encontrada").build();
    }

    @GET
    @Path("/medico/{medicoId}")
    @Operation(summary = "Listar especialidades por médico", description = "Retorna uma lista de especialidades associadas a um médico específico")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lista de especialidades",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EspecialidadeDTO.class)))
    })
    public Response listarPorMedico(
            @Parameter(description = "ID do médico", required = true)
            @PathParam("medicoId") Long medicoId) {
        List<Especialidade> especialidades = especialidadeService.listarPorMedico(medicoId);
        return Response.ok(especialidades.stream()
                .map(especialidadeService::toDTO)
                .collect(Collectors.toList())).build();
    }

    @POST
    @Operation(summary = "Criar especialidade", description = "Cria uma nova especialidade com os dados fornecidos")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Especialidade criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EspecialidadeDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Response criar(
            @RequestBody(description = "Dados da especialidade", required = true,
                    content = @Content(schema = @Schema(implementation = EspecialidadeDTO.class)))
            @Valid EspecialidadeDTO especialidadeDTO,
            @Context UriInfo uriInfo) {

        Especialidade especialidade = especialidadeService.criar(especialidadeDTO);
        URI location = uriInfo.getAbsolutePathBuilder().path(especialidade.id.toString()).build();
        return Response.created(location).entity(especialidadeService.toDTO(especialidade)).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar especialidade", description = "Atualiza os dados de uma especialidade existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Especialidade atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EspecialidadeDTO.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Especialidade não encontrada")
    })
    public Response atualizar(
            @Parameter(description = "ID da especialidade", required = true)
            @PathParam("id") Long id,
            @RequestBody(description = "Dados atualizados da especialidade", required = true,
                    content = @Content(schema = @Schema(implementation = EspecialidadeDTO.class)))
            @Valid EspecialidadeDTO especialidadeDTO) {

        Especialidade especialidade = especialidadeService.atualizar(id, especialidadeDTO);
        return Response.ok(especialidadeService.toDTO(especialidade)).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir especialidade", description = "Exclui uma especialidade existente")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Especialidade excluída com sucesso"),
            @APIResponse(responseCode = "404", description = "Especialidade não encontrada"),
            @APIResponse(responseCode = "400", description = "Não é possível excluir a especialidade pois ela está associada a médicos")
    })
    public Response excluir(
            @Parameter(description = "ID da especialidade", required = true)
            @PathParam("id") Long id) {

        especialidadeService.excluir(id);
        return Response.noContent().build();
    }
}
