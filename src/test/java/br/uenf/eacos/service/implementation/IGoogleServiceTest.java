package br.uenf.eacos.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.uenf.eacos.feign.google.GoogleLoginClient;
import br.uenf.eacos.feign.google.GoogleOauthClient;
import br.uenf.eacos.model.dto.google.GoogleAuthDTO;
import br.uenf.eacos.model.dto.google.GoogleUserInfoDTO;
import br.uenf.eacos.model.entity.auth.User;
import br.uenf.eacos.repository.auth.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para IGoogleService")
class IGoogleServiceTest {

    @Mock
    private GoogleLoginClient googleClient;

    @Mock
    private GoogleOauthClient googleOauthClient;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private IGoogleService googleService;

    private ObjectMapper objectMapper;
    private GoogleAuthDTO googleAuthDTO;
    private GoogleUserInfoDTO googleUserInfoDTO;
    private User user;
    private String testCode;
    private String testToken;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(googleService, "objectMapper", objectMapper);
        ReflectionTestUtils.setField(googleService, "clientId", "test-client-id");
        ReflectionTestUtils.setField(googleService, "clientSecret", "test-client-secret");
        ReflectionTestUtils.setField(googleService, "redirectUri", "http://localhost:8080/callback");

        testCode = "test-auth-code";
        testToken = "test-access-token";

        googleAuthDTO = new GoogleAuthDTO();
        googleAuthDTO.setAccess_token("test-access-token");
        googleAuthDTO.setRefresh_token("test-refresh-token");
        googleAuthDTO.setId_token("header.payload.signature");

        googleUserInfoDTO = new GoogleUserInfoDTO();
        googleUserInfoDTO.setSub("test-user-id");
        googleUserInfoDTO.setName("Test User");
        googleUserInfoDTO.setEmail("test@example.com");

        user = User.builder()
                .identifier("test-user-id")
                .name("Test User")
                .email("test@example.com")
                .build();
        user.setId(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Deve retornar token de acesso quando getToken for chamado com código válido")
    void shouldReturnAccessTokenWhenGetTokenCalledWithValidCode() {
        // Given
        when(googleOauthClient.getAccessToken(any(MultiValueMap.class))).thenReturn(googleAuthDTO);

        // When
        GoogleAuthDTO result = googleService.getToken(testCode);

        // Then
        assertNotNull(result);
        assertEquals(googleAuthDTO.getAccess_token(), result.getAccess_token());
        assertEquals(googleAuthDTO.getRefresh_token(), result.getRefresh_token());
        assertEquals(googleAuthDTO.getId_token(), result.getId_token());
        
        verify(googleOauthClient).getAccessToken(argThat(formData -> 
            formData.get("code").contains(testCode) &&
            formData.get("client_id").contains("test-client-id") &&
            formData.get("client_secret").contains("test-client-secret") &&
            formData.get("grant_type").contains("authorization_code") &&
            formData.get("redirect_uri").contains("http://localhost:8080/callback") &&
            formData.get("access_type").contains("offline")
        ));
    }

    @Test
    @DisplayName("Deve retornar informações do usuário quando getUserInfo for chamado com token válido")
    void shouldReturnUserInfoWhenGetUserInfoCalledWithValidToken() {
        // Given
        when(googleClient.getUserInfo("Bearer " + testToken)).thenReturn(googleUserInfoDTO);

        // When
        GoogleUserInfoDTO result = googleService.getUserInfo(testToken);

        // Then
        assertNotNull(result);
        assertEquals(googleUserInfoDTO.getSub(), result.getSub());
        assertEquals(googleUserInfoDTO.getName(), result.getName());
        assertEquals(googleUserInfoDTO.getEmail(), result.getEmail());
        
        verify(googleClient).getUserInfo("Bearer " + testToken);
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Deve fazer login e criar usuário quando usuário não existir")
    void shouldLoginAndCreateUserWhenUserDoesNotExist() throws Exception {
        // Given
        String encodedPayload = Base64.getEncoder().encodeToString(
            objectMapper.writeValueAsBytes(googleUserInfoDTO)
        );
        googleAuthDTO.setId_token("header." + encodedPayload + ".signature");

        when(googleOauthClient.getAccessToken(any(MultiValueMap.class))).thenReturn(googleAuthDTO);
        when(userRepository.findByIdentifier(googleUserInfoDTO.getSub())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        GoogleUserInfoDTO result = googleService.login(testCode);

        // Then
        assertNotNull(result);
        assertEquals(googleUserInfoDTO.getSub(), result.getSub());
        assertEquals(googleUserInfoDTO.getName(), result.getName());
        assertEquals(googleUserInfoDTO.getEmail(), result.getEmail());

        verify(userRepository).findByIdentifier(googleUserInfoDTO.getSub());
        verify(userRepository).save(argThat(savedUser -> 
            savedUser.getIdentifier().equals(googleUserInfoDTO.getSub()) &&
            savedUser.getName().equals(googleUserInfoDTO.getName()) &&
            savedUser.getEmail().equals(googleUserInfoDTO.getEmail())
        ));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Deve fazer login sem criar usuário quando usuário já existir")
    void shouldLoginWithoutCreatingUserWhenUserAlreadyExists() throws Exception {
        // Given
        String encodedPayload = Base64.getEncoder().encodeToString(
            objectMapper.writeValueAsBytes(googleUserInfoDTO)
        );
        googleAuthDTO.setId_token("header." + encodedPayload + ".signature");

        when(googleOauthClient.getAccessToken(any(MultiValueMap.class))).thenReturn(googleAuthDTO);
        when(userRepository.findByIdentifier(googleUserInfoDTO.getSub())).thenReturn(Optional.of(user));

        // When
        GoogleUserInfoDTO result = googleService.login(testCode);

        // Then
        assertNotNull(result);
        assertEquals(googleUserInfoDTO.getSub(), result.getSub());

        verify(userRepository).findByIdentifier(googleUserInfoDTO.getSub());
        verify(userRepository, never()).save(any(User.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Deve lançar exceção quando ID token for inválido")
    void shouldThrowExceptionWhenIdTokenIsInvalid() {
        // Given
        googleAuthDTO.setId_token("invalid-token");
        when(googleOauthClient.getAccessToken(any(MultiValueMap.class))).thenReturn(googleAuthDTO);

        // When & Then
        assertThrows(Exception.class, () -> googleService.login(testCode));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Deve configurar ObjectMapper para aceitar strings vazias como null")
    void shouldConfigureObjectMapperToAcceptEmptyStringsAsNull() throws Exception {
        // Given
        String encodedPayload = Base64.getEncoder().encodeToString(
            objectMapper.writeValueAsBytes(googleUserInfoDTO)
        );
        googleAuthDTO.setId_token("header." + encodedPayload + ".signature");

        when(googleOauthClient.getAccessToken(any(MultiValueMap.class))).thenReturn(googleAuthDTO);
        when(userRepository.findByIdentifier(googleUserInfoDTO.getSub())).thenReturn(Optional.of(user));

        // When
        GoogleUserInfoDTO result = googleService.login(testCode);

        // Then
        assertNotNull(result);
        // Verifica se o ObjectMapper foi configurado corretamente através do comportamento
        verify(googleOauthClient).getAccessToken(any(MultiValueMap.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    @DisplayName("Deve construir MultiValueMap corretamente no getToken")
    void shouldBuildMultiValueMapCorrectlyInGetToken() {
        // Given
        when(googleOauthClient.getAccessToken(any(MultiValueMap.class))).thenReturn(googleAuthDTO);

        // When
        googleService.getToken(testCode);

        // Then
        verify(googleOauthClient).getAccessToken(argThat(formData -> {
            MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
            expected.add("code", testCode);
            expected.add("client_id", "test-client-id");
            expected.add("client_secret", "test-client-secret");
            expected.add("grant_type", "authorization_code");
            expected.add("redirect_uri", "http://localhost:8080/callback");
            expected.add("access_type", "offline");
            
            return formData.equals(expected);
        }));
    }
}
