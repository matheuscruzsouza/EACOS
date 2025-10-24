package br.uenf.eacos.service;

import org.springframework.stereotype.Service;

import br.uenf.eacos.model.dto.google.GoogleAuthDTO;
import br.uenf.eacos.model.dto.google.GoogleUserInfoDTO;

@Service
public interface AuthService {

    GoogleUserInfoDTO login(String code) throws Exception;

    GoogleAuthDTO getToken(String code);

    GoogleUserInfoDTO getUserInfo(String accessToken);

}
