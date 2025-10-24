# Configurar Autenticação OAuth2 com Google (EACOS Backend)

Este guia descreve como configurar a autenticação OAuth2 com Google no backend Spring Boot, incluindo a configuração das credenciais e testes dos fluxos de autenticação.

## Pré-requisitos

- Projeto criado no Google Cloud Console: https://console.cloud.google.com
- Java 21 e Spring Boot 3.5.6
- Backend deste repositório com dependências instaladas

## Passo 1 — Configurar Projeto no Google Cloud Console

1. Acesse o [Google Cloud Console](https://console.cloud.google.com)
2. Crie um novo projeto ou selecione um existente
3. Ative a **Google+ API** e **Google OAuth2 API**
4. Vá em **APIs & Services > Credentials**
5. Clique em **Create Credentials > OAuth 2.0 Client IDs**

### Configuração do OAuth2 Client

**Application type:** Web application

**Authorized redirect URIs:**
```
http://localhost:8080/login/oauth2/code/google
https://seu-dominio.com/login/oauth2/code/google
```

**Authorized JavaScript origins:**
```
http://localhost:8080
https://seu-dominio.com
```

## Passo 2 — Configurar Credenciais no Backend

### Opção 1: Variáveis de Ambiente

```bash
# Credenciais OAuth2 do Google
GOOGLE_CLIENT_ID=seu-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=seu-client-secret

# URL de redirecionamento
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google
```

### Opção 2: application.yaml

```yaml
google:
  oauth2:
    client-id: seu-client-id.apps.googleusercontent.com
    client-secret: seu-client-secret
    redirect-uri: http://localhost:8080/login/oauth2/code/google
```

### Opção 3: application.properties

```properties
spring.security.oauth2.client.registration.google.client-id=seu-client-id.apps.googleusercontent.com
spring.security.oauth2.client.registration.google.client-secret=seu-client-secret
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google
```

## Passo 3 — Configuração do Spring Security

O projeto já possui a configuração OAuth2 implementada em `SecurityConfig.java`:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/oauth2/**", "/login/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google")
                .defaultSuccessUrl("/swagger-ui.html", true)
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.build();
    }
}
```

## Passo 4 — Executar o Backend

### Desenvolvimento

```bash
# Com credenciais via variáveis de ambiente
export GOOGLE_CLIENT_ID="seu-client-id.apps.googleusercontent.com"
export GOOGLE_CLIENT_SECRET="seu-client-secret"
./gradlew bootRun

# Ou com credenciais no application.yaml
./gradlew bootRun
```

### Produção

```bash
# Com Docker Compose
docker-compose up -d

# Ou build e execução direta
./gradlew build
java -jar build/libs/eacos-backend-0.0.1-SNAPSHOT.jar
```

## Passo 5 — Testar Autenticação

### 1. Acessar Login

Navegue para: `http://localhost:8080/oauth2/authorization/google`

### 2. Fluxo de Autenticação

1. Será redirecionado para Google
2. Faça login com sua conta Google
3. Autorize o acesso ao aplicativo
4. Será redirecionado de volta para o backend
5. Acesse endpoints protegidos com o token JWT

### 3. Testar Endpoints Protegidos

```bash
# Obter token de acesso (após login)
curl -X GET "http://localhost:8080/api/v1/config" \
     -H "Authorization: Bearer <seu-token-jwt>"
```

### 4. Usar Swagger UI

1. Acesse: `http://localhost:8080/swagger-ui.html`
2. Clique em **Authorize**
3. Insira o token JWT obtido
4. Teste os endpoints protegidos

## Configuração Avançada

### Personalizar URLs de Redirecionamento

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope: openid,profile,email
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://www.googleapis.com/oauth2/v2/userinfo
            user-name-attribute: id
```

### Configurar Scopes Adicionais

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            scope: openid,profile,email,https://www.googleapis.com/auth/drive.readonly
```

### Personalizar UserInfo

```java
@Component
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        
        // Personalizar dados do usuário
        Map<String, Object> attributes = oAuth2User.getAttributes();
        // Processar atributos conforme necessário
        
        return oAuth2User;
    }
}
```

## Troubleshooting

### Erros Comuns

**401 Unauthorized:**
- Verifique se as credenciais estão corretas
- Confirme se as URLs de redirecionamento estão configuradas
- Verifique se o projeto está ativo no Google Cloud Console

**403 Forbidden:**
- Verifique se as APIs necessárias estão habilitadas
- Confirme se o escopo solicitado está autorizado

**Redirect URI Mismatch:**
- Verifique se a URL de redirecionamento no Google Cloud Console
- Confirme se a URL no backend está correta

### Logs de Debug

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.security.oauth2: DEBUG
```

## Segurança

### Boas Práticas

1. **Nunca commite credenciais** no repositório
2. **Use variáveis de ambiente** em produção
3. **Configure HTTPS** em produção
4. **Rotacione credenciais** periodicamente
5. **Monitore logs** de autenticação

### Configuração de Produção

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            redirect-uri: https://seu-dominio.com/login/oauth2/code/google
```

## Integração com Frontend

### React/Angular/Vue

```javascript
// Exemplo de integração com frontend
const loginWithGoogle = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google';
};

// Após login, usar token para chamadas API
const apiCall = async () => {
  const response = await fetch('/api/v1/config', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  return response.json();
};
```

## Conclusão

A configuração OAuth2 com Google está pronta para uso. O sistema permite autenticação segura e integração com APIs do Google, mantendo a segurança e facilidade de uso para os desenvolvedores.
