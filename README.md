# SIPE - Sistema Integrado de Ponto Eletrônico

API RESTful para controle de ponto eletrônico, gestão de registros de entrada e saída e geração de relatórios mensais de frequência. Integração com sistemas externos de controle de acesso e JSARH para automação do processo.

## Índice
- [Visão Geral](#visão-geral)
- [Recursos](#recursos)
- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Execução](#instalação-e-execução)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Endpoints da API](#endpoints-da-api)
- [Exemplos de Uso](#exemplos-de-uso)
- [Relatórios](#relatórios)
- [Banco de Dados e Migrações](#banco-de-dados-e-migrações)
- [Testes e Qualidade](#testes-e-qualidade)
- [Contribuição](#contribuição)

## Visão Geral
O SIPE fornece uma solução backend para gestão de ponto eletrônico, capturando registros de entrada e saída de funcionários, calculando horas de permanência, armazenando dados no banco de dados e gerando relatórios PDF mensais. Integra-se com:
- Sistema de Controle de Acesso (via Feign client, serviço externo).
- Sistema JSARH para obter informações de feriados, licenças e ausências.

## Segurança
Este serviço funciona como **OAuth2 Resource Server**. Todas as requisições devem incluir o header:

```http
Authorization: Bearer <token-jwt>
```

O token JWT deve ser emitido pelo Authorization Server em `http://localhost:9000`. As chaves públicas são obtidas em:

```bash
GET http://localhost:9000/oauth2/jwks
```

As authorities (roles) são extraídas do claim `authorities` no token e usadas para controle de acesso.

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
- Spring Boot 3.5.3
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
- Spring Cloud (BOM 2025.0.0)

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
   cd sipe-api
   ```
3. Defina o profile ativo e as variáveis de ambiente:
   ```bash
   export SIPE_API_PROFILE=dev   # ou prod
   export MYSQL_URL=jdbc:mysql://<host>:<porta>/<database>
   export MYSQL_USER=<usuario>
   export MYSQL_USER_PASSWORD=<senha>
   export COLETOR_URL=http://<url-coletor>
   export JSARH_URL=http://<url-jsarh>
   ```
4. Build e execução:
   ```bash
   mvn clean package
   java -jar target/sipe-0.3.9.jar
   ```
5. (Opcional) Modo desenvolvimento:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```
6. (Opcional) Docker:
   ```bash
   docker build -t sipe .
   docker run -p 8084:8084 --env-file .env sipe
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
Basepath: `http://<host>:8084`

### Arquivos
- `POST /v1/sipe/arquivos`  
  Parâmetros: `bytes` (MultipartFile), `nome` (String), `descricao` (String, opcional)
- `PATCH /v1/sipe/arquivos/{id}`  
  Parâmetros: mesmos do POST  
- `GET /v1/sipe/arquivos?pag={0}&tamanho={10}`  
  Lista metadados com paginação  
- `GET /v1/sipe/arquivos/{id}`  
  Recupera conteúdo do arquivo  
- `GET /v1/sipe/arquivos/{id}/metadata`  
  Recupera metadados do arquivo  
- `DELETE /v1/sipe/arquivos/{id}`  
  Remove o arquivo por ID

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
curl -X POST http://localhost:8084/v1/sipe/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","matricula":"123","cracha":"ABC123","horaDiaria":8}'
```

## Relatórios
- `GET /v1/sipe/relatorios/{matricula}?inicio={data}&fim={data}` — Download de relatório em PDF

## Banco de Dados e Migrações
- Banco de produção: MySQL 8+.
- Banco para testes: H2 (memória).
- Migrações com Flyway: scripts em `src/main/resources/db/migration`.

## Testes e Qualidade
- Testes: `spring-boot-starter-test` e `rest-assured` para testes de API.
- Banco em testes: H2.
- Cobertura: JaCoCo configurado no `pom.xml` (gera relatório no ciclo `package`).


## Contribuição
Pull requests são bem-vindos! Abra issues para sugestões e melhorias.

---
_Desenvolvido pela equipe NUTEC/SJRR
