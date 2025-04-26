# SIPE - Sistema Integrado de Ponto Eletrônico

API RESTful para controle de ponto eletrônico, gestão de registros de entrada e saída e geração de relatórios mensais de frequência. Integração com sistemas externos de controle de acesso e JSARH para automação do processo.

## Índice
- [Visão Geral](#visão-geral)
- [Recursos](#recursos)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Configuração](#configuração)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Exemplos de Uso](#exemplos-de-uso)
- [Relatórios](#relatórios)
- [Contribuição](#contribuição)
- [Licença](#licença)

## Visão Geral
O SIPE fornece uma solução backend para gestão de ponto eletrônico, capturando registros de entrada e saída de funcionários, calculando horas de permanência, armazenando dados no banco de dados e gerando relatórios PDF mensais. Integra-se com:
- Sistema de Controle de Acesso (via Feign client, serviço externo).
- Sistema JF1 (JSARH) para obter informações de feriados, licenças e ausências.

## Recursos
- CRUD de Usuários: cadastro, consulta, atualização e remoção.
- Gestão de Pontos: criação manual e automática, consulta por período.
- Gestão de Registros: inclusão, atualização e listagem de registros de ponto.
- Upload e atualização de arquivos binários.
- Geração de relatórios mensais em PDF (JasperReports).
- HATEOAS nas respostas para navegação entre recursos.
- Validação de dados com Bean Validation.
- Circuit Breaker e Resiliência (Resilience4j).
- Clientes HTTP declarativos (OpenFeign).
- Suporte a CORS configurado para aplicações front-end.

## Tecnologias
- Java 21
- Spring Boot 3.4.4
- Spring Data JPA (Hibernate)
- Spring Cloud OpenFeign
- Resilience4j
- JasperReports
- H2 Database (testes)
- MySQL (produção)
- Maven
- Lombok
- Spring HATEOAS
- Spring Validation

## Pré-requisitos
- Java 21 ou superior
- Maven 3.8+
- MySQL 8+ (ou outro banco compatível via JDBC)
- Docker (opcional, para execução em container)

## Instalação e Execução
1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   ```
2. Acesse a pasta do projeto:
   ```bash
   cd sipe
   ```
3. Configure as variáveis de ambiente ou `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://<host>:<porta>/<database>
   spring.datasource.username=<usuario>
   spring.datasource.password=<senha>

   ponto.coletor.url=http://<url-coletor>
   servidor.jsarh.url=http://<url-jsarh>
   ```
4. Build e run:
   ```bash
   mvn clean package
   java -jar target/sipe-0.3.5-SNAPSHOT.jar
   ```
5. (Opcional) Execução em modo dev:
   ```bash
   mvn spring-boot:run
   ```
6. (Opcional) Docker:
   ```bash
   docker build -t sipe .
   docker run -p 8080:8080 --env-file .env sipe
   ```

## Estrutura do Projeto
```text
src/main/java/br/jus/trf1/sipe
├── arquivo       # Upload e atualização de arquivos
├── usuario       # Operações CRUD de usuários
├── ponto         # Criação e cálculo de pontos diários
├── registro      # Gestão de registros de entrada/saída
├── relatorio     # Geração de relatórios PDF
├── externo       # Clientes Feign para serviços externos (coletor, JSARH)
├── comum         # Configurações, utilitários e validadores
├── erro          # Tratamento global de exceções
└── SipeApplication.java  # Inicialização do Spring Boot
```

## Endpoints da API
Basepath: `http://<host>:8080`

### Arquivos
- `POST /v1/sipe/arquivos`  
  Parâmetros: `bytes` (MultipartFile), `nome` (String), `descricao` (String, opcional)
- `PATCH /v1/sipe/arquivos`  
  Parâmetros: mesmo que POST

### Usuários
- `POST /v1/sipe/usuarios` — Cadastra usuário  
- `GET /v1/sipe/usuarios` — Lista usuários (filtros: `nome`, `cracha`, `matricula`, paginação: `page`, `size`)  
- `GET /v1/sipe/usuarios/{id}` — Consulta usuário por ID  
- `PUT /v1/sipe/usuarios/{id}` — Atualiza usuário  
- `DELETE /v1/sipe/usuarios/{id}` — Remove usuário  

### Pontos
- `POST /v1/sipe/pontos` — Cria ponto manual (body: PontoNovoRequest)  
- `POST /v1/sipe/pontos/usuarios/{matricula}?dia={dia}` — Cria ponto automático  
- `POST /v1/sipe/pontos/usuarios?matricula={matricula}&inicio={data}&fim={data}`  
- `GET /v1/sipe/pontos/{matricula}/{dia}` — Consulta ponto  
- `GET /v1/sipe/pontos?matricula={matricula}&inicio={data}&fim={data}`  
- `PUT /v1/sipe/pontos` — Atualiza ponto (body: PontoAtualizadoRequest)  

### Registros
- `POST /v1/sipe/registros/pontos?matricula={matricula}&dia={data}` — Adiciona registros  
- `PATCH /v1/sipe/registros/pontos?matricula={matricula}&dia={data}` — Atualiza registros automáticos  
- `PUT /v1/sipe/registros/pontos?matricula={matricula}&dia={data}` — Atualiza registro específico  
- `GET /v1/sipe/registros/pontos?matricula={matricula}&dia={data}` — Lista registros do ponto  
- `GET /v1/sipe/registros/{id}` — Consulta registro  

## Exemplos de Uso
```bash
curl -X POST http://localhost:8080/v1/sipe/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","matricula":"123","cracha":"ABC123","horaDiaria":8}'
```

## Relatórios
- `GET /v1/sipe/relatorios/{matricula}?inicio={data}&fim={data}` — Download de relatório em PDF

## Contribuição
Contribuições são bem-vindas! Abra issues e pull requests para melhorias.

## Licença
Este projeto não possui licença explícita.
