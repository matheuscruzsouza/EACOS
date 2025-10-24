package br.uenf.eacos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.uenf.eacos.model.entity.eacos.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
}
