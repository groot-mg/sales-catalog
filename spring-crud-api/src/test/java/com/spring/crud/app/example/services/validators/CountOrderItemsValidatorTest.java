package com.spring.crud.app.example.services.validators;

import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import com.spring.crud.app.example.services.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test the {@link CountOrderItemsValidator}
 *
 * @author Mauricio Generoso
 */
class CountOrderItemsValidatorTest {

    private CountOrderItemsValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new CountOrderItemsValidator();
    }

    @Test
    void validate_shouldPass() {
        // Arrange
        OrderItems orderItem1 = new OrderItems();
        orderItem1.setItemId(UUID.randomUUID());

        OrderItems orderItem2 = new OrderItems();
        orderItem2.setItemId(UUID.randomUUID());

        Order order = new Order();
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        // Act
        validator.validate(order);
    }

    @Test
    void validate_shouldThrowsWhenOrderItemsIsNull() {
        // Arrange
        Order order = new Order();
        order.setOrderItems(null);

        // Act & Assert
        assertThrows(BusinessException.class, () -> validator.validate(order));
    }

    @Test
    void validate_shouldThrowsWhenHasDuplicateOrderItem() {
        // Arrange
        UUID itemId = UUID.randomUUID();
        OrderItems orderItem1 = new OrderItems();
        orderItem1.setItemId(itemId);

        OrderItems orderItem2 = new OrderItems();
        orderItem2.setItemId(itemId);

        Order order = new Order();
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        // Act & Assert
        assertThrows(BusinessException.class, () -> validator.validate(order));
    }
}