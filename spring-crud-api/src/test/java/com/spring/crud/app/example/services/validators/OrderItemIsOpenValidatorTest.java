package com.spring.crud.app.example.services.validators;


import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import com.spring.crud.app.example.services.exceptions.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test {@link OrderItemIsOpenValidator}
 *
 * @author Mauricio Generoso
 */
@SpringBootTest
class OrderItemIsOpenValidatorTest {

    private OrderItemIsOpenValidator validator;

    @BeforeEach
    void setup() {
        this.validator = new OrderItemIsOpenValidator();
    }

    @Test
    void validate_shouldPassWhenOrderIsOpen() {
        // Arrange
        Order order = new Order();
        order.setOpen(true);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);

        // Act
        validator.validate(orderItem);
    }

    @Test
    void validate_shouldThrowsExceptionWhenOrderIsNotOpen() {
        // Arrange
        Order order = new Order();
        order.setOpen(false);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);

        // Act
        assertThrows(BusinessException.class, () -> validator.validate(orderItem));
    }
}
