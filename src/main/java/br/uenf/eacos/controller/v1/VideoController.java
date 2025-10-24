package br.uenf.eacos.controller.v1;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.uenf.eacos.model.dto.VideoDTO;
import br.uenf.eacos.model.entity.eacos.Video;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(
    path="/api/v1/videos", 
    produces=MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Videos Controller", 
    description = "Operations related to videos management")
public interface VideoController {

    @GetMapping
    @Operation(
        summary = "List all videos", 
        description = "List all the videos uploaded to the platform", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Video>> findAll(
        @RequestParam(name = "page", required = false, defaultValue="0")  Integer page,
        @RequestParam(name = "size", required = false, defaultValue="10") Integer size
    );

    @PostMapping
    @Operation(
        summary = "Save a new video", 
        description = "Save a new video for processing", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Video> save(
        @RequestBody VideoDTO dto
    );

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing video", 
        description = "Update an existing video data", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Video> update(
        @PathVariable("id") Long id,
        @RequestBody VideoDTO dto
    );

}
