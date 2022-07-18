package com.generoso.shopping.lib.service.validators;

import com.generoso.shopping.lib.exception.DuplicateException;
import com.generoso.shopping.lib.model.OrderItems;
import com.generoso.shopping.lib.repository.OrderItemsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test {@link DuplicatedOrderItemValidator}
 *
 * @author Mauricio Generoso
 */
@ExtendWith(MockitoExtension.class)
class DuplicatedOrderItemValidatorTest {

    @Mock
    private OrderItemsRepository repository;

    private DuplicatedOrderItemValidator validator;

    @BeforeEach
    public void setup() {
        this.validator = new DuplicatedOrderItemValidator();
    }

    @Test
    void validate_shouldPassWhenIsNewAndThereIsNotOtherWithTheSameItemId() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);
        orderItem.setItemId(itemId);

        doReturn(Optional.empty()).when(repository).findByOrderIdAndItemId(orderId, itemId);

        // Act
        validator.validate(repository, orderItem);

        // Assert
        verify(repository, times(1)).findByOrderIdAndItemId(orderId, itemId);
    }

    @Test
    void validate_shouldThrowsExceptionWhenIsNewAndThereIsOtherWithTheSameItemId() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        OrderItems orderItem = new OrderItems();
        orderItem.setOrderId(orderId);
        orderItem.setItemId(itemId);

        doReturn(Optional.of(new OrderItems())).when(repository).findByOrderIdAndItemId(orderId, itemId);

        // Act & Assert
        assertThrows(DuplicateException.class, () -> validator.validate(repository, orderItem));
    }

    @Test
    void validate_shouldPassWhenIsNotNewAndThereIsNotOtherWithTheSameItemId() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        OrderItems orderItem = new OrderItems();
        orderItem.setId(UUID.randomUUID());
        orderItem.setOrderId(orderId);
        orderItem.setItemId(itemId);

        doReturn(Optional.empty()).when(repository).findByOrderIdAndItemId(orderId, itemId);

        // Act
        validator.validate(repository, orderItem);

        // Assert
        verify(repository, times(1)).findByOrderIdAndItemId(orderId, itemId);
    }

    @Test
    void validate_shouldThrowsExceptionWhenIsNotNewAndThereIsOtherWithTheSameItemId() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        OrderItems orderItem = new OrderItems();
        orderItem.setId(UUID.randomUUID());
        orderItem.setOrderId(orderId);
        orderItem.setItemId(itemId);

        OrderItems existingOrderItem = new OrderItems();
        existingOrderItem.setId(UUID.randomUUID());

        doReturn(Optional.of(existingOrderItem)).when(repository).findByOrderIdAndItemId(orderId, itemId);

        // Act
        assertThrows(DuplicateException.class, () -> validator.validate(repository, orderItem));
    }
}
