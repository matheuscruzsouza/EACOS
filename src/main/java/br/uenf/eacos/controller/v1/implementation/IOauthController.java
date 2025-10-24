package br.uenf.eacos.controller.v1.implementation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.uenf.eacos.controller.v1.OAuthController;
import br.uenf.eacos.model.dto.google.GoogleUserInfoDTO;
import br.uenf.eacos.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IOauthController implements OAuthController {

    private final AuthService googleService;

    @Override
    public ResponseEntity<GoogleUserInfoDTO> login(String code) throws Exception {
        return ResponseEntity.ok(googleService.login(code));
    }

}
