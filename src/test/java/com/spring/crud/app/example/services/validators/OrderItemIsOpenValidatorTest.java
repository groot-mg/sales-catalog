package com.spring.crud.app.example.services.validators;


import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import com.spring.crud.app.example.services.exceptions.BusinessException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test {@link OrderItemIsOpenValidator}
 *
 * @author Mauricio Generoso
 */
public class OrderItemIsOpenValidatorTest {

    private OrderItemIsOpenValidator validator;

    @Before
    public void setup() {
        this.validator = new OrderItemIsOpenValidator();
    }

    @Test
    public void validate_shouldPassWhenOrderIsOpen() {
        // Arrange
        Order order = new Order();
        order.setOpen(true);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);

        // Act
        validator.validate(orderItem);
    }

    @Test(expected = BusinessException.class)
    public void validate_shouldThrowsExceptionWhenOrderIsNotOpen() {
        // Arrange
        Order order = new Order();
        order.setOpen(false);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);

        // Act
        validator.validate(orderItem);
    }
}
