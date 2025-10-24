package br.uenf.eacos.controller.v1;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.uenf.eacos.model.dto.DestinyMailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(
    path="/api/v1/config", 
    produces=MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Configuration Controller", 
    description = "Operations related to management")
public interface ConfigController {

    @GetMapping
    @Operation(
        summary = "List destiny email", 
        description = "List all the viodos uploades to the platform", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<DestinyMailDTO> destinyMail();

}
