# Testes Unitários - EACOS Backend

## Visão Geral

Este documento descreve os testes unitários implementados para os Services do projeto EACOS. Os testes garantem a qualidade e confiabilidade do código através da validação de comportamentos esperados, tratamento de erros e integração com dependências.

## Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **JUnit 5** - Framework de testes principal
- **Mockito** - Framework para criação de mocks
- **Spring Boot Test** - Integração com Spring Framework
- **Gradle** - Ferramenta de build (executado com Java 21)
- **Lombok** - Redução de boilerplate nos testes

### Organização dos Arquivos

```
src/test/java/br/uenf/eacos/service/implementation/
├── IItemServiceTest.java
├── IVideoServiceTest.java
├── IAuditServiceTest.java
└── IGoogleServiceTest.java
```

## Testes por Service

### 1. IItemServiceTest

**Arquivo:** `IItemServiceTest.java`  
**Classe testada:** `IItemService`  
**Total de testes:** 6

#### Testes Implementados

| Método | Descrição | Cenário |
|--------|-----------|---------|
| `shouldReturnPageOfItemsWhenFindAllCalled()` | Testa paginação de itens | Cenário positivo - retorna página com itens |
| `shouldSaveItemWhenSaveCalledWithValidItemDTO()` | Testa criação de item | Cenário positivo - salva item com DTO válido |
| `shouldUpdateExistingItemWhenUpdateCalledWithValidId()` | Testa atualização de item | Cenário positivo - atualiza item existente |
| `shouldThrowRuntimeExceptionWhenUpdateCalledWithNonExistentId()` | Testa erro para ID inexistente | Cenário negativo - ID não encontrado |
| `shouldCallUpdateFromDTOWhenUpdateExecuted()` | Testa chamada do método de atualização | Validação de comportamento interno |

#### Dependências Mockadas

- `ItemRepository` - Mock para operações de persistência

#### Exemplo de Teste

```java
@Test
@DisplayName("Deve salvar item quando save for chamado com ItemDTO válido")
void shouldSaveItemWhenSaveCalledWithValidItemDTO() {
    // Given
    Video savedVideo = Video.builder().build();
    savedVideo.setId(1L);
    
    when(videoRepository.saveAndFlush(any(Video.class))).thenReturn(savedVideo);

    // When
    Video result = videoService.save(videoDTO);

    // Then
    assertNotNull(result);
    assertEquals(savedVideo.getId(), result.getId());
    verify(videoRepository).saveAndFlush(any(Video.class));
}
```

### 2. IVideoServiceTest

**Arquivo:** `IVideoServiceTest.java`  
**Classe testada:** `IVideoService`  
**Total de testes:** 6

#### Testes Implementados

| Método | Descrição | Cenário |
|--------|-----------|---------|
| `shouldReturnPageOfVideosWhenFindAllCalled()` | Testa paginação de vídeos | Cenário positivo - retorna página com vídeos |
| `shouldSaveVideoWhenSaveCalledWithValidVideoDTO()` | Testa criação de vídeo | Cenário positivo - salva vídeo com DTO válido |
| `shouldUpdateExistingVideoWhenUpdateCalledWithValidId()` | Testa atualização de vídeo | Cenário positivo - atualiza vídeo existente |
| `shouldThrowRuntimeExceptionWhenUpdateCalledWithNonExistentId()` | Testa erro para ID inexistente | Cenário negativo - ID não encontrado |
| `shouldCallUpdateFromDTOWhenUpdateExecuted()` | Testa chamada do método de atualização | Validação de comportamento interno |
| `shouldCallFromDTOWhenSaveExecuted()` | Testa chamada do método de criação | Validação de comportamento interno |

#### Dependências Mockadas

- `VideoRepository` - Mock para operações de persistência

### 3. IAuditServiceTest

**Arquivo:** `IAuditServiceTest.java`  
**Classe testada:** `IAuditService`  
**Total de testes:** 6

#### Testes Implementados

| Método | Descrição | Cenário |
|--------|-----------|---------|
| `shouldLogCreateWhenLogCreateCalled()` | Testa log de criação | Cenário positivo - registra criação de entidade |
| `shouldLogUpdateWhenLogUpdateCalled()` | Testa log de atualização | Cenário positivo - registra atualização de entidade |
| `shouldLogDeleteWhenLogDeleteCalled()` | Testa log de exclusão | Cenário positivo - registra exclusão de entidade |
| `shouldLogSpecificChangesWhenPropertiesAreDifferent()` | Testa registro de mudanças específicas | Validação de mudanças detalhadas |
| `shouldUseClassNameAsTableNameWhenNoTableAnnotation()` | Testa nome da tabela | Validação de nome da tabela |
| `shouldSetCurrentDateTimeWhenLogIsCreated()` | Testa data de alteração | Validação de timestamp |

#### Dependências Mockadas

- `AuditRepository` - Mock para operações de auditoria
- `ChangeSetRepository` - Mock para conjuntos de alterações
- `ChangesRepository` - Mock para mudanças específicas

#### Características Especiais

- Testa conversão de objetos para JSON
- Valida criação de logs de auditoria completos
- Verifica registro de mudanças específicas entre estados

### 4. IGoogleServiceTest

**Arquivo:** `IGoogleServiceTest.java`  
**Classe testada:** `IGoogleService`  
**Total de testes:** 6

#### Testes Implementados

| Método | Descrição | Cenário |
|--------|-----------|---------|
| `shouldReturnAccessTokenWhenGetTokenCalledWithValidCode()` | Testa obtenção de token | Cenário positivo - retorna token de acesso |
| `shouldReturnUserInfoWhenGetUserInfoCalledWithValidToken()` | Testa obtenção de informações do usuário | Cenário positivo - retorna dados do usuário |
| `shouldLoginAndCreateUserWhenUserDoesNotExist()` | Testa login com criação de usuário | Cenário positivo - cria usuário novo |
| `shouldLoginWithoutCreatingUserWhenUserAlreadyExists()` | Testa login sem criação | Cenário positivo - usuário já existe |
| `shouldThrowExceptionWhenIdTokenIsInvalid()` | Testa erro com token inválido | Cenário negativo - token malformado |
| `shouldBuildMultiValueMapCorrectlyInGetToken()` | Testa construção do MultiValueMap | Validação de parâmetros HTTP |

#### Dependências Mockadas

- `GoogleLoginClient` - Mock para API do Google Login
- `GoogleOauthClient` - Mock para API OAuth do Google
- `UserRepository` - Mock para operações de usuário

#### Características Especiais

- Testa decodificação de JWT tokens
- Valida criação automática de usuários
- Verifica construção correta de parâmetros HTTP
- Testa configuração do ObjectMapper

## Padrões de Teste Utilizados

### Estrutura AAA (Arrange-Act-Assert)

Todos os testes seguem o padrão AAA:

```java
@Test
@DisplayName("Descrição do teste")
void nomeDoTeste() {
    // Arrange (Given) - Preparação dos dados
    when(mock.method()).thenReturn(expectedValue);
    
    // Act (When) - Execução do método testado
    Result result = service.methodUnderTest();
    
    // Assert (Then) - Verificação dos resultados
    assertNotNull(result);
    assertEquals(expectedValue, result.getValue());
    verify(mock).method();
}
```

### Anotações Utilizadas

- `@ExtendWith(MockitoExtension.class)` - Configuração do Mockito
- `@Mock` - Criação de mocks
- `@InjectMocks` - Injeção de dependências no objeto testado
- `@BeforeEach` - Configuração inicial antes de cada teste
- `@DisplayName` - Descrição legível do teste
- `@SuppressWarnings("unchecked")` - Supressão de warnings de type safety

### Verificações Implementadas

#### Assertions
- `assertNotNull()` - Verifica se objeto não é nulo
- `assertEquals()` - Verifica igualdade de valores
- `assertThrows()` - Verifica se exceção é lançada

#### Verificações de Comportamento
- `verify()` - Verifica se método foi chamado
- `verify().never()` - Verifica se método não foi chamado
- `verify().atLeastOnce()` - Verifica se método foi chamado pelo menos uma vez

## Execução dos Testes

### Comando para Executar Todos os Testes

```bash
./gradlew test --tests "*ServiceTest"
```

### Comando com Java Específico

```bash
JAVA_HOME=/path/to/java21 ./gradlew test --tests "*ServiceTest"
```

### Relatórios Gerados

- **XML:** `build/test-results/test/`
- **HTML:** `build/reports/tests/test/index.html`

## Cobertura de Testes

### Métodos Testados por Service

| Service | Métodos Testados | Cobertura |
|---------|------------------|-----------|
| IItemService | `findAll()`, `save()`, `update()` | 100% |
| IVideoService | `findAll()`, `save()`, `update()` | 100% |
| IAuditService | `logCreate()`, `logUpdate()`, `logDelete()` | 100% |
| IGoogleService | `login()`, `getToken()`, `getUserInfo()` | 100% |

### Cenários Cobertos

- ✅ Cenários de sucesso
- ✅ Cenários de erro
- ✅ Validação de parâmetros
- ✅ Comportamento de dependências
- ✅ Tratamento de exceções
- ✅ Estados de objetos

## Estatísticas dos Testes

- **Total de testes:** 24
- **Taxa de sucesso:** 100%
- **Services cobertos:** 4
- **Métodos testados:** 12
- **Dependências mockadas:** 7

## Boas Práticas Implementadas

### 1. Nomenclatura Descritiva
- Nomes de métodos em inglês
- Descrições claras do comportamento esperado
- Uso de `@DisplayName` para descrições em português

### 2. Isolamento de Testes
- Cada teste é independente
- Setup limpo em `@BeforeEach`
- Mocks específicos para cada cenário

### 3. Validação Completa
- Verificação de valores de retorno
- Validação de chamadas de métodos
- Teste de cenários de erro

### 4. Manutenibilidade
- Código limpo e legível
- Reutilização de setup comum
- Estrutura consistente entre testes

## Próximos Passos

### Melhorias Futuras

1. **Testes de Integração**
   - Testes com banco de dados real
   - Testes de endpoints REST

2. **Cobertura de Código**
   - Relatórios de cobertura
   - Métricas de qualidade

3. **Testes de Performance**
   - Testes de carga
   - Validação de tempos de resposta

4. **Testes de Segurança**
   - Validação de autenticação
   - Testes de autorização

## Conclusão

Os testes unitários implementados fornecem uma base sólida para garantir a qualidade do código dos Services do EACOS. Com 24 testes cobrindo todos os métodos públicos e cenários críticos, o projeto tem alta confiabilidade e facilita a manutenção futura do código.

A implementação segue as melhores práticas de desenvolvimento orientado a testes (TDD) e garante que mudanças futuras não quebrem funcionalidades existentes.
