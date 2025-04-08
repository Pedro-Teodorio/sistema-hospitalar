# Sistema Hospitalar API

Este projeto implementa uma API REST para um Sistema Hospitalar completo utilizando Quarkus, fornecendo funcionalidades para gerenciamento de médicos, pacientes, consultas, prontuários, receitas e exames.

## Tecnologias Utilizadas

- Java 11+
- Quarkus
- Hibernate ORM with Panache
- RESTEasy Jackson
- Hibernate Validator
- H2 Database (em memória)
- OpenAPI (Swagger)

## Pré-requisitos

- JDK 11+
- Maven 3.8.1+
- Git

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas:

```
sistema-hospitalar/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/
│   │   │   ├── controller/      # Controladores REST 
│   │   │   ├── dto/             # Objetos de Transferência de Dados
│   │   │   ├── exception/       # Exceções personalizadas
│   │   │   ├── model/           # Entidades de domínio
│   │   │   │   └── enums/       # Enumerações
│   │   │   ├── repository/      # Repositórios de dados
│   │   │   ├── service/         # Serviços com lógica de negócios
│   │   │   └── util/            # Utilitários
│   │   └── resources/
│   │       ├── application.properties  # Configurações do aplicativo
│   │       └── import.sql               # Script SQL para dados iniciais
│   └── test/                     # Testes unitários e de integração
```

## Entidades e Relacionamentos

O sistema possui as seguintes entidades principais:

- **Médico**: Profissionais médicos com suas especialidades
- **Paciente**: Dados dos pacientes do hospital
- **Especialidade**: Áreas de atuação dos médicos
- **Consulta**: Agendamentos e atendimentos médicos
- **Prontuário**: Registros médicos das consultas
- **Receita**: Prescrições médicas
- **Exame**: Exames solicitados e seus resultados

### Relacionamentos:

- Médico - Especialidade: Many-to-Many
- Médico - Consulta: One-to-Many
- Paciente - Consulta: One-to-Many
- Consulta - Prontuário: One-to-One
- Consulta - Receita: One-to-Many
- Consulta - Exame: One-to-Many

## Instalação e Execução

### Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/sistema-hospitalar.git
cd sistema-hospitalar
```

### Executando em Modo de Desenvolvimento

```bash
./mvnw quarkus:dev
```

A aplicação estará disponível em http://localhost:8080

### Gerando o Pacote Executável

```bash
./mvnw package
```

Isso gerará o arquivo `sistema-hospitalar-1.0.0-runner.jar` no diretório `target/`.

### Executando a Aplicação Empacotada

```bash
java -jar target/sistema-hospitalar-1.0.0-runner.jar
```

## Documentação da API

A documentação completa da API usando OpenAPI (Swagger) está disponível em:

```
http://localhost:8080/swagger
```

## Endpoints Principais

### Médicos
- `GET /medicos` - Lista todos os médicos
- `GET /medicos/{id}` - Busca médico por ID
- `GET /medicos/busca?nome={nome}` - Busca médicos por nome
- `GET /medicos/especialidade/{especialidadeId}` - Lista médicos por especialidade
- `POST /medicos` - Cria um novo médico
- `PUT /medicos/{id}` - Atualiza um médico existente
- `DELETE /medicos/{id}` - Remove um médico

### Pacientes
- `GET /pacientes` - Lista todos os pacientes
- `GET /pacientes/{id}` - Busca paciente por ID
- `GET /pacientes/busca?nome={nome}` - Busca pacientes por nome
- `GET /pacientes/cpf/{cpf}` - Busca paciente por CPF
- `POST /pacientes` - Cria um novo paciente
- `PUT /pacientes/{id}` - Atualiza um paciente existente
- `DELETE /pacientes/{id}` - Remove um paciente

### Especialidades
- `GET /especialidades` - Lista todas as especialidades
- `GET /especialidades/{id}` - Busca especialidade por ID
- `GET /especialidades/nome/{nome}` - Busca especialidade por nome
- `GET /especialidades/medico/{medicoId}` - Lista especialidades de um médico
- `POST /especialidades` - Cria uma nova especialidade
- `PUT /especialidades/{id}` - Atualiza uma especialidade existente
- `DELETE /especialidades/{id}` - Remove uma especialidade

### Consultas
- `GET /consultas` - Lista todas as consultas
- `GET /consultas/{id}` - Busca consulta por ID
- `GET /consultas/medico/{medicoId}` - Lista consultas de um médico
- `GET /consultas/paciente/{pacienteId}` - Lista consultas de um paciente
- `GET /consultas/status/{status}` - Lista consultas por status
- `GET /consultas/periodo?inicio={datetime}&fim={datetime}` - Lista consultas em um período
- `POST /consultas` - Cria uma nova consulta
- `PUT /consultas/{id}` - Atualiza uma consulta existente
- `PUT /consultas/{id}/cancelar` - Cancela uma consulta
- `PUT /consultas/{id}/realizar` - Marca uma consulta como realizada
- `DELETE /consultas/{id}` - Remove uma consulta

### Prontuários, Receitas e Exames
- Endpoints completos para gestão de prontuários, receitas médicas e exames
- Funcionalidades para registrar resultados de exames e emitir receitas médicas

## Exemplo de Uso

### 1. Criar uma especialidade

```bash
curl -X POST http://localhost:8080/especialidades \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cardiologia",
    "descricao": "Tratamento de doenças relacionadas ao coração e sistema circulatório"
  }'
```

### 2. Criar um médico

```bash
curl -X POST http://localhost:8080/medicos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Dr. Carlos Silva",
    "crm": "123456",
    "email": "carlos.silva@hospital.com",
    "telefone": "11999998888",
    "especialidadeIds": [1]
  }'
```

### 3. Criar um paciente

```bash
curl -X POST http://localhost:8080/pacientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Pereira",
    "cpf": "12345678901",
    "dataNascimento": "1980-05-10",
    "email": "joao.pereira@email.com",
    "telefone": "11977776666",
    "endereco": "Rua das Flores, 123 - São Paulo/SP"
  }'
```

### 4. Agendar uma consulta

```bash
curl -X POST http://localhost:8080/consultas \
  -H "Content-Type: application/json" \
  -d '{
    "dataHora": "2025-04-15T14:00:00",
    "status": "AGENDADA",
    "medicoId": 1,
    "pacienteId": 1,
    "observacao": "Primeira consulta - Avaliação cardiovascular"
  }'
```

## Regras de Negócio Implementadas

- Verificação de disponibilidade de médico para agendamento
- Validação de consultas realizadas para registrar prontuários, receitas e exames
- Consistência no cancelamento e realização de consultas
- Validações de dados como CRM único para médicos e CPF único para pacientes
- Validações temporais para datas de consultas, validade de receitas, etc.



## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

## Autor

Pedro Teodorio - [GitHub](https://github.com/Pedro-Teodorio)

---

Desenvolvido como parte do projeto de estudo de APIs REST com Quarkus - 2025