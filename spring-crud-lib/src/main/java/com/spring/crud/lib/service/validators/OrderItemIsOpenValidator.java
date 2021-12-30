package com.spring.crud.lib.service.validators;

import com.spring.crud.lib.exception.BusinessException;
import com.spring.crud.lib.model.Order;
import com.spring.crud.lib.model.OrderItems;

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
