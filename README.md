
![Logo](https://dev-to-uploads.s3.amazonaws.com/uploads/articles/th5xamgrr6se0x5ro4g6.png)

# Projeto EACOS (Educação Ambiental Costeira)

O Projeto EACOS é uma iniciativa ligada à Associação de Surf – Escolinha de Surf de Grussaí em Campos dos Goytacazes/RJ, que tem como objetivo promover a formação de jovens conscientes através do esporte e do fortalecimento do conhecimento sobre a costa marinha. Ele faz parte de um conjunto de ações que incluem projetos de conhecimento sobre a costa e de artesanato sustentável, com o apoio de secretarias de Educação e Turismo. 

## Principais características do Projeto EACOS:

### Foco na formação de jovens:
Busca desenvolver a consciência dos jovens por meio do esporte. 

### Conhecimento da costa:
Fortalece o aprendizado e a valorização da região costeira. 

## Parcerias:
Conta com o apoio da Associação de Surf, Escolinha de Surf de Grussaí, e envolve outras iniciativas. 

## Desenvolvimento comunitário:
Visa valorizar a cultura local e as comunidades, alinhando-se a projetos de sustentabilidade e educação. 

## Documentação

A documentação completa do projeto está disponível na pasta `docs/`:

- **[README.md](docs/README.md)** - Índice geral da documentação
- **[API.md](docs/API.md)** - Documentação da API REST
- **[PLAN-BACKEND.md](docs/PLAN-BACKEND.md)** - Plano de desenvolvimento
- **[SETUP-AUTH.md](docs/SETUP-AUTH.md)** - Configuração de autenticação
- **[TESTES-UNITARIOS.md](docs/TESTES-UNITARIOS.md)** - Documentação dos testes
- **[GUIA-TESTES.md](docs/GUIA-TESTES.md)** - Guia prático de testes

## Execução Rápida

```bash
# Executar a aplicação
./gradlew bootRun

# Executar testes
./gradlew test

# Acessar Swagger UI
http://localhost:8080/swagger-ui.html

# Acessar H2 Console (desenvolvimento)
http://localhost:8080/h2-console
```

## Stack utilizada

**Back-end:** 
- Java 21
- Spring Boot 3.5.6
- Spring Security (OAuth2)
- Spring Data JPA
- Spring Cloud OpenFeign
- Liquibase (migrações de banco)

**Database:** 
- H2 (desenvolvimento)
- PostgreSQL 17-alpine (produção)

**Documentação:**
- Swagger/OpenAPI 3
- SpringDoc

**Ferramentas:**
- Gradle (build)
- Lombok
- Docker Compose

## Autores

- [@octokatherine](https://www.github.com/octokatherine)

## Suporte

Para suporte, mande um email para fake@fake.com ou entre em nosso canal do Slack.

