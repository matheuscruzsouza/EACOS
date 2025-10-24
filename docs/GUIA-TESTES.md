# Guia Prático - Execução de Testes Unitários

## Tecnologias Utilizadas

- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **JUnit 5** - Framework de testes
- **Mockito** - Framework de mocking
- **Gradle** - Ferramenta de build

## Executando os Testes

### 1. Executar Todos os Testes de Service

```bash
# No diretório raiz do projeto
./gradlew test --tests "*ServiceTest"
```

### 2. Executar Teste de um Service Específico

```bash
# Teste apenas do IItemService
./gradlew test --tests "IItemServiceTest"

# Teste apenas do IAuditService
./gradlew test --tests "IAuditServiceTest"
```

### 3. Executar com Informações Detalhadas

```bash
# Com output detalhado
./gradlew test --tests "*ServiceTest" --info

# Com output de debug
./gradlew test --tests "*ServiceTest" --debug
```

### 4. Executar Testes Específicos

```bash
# Executar apenas um método de teste
./gradlew test --tests "IItemServiceTest.shouldSaveItemWhenSaveCalledWithValidItemDTO"
```

## Interpretando os Resultados

### Saída de Sucesso

```
> Task :test

BUILD SUCCESSFUL in 16s
4 actionable tasks: 2 executed, 2 up-to-date
```

### Saída com Falhas

```
> Task :test

Testes unitários para IItemService > Deve salvar item quando save for chamado FAILED
    Expected: 1L
    Actual: 2L
    -> at br.uenf.eacos.service.implementation.IItemServiceTest.shouldSaveItemWhenSaveCalledWithValidItemDTO(IItemServiceTest.java:85)

BUILD FAILED in 12s
```

## Relatórios de Teste

### Relatório HTML

Após a execução, um relatório HTML é gerado em:
```
build/reports/tests/test/index.html
```

Este relatório contém:
- Resumo geral dos testes
- Detalhes de cada teste executado
- Tempo de execução
- Stack traces de falhas

### Relatórios XML

Os resultados em XML ficam em:
```
build/test-results/test/
```

Útil para integração com ferramentas de CI/CD.

## Debugging de Testes

### 1. Adicionando Logs

```java
@Test
@DisplayName("Teste com logs")
void testeComLogs() {
    // Arrange
    System.out.println("Preparando dados de teste...");
    
    // Act
    Result result = service.methodUnderTest();
    System.out.println("Resultado obtido: " + result);
    
    // Assert
    assertNotNull(result);
}
```

### 2. Usando Debugger

Para debugar testes no IDE:

1. Coloque breakpoints no código
2. Execute o teste em modo debug
3. Use "Step Over", "Step Into" conforme necessário

### 3. Verificando Mocks

```java
@Test
void verificarComportamentoDoMock() {
    // Arrange
    when(mock.method()).thenReturn(expectedValue);
    
    // Act
    service.methodUnderTest();
    
    // Assert - Verificar se o mock foi chamado corretamente
    verify(mock).method();
    verify(mock, times(1)).method(); // Exatamente 1 vez
    verify(mock, never()).otherMethod(); // Nunca chamado
    verify(mock, atLeastOnce()).method(); // Pelo menos 1 vez
}
```

## Cenários Comuns de Problemas

### 1. Mock Não Funcionando

**Problema:** Mock não retorna o valor esperado

**Solução:**
```java
// Verificar se o mock está configurado corretamente
when(mock.method(any())).thenReturn(expectedValue);

// Verificar se está sendo chamado com os parâmetros corretos
verify(mock).method(eq(expectedParameter));
```

### 2. Assertion Falhando

**Problema:** Valores não são iguais como esperado

**Solução:**
```java
// Usar assertEquals com mensagem descritiva
assertEquals(expectedValue, actualValue, "Valor não corresponde ao esperado");

// Verificar se objetos são nulos
assertNotNull(actualValue, "Valor não deveria ser nulo");
```

### 3. Exceção Não Sendo Lançada

**Problema:** Teste espera exceção mas ela não é lançada

**Solução:**
```java
// Usar assertThrows corretamente
Exception exception = assertThrows(RuntimeException.class, () -> {
    service.methodThatShouldThrow();
});
assertEquals("Mensagem esperada", exception.getMessage());
```

## Boas Práticas para Manutenção

### 1. Nomes Descritivos

```java
// ❌ Ruim
@Test
void test1() { }

// ✅ Bom
@Test
@DisplayName("Deve retornar erro quando ID for inválido")
void shouldThrowExceptionWhenIdIsInvalid() { }
```

### 2. Setup Limpo

```java
@BeforeEach
void setUp() {
    // Configuração comum para todos os testes
    testData = createTestData();
    when(mock.getData()).thenReturn(testData);
}
```

### 3. Testes Independentes

```java
// Cada teste deve ser independente
@Test
void teste1() {
    // Não depende do resultado de outros testes
}

@Test
void teste2() {
    // Configuração própria
}
```

### 4. Validação Completa

```java
@Test
void testeCompleto() {
    // Arrange
    InputData input = createInput();
    ExpectedResult expected = createExpected();
    
    // Act
    ActualResult actual = service.process(input);
    
    // Assert - Validar todos os aspectos importantes
    assertNotNull(actual);
    assertEquals(expected.getValue(), actual.getValue());
    assertEquals(expected.getStatus(), actual.getStatus());
    verify(dependency).wasCalled();
}
```

## Integração com CI/CD

### GitHub Actions

```yaml
name: Testes Unitários

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 21
      uses: actions/setup-java@v4
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Run tests
      run: ./gradlew test --tests "*ServiceTest"
    
    - name: Upload test results
      uses: actions/upload-artifact@v4
      if: always()
      with:
        name: test-results
        path: build/test-results/
```

### Jenkins Pipeline

```groovy
pipeline {
    agent any
    
    stages {
        stage('Test') {
            steps {
                sh './gradlew test --tests "*ServiceTest"'
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'build/test-results/test/*.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'build/reports/tests/test',
                        reportFiles: 'index.html',
                        reportName: 'Relatório de Testes'
                    ])
                }
            }
        }
    }
}
```

## Métricas de Qualidade

### Cobertura de Código

Para gerar relatórios de cobertura:

```bash
./gradlew test jacocoTestReport
```

### Análise de Qualidade

```bash
# Executar análise estática
./gradlew check

# Executar todos os testes
./gradlew test
```

## Configuração do Ambiente

### Pré-requisitos

- **Java 21** instalado e configurado
- **Gradle** (wrapper incluído no projeto)
- **IDE** com suporte a Java (IntelliJ IDEA, Eclipse, VS Code)

### Verificar Versão do Java

```bash
java -version
# Deve mostrar: openjdk version "21.x.x"

./gradlew --version
# Deve mostrar: Gradle 8.x com Java 21
```

### Configuração do IDE

#### IntelliJ IDEA
1. File → Project Structure → Project → Project SDK: Java 21
2. File → Settings → Build → Gradle → Gradle JVM: Java 21

#### Eclipse
1. Window → Preferences → Java → Installed JREs → Add → Java 21
2. Window → Preferences → Gradle → Gradle Distribution → Use Gradle Wrapper

## Conclusão

Este guia prático fornece todas as informações necessárias para executar, interpretar e manter os testes unitários do projeto EACOS com Spring Boot 3.5.6 e Java 21. Seguindo essas práticas, a equipe pode garantir a qualidade contínua do código e facilitar a manutenção dos testes.
