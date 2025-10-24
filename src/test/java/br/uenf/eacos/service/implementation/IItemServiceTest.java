package br.uenf.eacos.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.uenf.eacos.model.dto.ItemDTO;
import br.uenf.eacos.model.entity.eacos.Item;
import br.uenf.eacos.repository.eacos.ItemRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para IItemService")
class IItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private IItemService itemService;

    private Item item;
    private ItemDTO itemDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .protocoloId("test-protocol-123")
                .build();
        item.setId(1L);
        
        itemDTO = new ItemDTO("Test Item");
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve retornar página de itens quando findAll for chamado")
    void shouldReturnPageOfItemsWhenFindAllCalled() {
        // Given
        Page<Item> expectedPage = new PageImpl<>(java.util.List.of(item));
        when(itemRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Item> result = itemService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(item, result.getContent().get(0));
        verify(itemRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve salvar item quando save for chamado com ItemDTO válido")
    void shouldSaveItemWhenSaveCalledWithValidItemDTO() {
        // Given
        when(itemRepository.saveAndFlush(any(Item.class))).thenAnswer(invocation -> {
            Item itemToSave = invocation.getArgument(0);
            itemToSave.setId(1L); // Simula o ID gerado pelo banco
            return itemToSave;
        });

        // When
        Item result = itemService.save(itemDTO);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(itemRepository).saveAndFlush(any(Item.class));
    }

    @Test
    @DisplayName("Deve atualizar item existente quando update for chamado com ID válido")
    void shouldUpdateExistingItemWhenUpdateCalledWithValidId() {
        // Given
        Long itemId = 1L;
        Item existingItem = Item.builder()
                .protocoloId("existing-protocol")
                .build();
        existingItem.setId(itemId);
        
        Item updatedItem = Item.builder()
                .protocoloId("updated-protocol")
                .build();
        updatedItem.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(updatedItem);

        // When
        Item result = itemService.update(itemId, itemDTO);

        // Then
        assertNotNull(result);
        assertEquals(itemId, result.getId());
        verify(itemRepository).findById(itemId);
        verify(itemRepository).saveAndFlush(any(Item.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando update for chamado com ID inexistente")
    void shouldThrowRuntimeExceptionWhenUpdateCalledWithNonExistentId() {
        // Given
        Long nonExistentId = 999L;
        when(itemRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> itemService.update(nonExistentId, itemDTO));
        
        assertEquals("Item entity not found", exception.getMessage());
        verify(itemRepository).findById(nonExistentId);
        verify(itemRepository, never()).saveAndFlush(any(Item.class));
    }

    @Test
    @DisplayName("Deve chamar updateFromDTO quando update for executado")
    void shouldCallUpdateFromDTOWhenUpdateExecuted() {
        // Given
        Long itemId = 1L;
        Item existingItem = spy(Item.builder()
                .protocoloId("existing-protocol")
                .build());
        existingItem.setId(itemId);
        
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.saveAndFlush(any(Item.class))).thenReturn(existingItem);

        // When
        itemService.update(itemId, itemDTO);

        // Then
        verify(existingItem).updateFromDTO(itemDTO);
    }
}
