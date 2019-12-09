package com.spring.crud.app.example.services.validators;

import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import com.spring.crud.app.example.services.exceptions.BusinessException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

/**
 * Test the {@link CountOrderItemsValidator}
 *
 * @author Mauricio Generoso
 */
public class CountOrderItemsValidatorTest {

    private CountOrderItemsValidator validator;

    @Before
    public void setup(){
        this.validator = new CountOrderItemsValidator();
    }

    @Test
    public void validate_shouldPass() {
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

    @Test(expected = BusinessException.class)
    public void validate_shouldThrowsWhenOrderItemsIsNull() {
        // Arrange
        Order order = new Order();
        order.setOrderItems(null);

        // Act
        validator.validate(order);
    }

    @Test(expected = BusinessException.class)
    public void validate_shouldThrowsWhenHasDuplicateOrderItem() {
        // Arrange
        UUID itemId = UUID.randomUUID();
        OrderItems orderItem1 = new OrderItems();
        orderItem1.setItemId(itemId);

        OrderItems orderItem2 = new OrderItems();
        orderItem2.setItemId(itemId);

        Order order = new Order();
        order.setOrderItems(Arrays.asList(orderItem1, orderItem2));

        // Act
        validator.validate(order);
    }
}