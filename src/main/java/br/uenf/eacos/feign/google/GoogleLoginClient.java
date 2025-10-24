package br.uenf.eacos.feign.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import br.uenf.eacos.model.dto.google.GoogleUserInfoDTO;

@FeignClient(
    name = "googleLoginClient", 
    url = "https://openidconnect.googleapis.com/v1")
public interface GoogleLoginClient {

    @GetMapping("/userinfo")
    GoogleUserInfoDTO getUserInfo(
        @RequestHeader("Authorization") String accessToken
    );

}
