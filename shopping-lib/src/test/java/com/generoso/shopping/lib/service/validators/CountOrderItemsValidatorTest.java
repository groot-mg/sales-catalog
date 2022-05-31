package com.generoso.shopping.lib.service.validators;

import com.generoso.shopping.lib.exception.BusinessException;
import com.generoso.shopping.lib.model.Order;
import com.generoso.shopping.lib.model.OrderItems;
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