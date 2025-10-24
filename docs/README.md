# Documentação - EACOS Backend

## Índice de Documentação

### 📋 Documentação Geral

- **[API.md](./API.md)** - Documentação da API REST
- **[PLAN-BACKEND.md](./PLAN-BACKEND.md)** - Plano de desenvolvimento do backend
- **[SETUP-AUTH.md](./SETUP-AUTH.md)** - Configuração de autenticação

### 🧪 Documentação de Testes

- **[TESTES-UNITARIOS.md](./TESTES-UNITARIOS.md)** - Documentação completa dos testes unitários
- **[GUIA-TESTES.md](./GUIA-TESTES.md)** - Guia prático para execução e manutenção de testes

## Visão Geral dos Testes

### Estatísticas dos Testes Unitários

| Métrica | Valor |
|---------|-------|
| **Total de Testes** | 24 |
| **Services Testados** | 4 |
| **Taxa de Sucesso** | 100% |
| **Cobertura de Métodos** | 100% |

### Services com Testes Implementados

1. **IItemService** - 6 testes
   - Operações CRUD para itens
   - Validação de paginação
   - Tratamento de erros

2. **IVideoService** - 6 testes
   - Operações CRUD para vídeos
   - Validação de paginação
   - Tratamento de erros

3. **IAuditService** - 6 testes
   - Logs de auditoria
   - Rastreamento de mudanças
   - Persistência de logs

4. **IGoogleService** - 6 testes
   - Autenticação OAuth
   - Integração com Google APIs
   - Gerenciamento de usuários

## Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Autenticação e autorização
- **OAuth2** - Protocolo de autenticação
- **Spring Data JPA** - Persistência de dados
- **H2/PostgreSQL** - Banco de dados
- **Liquibase** - Migrações de banco
- **SpringDoc OpenAPI** - Documentação automática
- **JUnit 5** - Framework de testes
- **Mockito** - Framework de mocking
- **Gradle** - Ferramenta de build

## Execução Rápida

```bash
# Executar a aplicação
./gradlew bootRun

# Executar todos os testes de service
./gradlew test --tests "*ServiceTest"

# Executar com Java específico
JAVA_HOME=/path/to/java21 ./gradlew test --tests "*ServiceTest"

# Acessar Swagger UI
http://localhost:8080/swagger-ui.html

# Acessar H2 Console (desenvolvimento)
http://localhost:8080/h2-console
```

## Estrutura dos Arquivos

```
docs/
├── API.md                    # Documentação da API
├── PLAN-BACKEND.md          # Plano do backend
├── SETUP-AUTH.md            # Setup de autenticação
├── TESTES-UNITARIOS.md      # Documentação dos testes
├── GUIA-TESTES.md           # Guia prático de testes
└── README.md                # Este arquivo

src/test/java/br/uenf/eacos/service/implementation/
├── IItemServiceTest.java    # Testes do ItemService
├── IVideoServiceTest.java   # Testes do VideoService
├── IAuditServiceTest.java   # Testes do AuditService
└── IGoogleServiceTest.java  # Testes do GoogleService
```

## Próximos Passos

### Melhorias Planejadas

1. **Testes de Integração**
   - Testes com banco de dados real
   - Testes de endpoints REST completos

2. **Cobertura de Código**
   - Relatórios de cobertura com JaCoCo
   - Métricas de qualidade de código

3. **Testes de Performance**
   - Testes de carga
   - Validação de tempos de resposta

4. **CI/CD**
   - Integração com GitHub Actions
   - Pipeline de testes automatizados

## Contribuição

Para contribuir com a documentação:

1. Mantenha a consistência de estilo
2. Atualize este README quando adicionar novos documentos
3. Use exemplos práticos sempre que possível
4. Mantenha a documentação atualizada com o código

## Contato

Para dúvidas sobre a documentação ou testes, consulte:
- Documentação técnica: `TESTES-UNITARIOS.md`
- Guia prático: `GUIA-TESTES.md`
- Configuração: `SETUP-AUTH.md`
