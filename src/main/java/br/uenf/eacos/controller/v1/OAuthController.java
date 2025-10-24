package br.uenf.eacos.controller.v1;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(
    path="/oauth", 
    produces=MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "OAuth Controller", 
    description = "Operations related to authentication management")
public interface OAuthController {

    @GetMapping("/login")
    @Operation(
        summary = "Login with Google", 
        description = "Login with Google", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<?> login(
        @RequestParam(name = "code", required = false) String code
    ) throws Exception;

}
