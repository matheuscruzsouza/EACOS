package br.uenf.eacos.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

import br.uenf.eacos.model.dto.google.GoogleAuthDTO;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@FeignClient(
    name = "googleLoginClient", 
    url = "https://oauth2.googleapis.com")
public interface GoogleOauthClient {

    @PostMapping(value="/token",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    GoogleAuthDTO getAccessToken(
        @RequestBody MultiValueMap<String, String> request
    );

}
