# API EACOS Backend (v1)

Todos os endpoints são versionados em `/api/v1` e protegidos por autenticação OAuth2.

## Tecnologias Utilizadas

- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Autenticação e autorização
- **OAuth2** - Protocolo de autenticação
- **SpringDoc OpenAPI** - Documentação automática
- **Swagger UI** - Interface de documentação interativa

## Acesso à Documentação

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Autenticação

Todos os endpoints (exceto documentação) requerem autenticação OAuth2:

```
Authorization: Bearer <access_token>
```

## Endpoints Implementados

### Configuração

#### GET /api/v1/config

Retorna configurações da aplicação.

**Resposta 200:**
```json
{
  "email": "destino@example.com"
}
```

**Headers:**
- `Authorization: Bearer <token>` (obrigatório)

### Gestão de Itens

#### GET /api/v1/item

Lista todos os itens com paginação.

**Parâmetros de Query:**
- `page` (opcional, default=0) - Número da página
- `size` (opcional, default=10) - Tamanho da página

**Resposta 200:**
```json
{
  "content": [
    {
      "id": 1,
      "protocoloId": "uuid-string",
      "userId": { "id": 1, "name": "User Name" },
      "videoId": { "id": 1 },
      "timestamp": "2025-01-01T10:00:00Z",
      "latitude": -22.9,
      "longitude": -43.2,
      "status": "ACTIVE",
      "createdAt": "2025-01-01T10:00:00Z",
      "updatedAt": "2025-01-01T10:00:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### POST /api/v1/item

Cria um novo item.

**Corpo da Requisição:**
```json
{
  "name": "Nome do Item"
}
```

**Resposta 200:**
```json
{
  "id": 1,
  "protocoloId": "uuid-string",
  "userId": null,
  "videoId": null,
  "timestamp": null,
  "latitude": null,
  "longitude": null,
  "status": "ACTIVE",
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-01T10:00:00Z"
}
```

#### PUT /api/v1/item/{id}

Atualiza um item existente.

**Parâmetros:**
- `id` (path) - ID do item

**Corpo da Requisição:**
```json
{
  "name": "Nome Atualizado"
}
```

**Resposta 200:**
```json
{
  "id": 1,
  "protocoloId": "uuid-string",
  "userId": null,
  "videoId": null,
  "timestamp": null,
  "latitude": null,
  "longitude": null,
  "status": "ACTIVE",
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-01T10:05:00Z"
}
```

### Gestão de Vídeos

#### GET /api/v1/videos

Lista todos os vídeos com paginação.

**Parâmetros de Query:**
- `page` (opcional, default=0) - Número da página
- `size` (opcional, default=10) - Tamanho da página

**Resposta 200:**
```json
{
  "content": [
    {
      "id": 1,
      "uploadedBy": { "id": 1, "name": "User Name" },
      "createdAt": "2025-01-01T10:00:00Z",
      "updatedAt": "2025-01-01T10:00:00Z",
      "status": "ACTIVE"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### POST /api/v1/videos

Cria um novo vídeo.

**Corpo da Requisição:**
```json
{
  "name": "Nome do Vídeo"
}
```

**Resposta 200:**
```json
{
  "id": 1,
  "uploadedBy": null,
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-01T10:00:00Z",
  "status": "ACTIVE"
}
```

#### PUT /api/v1/videos/{id}

Atualiza um vídeo existente.

**Parâmetros:**
- `id` (path) - ID do vídeo

**Corpo da Requisição:**
```json
{
  "name": "Nome Atualizado"
}
```

**Resposta 200:**
```json
{
  "id": 1,
  "uploadedBy": null,
  "createdAt": "2025-01-01T10:00:00Z",
  "updatedAt": "2025-01-01T10:05:00Z",
  "status": "ACTIVE"
}
```

### OAuth2

#### GET /oauth2/authorization/google

Inicia o fluxo de autenticação com Google.

**Resposta:** Redirecionamento para Google OAuth

#### GET /oauth/login

Callback do Google OAuth (configurado automaticamente).

## Modelos de Dados

### Item
- `id` - Identificador único
- `protocoloId` - UUID único para protocolo
- `userId` - Referência ao usuário
- `videoId` - Referência ao vídeo
- `timestamp` - Data/hora do registro
- `latitude` - Coordenada de latitude
- `longitude` - Coordenada de longitude
- `status` - Status do registro (ACTIVE, INACTIVE)
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização

### Video
- `id` - Identificador único
- `uploadedBy` - Usuário que fez upload
- `createdAt` - Data de criação
- `updatedAt` - Data de atualização
- `status` - Status do vídeo (ACTIVE, INACTIVE)

### User
- `id` - Identificador único
- `name` - Nome do usuário
- `email` - Email do usuário
- `identifier` - Identificador externo (Google ID)

## Códigos de Erro

A API utiliza códigos de status HTTP padrão:

- `200` - Sucesso
- `201` - Criado com sucesso
- `400` - Requisição inválida
- `401` - Não autorizado
- `403` - Acesso negado
- `404` - Recurso não encontrado
- `500` - Erro interno do servidor

## Configuração do Ambiente

### Desenvolvimento
- **Banco**: H2 (memória)
- **Console H2**: `http://localhost:8080/h2-console`
- **Swagger**: `http://localhost:8080/swagger-ui.html`

### Produção
- **Banco**: PostgreSQL
- **Autenticação**: OAuth2 com Google
- **Migrações**: Liquibase

## Exemplos de Uso

### Autenticação com Google OAuth2

1. Acesse: `http://localhost:8080/oauth2/authorization/google`
2. Faça login com sua conta Google
3. Use o token retornado nas requisições:

```bash
curl -H "Authorization: Bearer <token>" \
     http://localhost:8080/api/v1/item
```

### Criar um novo item

```bash
curl -X POST \
     -H "Authorization: Bearer <token>" \
     -H "Content-Type: application/json" \
     -d '{"name": "Meu Item"}' \
     http://localhost:8080/api/v1/item
```
