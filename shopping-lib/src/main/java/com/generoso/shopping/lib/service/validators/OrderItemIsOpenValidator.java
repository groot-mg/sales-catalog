package com.generoso.shopping.lib.service.validators;

import com.generoso.shopping.lib.exception.BusinessException;
import com.generoso.shopping.lib.model.Order;
import com.generoso.shopping.lib.model.OrderItems;
import org.springframework.stereotype.Component;

/**
 * Validator to verify if a order item is open to allow edition
 *
 * @author Mauricio Generoso
 */
@Component
public class OrderItemIsOpenValidator {

    public void validate(OrderItems entity) {
        Order order = entity.getOrder();
        if (!order.isOpen()) {
            throw new BusinessException("Don't allow operation on closed order");
        }
    }
}
