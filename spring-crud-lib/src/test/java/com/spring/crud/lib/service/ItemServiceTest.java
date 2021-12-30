package com.spring.crud.lib.service;

import com.spring.crud.lib.exception.BusinessException;
import com.spring.crud.lib.exception.ResourceNotFoundException;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.repository.ItemRepository;
import com.spring.crud.lib.repository.OrderItemsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test {@link ItemService}
 *
 * @author Mauricio Generoso
 */
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @InjectMocks
    private ItemService service;

    @Test
    void findById_shouldReturnWhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Item item = new Item();

        doReturn(Optional.of(item)).when(repository).findById(id);

        // Act
        Item result = service.findById(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    void findById_shouldThrowsExceptionWhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        doReturn(Optional.empty()).when(repository).findById(id);

        // Act
        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void save_shouldCallMethodToSave() {
        // Arrange
        Item item = new Item();

        doReturn(item).when(repository).save(item);

        // Act
        service.save(item);

        // Assert
        verify(repository, times(1)).save(item);
    }

    @Test
    void deleteById_shouldDeleteWhenNotExistsOnAnyOrderItem() {
        // Arrange
        ItemService spyService = Mockito.spy(service);

        UUID id = UUID.randomUUID();
        Item item = new Item();

        doReturn(item).when(spyService).findById(id);
        doReturn(false).when(orderItemsRepository).existsByItemId(id);
        doNothing().when(spyService).delete(item);

        // Act
        spyService.deleteById(id);

        // Assert
        verify(spyService, times(1)).findById(id);
        verify(orderItemsRepository, times(1)).existsByItemId(id);
        verify(spyService, times(1)).delete(item);
    }

    @Test
    void deleteById_shouldThrowsExceptionWhenExistsOnAnyOrderItem() {
        // Arrange
        ItemService spyService = Mockito.spy(service);

        UUID id = UUID.randomUUID();

        doReturn(new Item()).when(spyService).findById(id);
        doReturn(true).when(orderItemsRepository).existsByItemId(id);

        // Act & Assert
        assertThrows(BusinessException.class, () -> spyService.deleteById(id));
    }
}