package com.hospital.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;


@OpenAPIDefinition(
        info = @Info(
                title = "Sistema Hospitalar API",
                version = "1.0.0",
                description = "API REST para gerenciamento completo de um sistema hospitalar, " +
                        "permitindo o cadastro de médicos, pacientes, agendamento de consultas, " +
                        "emissão de receitas, solicitação de exames e gestão de prontuários. " +
                        "<h3>Fluxo de Criação</h3>" +
                        "<ol>" +
                        "<li><strong>Especialidades</strong>: Primeiro cadastre as especialidades médicas</li>" +
                        "<li><strong>Médicos</strong>: Cadastre os médicos associando-os às especialidades</li>" +
                        "<li><strong>Pacientes</strong>: Cadastre os pacientes do sistema</li>" +
                        "<li><strong>Consultas</strong>: Agende consultas entre médicos e pacientes</li>" +
                        "<li><strong>Realização da Consulta</strong>: Marque a consulta como realizada quando ocorrer</li>" +
                        "<li><strong>Prontuário</strong>: Crie o prontuário com informações da consulta realizada</li>" +
                        "<li><strong>Receitas</strong>: Emita receitas associadas à consulta</li>" +
                        "<li><strong>Exames</strong>: Solicite exames associados à consulta</li>" +
                        "<li><strong>Resultados</strong>: Registre os resultados dos exames posteriormente</li>" +
                        "</ol>",
                contact = @Contact(
                        name = "Pedro Teodorio",
                        url = "https://github.com/Pedro-Teodorio"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),

        tags = {
                @Tag(name = "Especialidade", description = "Operações relacionadas às especialidades médicas - Primeiro passo na configuração do sistema"),
                @Tag(name = "Médico", description = "Operações relacionadas aos médicos - Segundo passo na configuração do sistema"),
                @Tag(name = "Paciente", description = "Operações relacionadas aos pacientes - Terceiro passo na configuração do sistema"),
                @Tag(name = "Consulta", description = "Operações relacionadas às consultas - Agendamento, cancelamento e realização de consultas"),
                @Tag(name = "Prontuário", description = "Operações relacionadas aos prontuários médicos - Criado após a realização de consultas"),
                @Tag(name = "Receita", description = "Operações relacionadas às receitas médicas - Prescrições de medicamentos"),
                @Tag(name = "Exame", description = "Operações relacionadas aos exames médicos - Solicitação e resultados de exames")
        }
)
public class OpenApiConfig extends Application {
    // Classe vazia, apenas para configuração
}