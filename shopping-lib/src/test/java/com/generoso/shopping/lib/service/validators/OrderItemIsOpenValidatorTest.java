package com.generoso.shopping.lib.service.validators;


import com.generoso.shopping.lib.exception.BusinessException;
import com.generoso.shopping.lib.model.Order;
import com.generoso.shopping.lib.model.OrderItems;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test {@link OrderItemIsOpenValidator}
 *
 * @author Mauricio Generoso
 */
@ExtendWith(MockitoExtension.class)
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
