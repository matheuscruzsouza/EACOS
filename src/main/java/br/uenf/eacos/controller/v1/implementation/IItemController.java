package br.uenf.eacos.controller.v1.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.uenf.eacos.controller.v1.ItemController;
import br.uenf.eacos.model.dto.ItemDTO;
import br.uenf.eacos.model.entity.eacos.Item;
import br.uenf.eacos.service.ItemService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IItemController implements ItemController {

    private final ItemService itemService;

    @Override
    public ResponseEntity<Page<Item>> findAll(Integer page, Integer size) {
        return ResponseEntity.ok(
            this.itemService.findAll(
                PageRequest.of(page, size)));
    }

    @Override
    public ResponseEntity<Item> update(Long id, ItemDTO dto) {
        return ResponseEntity.ok(
            this.itemService.update(id, dto));
    }
    
}
