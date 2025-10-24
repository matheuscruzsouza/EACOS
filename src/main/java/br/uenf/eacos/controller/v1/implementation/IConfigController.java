package br.uenf.eacos.controller.v1.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.uenf.eacos.controller.v1.ConfigController;
import br.uenf.eacos.model.dto.DestinyMailDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IConfigController implements ConfigController {

    @Value("${app.config.mail}")
    private String destinyMail;

    @Override
    public ResponseEntity<DestinyMailDTO> destinyMail() {
        return ResponseEntity.ok(
            DestinyMailDTO.builder()
                          .mail(this.destinyMail)
                          .build());
    }

}
