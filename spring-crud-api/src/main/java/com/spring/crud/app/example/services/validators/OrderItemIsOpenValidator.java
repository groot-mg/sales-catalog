package com.spring.crud.app.example.services.validators;

import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import com.spring.crud.app.example.services.exceptions.BusinessException;

/**
 * Validator to verify if a order item is open to allow edition
 *
 * @author Mauricio Generoso
 */
public class OrderItemIsOpenValidator {

    public void validate(OrderItems entity) {
        Order order = entity.getOrder();
        if (!order.isOpen()) {
            throw new BusinessException("Don't allow operation on closed order");
        }
    }
}
