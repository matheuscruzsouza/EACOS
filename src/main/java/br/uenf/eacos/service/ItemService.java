package br.uenf.eacos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.uenf.eacos.model.dto.ItemDTO;
import br.uenf.eacos.model.entity.eacos.Item;

public interface ItemService {

    Page<Item> findAll(Pageable pageable);

    Item save(ItemDTO item);

    Item update(Long id, ItemDTO item);

}
