package br.uenf.eacos.controller.v1;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.uenf.eacos.model.dto.ItemDTO;
import br.uenf.eacos.model.entity.eacos.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(
    path="/api/v1/items", 
    produces=MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Item Controller", 
    description = "Operations related to item management")
public interface ItemController {

    @GetMapping
    @Operation(
        summary = "List all item", 
        description = "List all the item data uploaded to the platform", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Page<Item>> findAll(
        @RequestParam(name = "page", required = false, defaultValue="0")  Integer page,
        @RequestParam(name = "size", required = false, defaultValue="10") Integer size
    );

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing item", 
        description = "Update an existing item data", 
        security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Item> update(
        @PathVariable("id") Long id,
        @RequestBody ItemDTO dto
    );
}
