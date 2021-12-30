package com.spring.crud.app.example.services;

import com.spring.crud.app.example.models.Item;
import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import com.spring.crud.app.example.repositories.OrderItemsRepository;
import com.spring.crud.app.example.services.exceptions.BusinessException;
import com.spring.crud.app.example.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test {@link OrderItemsService}
 *
 * @author Mauricio Generoso
 */
@SpringBootTest
class OrderItemsServiceTest {

    @Mock
    private OrderItemsRepository repository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private OrderItemsService service;

    @Test
    void findById_shouldReturnWhenExists() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        OrderItems orderItem = new OrderItems();

        doReturn(Optional.of(orderItem)).when(repository).findByOrderIdAndId(orderId, id);

        // Act
        OrderItems result = service.findById(orderId, id);

        // Assert
        assertNotNull(result);
        doReturn(Optional.of(orderItem)).when(repository).findByOrderIdAndId(orderId, id);
    }

    @Test
    void findById_shouldThrowsExceptionWhenNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        doReturn(Optional.empty()).when(repository).findById(id);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> service.findById(orderId, id));
    }

    @Test
    void save_shouldCallMethodToSave() {
        // Arrange
        Order order = new Order();

        UUID orderItemId = UUID.randomUUID();
        OrderItems orderItem = new OrderItems();
        orderItem.setItemId(orderItemId);

        doReturn(new Item()).when(itemService).findById(orderItemId);
        doReturn(orderItem).when(repository).save(orderItem);

        // Act
        service.save(order, orderItem);

        // Assert
        verify(repository, times(1)).save(orderItem);
    }

    @Test
    void delete_shouldAllowToDeleteWhenHasMoreThanOneOnOrderAndOrderIsOpen() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOpen(true);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);
        orderItem.setOrder(order);

        doReturn(2L).when(repository).countByOrderId(orderId);
        doNothing().when(repository).delete(orderItem);

        // Act
        service.delete(orderItem);

        // Assert
        verify(repository, times(1)).countByOrderId(orderId);
        verify(repository, times(1)).delete(orderItem);
    }

    @Test
    void delete_shouldThrowsExceptionWhenThereIsOnlyOneOrderItemOnOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);

        doReturn(1L).when(repository).countByOrderId(orderId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> service.delete(orderItem));
    }

    @Test
    void delete_shouldThrowsExceptionWhenOrderIsNotOpen() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setOpen(false);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);
        orderItem.setOrder(order);

        doReturn(2L).when(repository).countByOrderId(orderId);
        doNothing().when(repository).delete(orderItem);

        // Act & Assert
        assertThrows(BusinessException.class, () -> service.delete(orderItem));
    }
}
