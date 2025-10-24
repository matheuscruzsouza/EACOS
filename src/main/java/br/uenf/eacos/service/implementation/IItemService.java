package br.uenf.eacos.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.uenf.eacos.model.dto.ItemDTO;
import br.uenf.eacos.model.entity.eacos.Item;
import br.uenf.eacos.repository.ItemRepository;
import br.uenf.eacos.service.ItemService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IItemService implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Page<Item> findAll(Pageable pageable) {
        return this.itemRepository.findAll(pageable);
    }

    @Override
    public Item save(ItemDTO item) {
        Item t = Item.fromDTO(item);
        return this.itemRepository.saveAndFlush(t);
    }

    @Override
    public Item update(Long id, ItemDTO item) {
        Item t = this.itemRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Item entity not found")
        );

        t.updateFromDTO(item);

        return this.itemRepository.saveAndFlush(t);
    }
    
}
