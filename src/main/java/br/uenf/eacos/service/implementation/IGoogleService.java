package br.uenf.eacos.service.implementation;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.uenf.eacos.feign.google.GoogleLoginClient;
import br.uenf.eacos.feign.google.GoogleOauthClient;
import br.uenf.eacos.model.dto.google.GoogleAuthDTO;
import br.uenf.eacos.model.dto.google.GoogleUserInfoDTO;
import br.uenf.eacos.model.entity.auth.User;
import br.uenf.eacos.repository.UserRepository;
import br.uenf.eacos.service.AuthService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IGoogleService implements AuthService {

    @Value("${google.oauth2.client-id}")
    private String clientId;

    @Value("${google.oauth2.client-secret}")
    private String clientSecret;

    @Value("${google.oauth2.redirect-uri}")
    private String redirectUri;

    private final GoogleLoginClient googleClient;
    private final GoogleOauthClient googleOauthClient;

    private final ObjectMapper objectMapper;
    private final Base64.Decoder decoder = Base64.getDecoder();

    private final UserRepository userRepository;

    @Override
    public GoogleUserInfoDTO login(String code) throws Exception {
        GoogleAuthDTO auth = getToken(code);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        byte[] decodedBytes = decoder.decode(auth.getId_token().split("\\.")[1]);

        GoogleUserInfoDTO userInfo = objectMapper.readValue(decodedBytes, GoogleUserInfoDTO.class);

        userInfo.setAccessToken(auth.getId_token());
        userInfo.setRefreshToken(auth.getAccess_token());

        var userOpt = Optional.ofNullable(userRepository.findByIdentifier(userInfo.getSub()).orElse(null));

        if (userOpt.isEmpty()) {
            User user = new User();
            user.setIdentifier(userInfo.getSub());
            user.setName(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            userRepository.save(user);
        }

        return userInfo;
    }

    @Override
    public GoogleAuthDTO getToken(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", redirectUri);
        formData.add("access_type", "offline");

        return googleOauthClient.getAccessToken(formData);
    }

    public GoogleUserInfoDTO getUserInfo(String token) {
        return googleClient.getUserInfo("Bearer " + token);
    }

}
