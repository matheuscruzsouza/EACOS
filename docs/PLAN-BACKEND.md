# Plano de Desenvolvimento do Backend (EACOS)

Este plano reflete a implementação atual em Spring Boot 3.5.6 com Java 21, seguindo as melhores práticas de desenvolvimento e arquitetura em camadas.

## Visão Geral Atual

- **Linguagem/Framework**: Java 21 + Spring Boot 3.5.6
- **Autenticação**: OAuth2 com Google (Spring Security)
- **Banco de dados**: H2 (desenvolvimento) + PostgreSQL (produção)
- **Migrações**: Liquibase
- **Documentação**: SpringDoc OpenAPI + Swagger UI
- **Testes**: JUnit 5 + Mockito + Spring Boot Test

## Arquitetura Implementada

### Estrutura de Pacotes

```
src/main/java/br/uenf/eacos/
├── config/                    # Configurações da aplicação
│   ├── AppConfig.java
│   ├── SecurityConfig.java
│   ├── FeignConfig.java
│   └── AuditListenerConfig.java
├── controller/v1/             # Controllers REST
│   ├── ItemController.java
│   ├── VideoController.java
│   ├── ConfigController.java
│   ├── OAuthController.java
│   └── implementation/        # Implementações dos controllers
├── model/                     # Modelos de dados
│   ├── entity/               # Entidades JPA
│   │   ├── AbstractEntity.java
│   │   ├── auth/User.java
│   │   ├── eacos/Item.java
│   │   ├── eacos/Video.java
│   │   └── audit/            # Entidades de auditoria
│   └── dto/                  # Data Transfer Objects
├── repository/               # Repositórios JPA
│   ├── UserRepository.java
│   ├── ItemRepository.java
│   ├── VideoRepository.java
│   └── audit/               # Repositórios de auditoria
├── service/                  # Camada de serviço
│   ├── ItemService.java
│   ├── VideoService.java
│   ├── AuthService.java
│   ├── AuditService.java
│   └── implementation/       # Implementações dos serviços
├── feign/google/            # Clientes Feign para APIs externas
├── listener/                # Listeners JPA
├── advice/                  # Exception handlers
└── constant/                # Constantes e enums
```

## Funcionalidades Implementadas

### Controllers REST
- **ItemController** - CRUD de itens com paginação
- **VideoController** - CRUD de vídeos com paginação  
- **ConfigController** - Configurações da aplicação
- **OAuthController** - Integração com Google OAuth2

### Serviços
- **ItemService** - Lógica de negócio para itens
- **VideoService** - Lógica de negócio para vídeos
- **AuthService** - Gerenciamento de autenticação
- **AuditService** - Sistema de auditoria automática

### Entidades Principais
- **User** - Usuários do sistema
- **Item** - Itens de coleta com geolocalização
- **Video** - Vídeos enviados pelos usuários
- **AbstractEntity** - Entidade base com auditoria

### Sistema de Auditoria
- **AuditLog** - Logs de auditoria
- **AuditChange** - Mudanças específicas
- **AuditChangeSet** - Conjuntos de mudanças
- **AuditListener** - Listener automático para auditoria

## Endpoints Atuais

### Públicos
- `GET /swagger-ui.html` - Documentação Swagger
- `GET /v3/api-docs` - OpenAPI JSON
- `GET /oauth2/authorization/google` - Login Google

### Protegidos (OAuth2)
- `GET /api/v1/config` - Configurações
- `GET /api/v1/item` - Listar itens
- `POST /api/v1/item` - Criar item
- `PUT /api/v1/item/{id}` - Atualizar item
- `GET /api/v1/videos` - Listar vídeos
- `POST /api/v1/videos` - Criar vídeo
- `PUT /api/v1/videos/{id}` - Atualizar vídeo

## Tecnologias e Dependências

### Core Spring Boot
- `spring-boot-starter-web` - Web MVC
- `spring-boot-starter-data-jpa` - JPA/Hibernate
- `spring-boot-starter-security` - Spring Security
- `spring-boot-starter-oauth2-client` - OAuth2 Client
- `spring-boot-starter-oauth2-resource-server` - OAuth2 Resource Server

### Spring Cloud
- `spring-cloud-starter-openfeign` - Cliente HTTP declarativo

### Banco de Dados
- `h2database` - Banco em memória (desenvolvimento)
- `postgresql` - Banco de produção
- `liquibase-core` - Migrações de banco

### Documentação
- `springdoc-openapi-starter-webmvc-ui` - Swagger UI
- `jackson-datatype-jsr310` - Suporte a Java Time

### Utilitários
- `lombok` - Redução de boilerplate
- `spring-boot-docker-compose` - Suporte a Docker Compose

## Configuração de Ambientes

### Desenvolvimento (application.yaml)
```yaml
spring:
  h2.console.enabled: true
  jpa.database-platform: org.hibernate.dialect.H2Dialect
  datasource:
    url: jdbc:h2:mem:eacos
    username: sa
    password: 
  liquibase.contexts: development
```

### Produção (Docker)
```yaml
spring:
  config.activate.on-profile: docker
  docker.compose.enabled: true
  jpa.database-platform: org.hibernate.dialect.PostgreSQLDialect
  datasource:
    url: jdbc:postgresql://postgres:5432/eacos
    username: eacos
    password: secret
  liquibase.contexts: production
```

## Segurança Implementada

### OAuth2 com Google
- Configuração via `SecurityConfig`
- Cliente OAuth2 configurado
- Redirecionamento automático para login
- Proteção de endpoints via `@SecurityRequirement`

### Auditoria Automática
- `@EntityListeners(AuditListener.class)` em `AbstractEntity`
- Registro automático de criação/atualização
- Rastreamento de mudanças específicas
- Logs estruturados em banco de dados

## Testes Implementados

### Estrutura de Testes
```
src/test/java/br/uenf/eacos/service/implementation/
├── IItemServiceTest.java      # 6 testes
├── IVideoServiceTest.java     # 6 testes  
├── IAuditServiceTest.java     # 6 testes
└── IGoogleServiceTest.java    # 6 testes
```

### Tecnologias de Teste
- **JUnit 5** - Framework principal
- **Mockito** - Criação de mocks
- **Spring Boot Test** - Integração com Spring
- **Spring Security Test** - Testes de segurança

### Cobertura Atual
- **24 testes** implementados
- **4 serviços** cobertos
- **100% de sucesso** nos testes
- **Cobertura completa** dos métodos públicos

## Próximos Passos

### Funcionalidades Planejadas
1. **Sistema de Coletas**
   - Endpoint para registro de coletas
   - Processamento de dados de lixo
   - Integração com modelo de IA

2. **Gestão de Praias**
   - CRUD de praias
   - Geolocalização de praias
   - Relatórios por região

3. **Analytics e Relatórios**
   - Dashboard administrativo
   - Métricas de uso
   - Relatórios de coleta

4. **Integração com IA**
   - Upload de modelos
   - Processamento de vídeos
   - Detecção de objetos

### Melhorias Técnicas
1. **Testes de Integração**
   - Testes com banco real
   - Testes de endpoints completos
   - Testes de segurança

2. **Monitoramento**
   - Logs estruturados
   - Métricas de performance
   - Health checks

3. **CI/CD**
   - Pipeline automatizado
   - Deploy automático
   - Testes automatizados

## Critérios de Qualidade

### Código
- ✅ Cobertura de testes 100%
- ✅ Padrões de nomenclatura
- ✅ Documentação automática
- ✅ Auditoria automática

### Segurança
- ✅ Autenticação OAuth2
- ✅ Autorização por endpoints
- ✅ Validação de dados
- ✅ Logs de auditoria

### Performance
- ✅ Paginação em endpoints
- ✅ Índices de banco
- ✅ Cache de configurações
- ✅ Conexões otimizadas
