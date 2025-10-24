package br.uenf.eacos.model.dto.google;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfoDTO implements Serializable {

    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private Boolean email_verified;
    private String at_hash;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    private String accessToken;
    private String refreshToken;
    private Long iat;
    private Long exp;

}
