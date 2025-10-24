package br.uenf.eacos.controller.v1;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.uenf.eacos.model.dto.VideoProcessDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(
    path="/api/v1/webhooks", 
    produces=MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Webhooks Controller", 
    description = "Operations related to webhooks management")
public interface WebhookController {

    @PostMapping
    @Operation(
        summary = "Save a processed video data", 
        description = "Save a list of items for a processed video", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> save(
        @RequestBody VideoProcessDTO dto
    );

}
