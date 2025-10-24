package br.uenf.eacos.controller.v1.implementation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.uenf.eacos.controller.v1.WebhookController;
import br.uenf.eacos.model.dto.VideoProcessDTO;
import br.uenf.eacos.service.WebhookService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IWebhookController implements WebhookController {

    private final WebhookService webhookService;

    @Override
    public ResponseEntity<?> save(VideoProcessDTO dto) {
        webhookService.saveVideoProcessedData(dto);
        return ResponseEntity.ok().build();
    }

}
