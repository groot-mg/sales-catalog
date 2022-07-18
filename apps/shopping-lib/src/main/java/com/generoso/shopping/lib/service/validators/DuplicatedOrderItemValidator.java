package com.generoso.shopping.lib.service.validators;

import com.generoso.shopping.lib.exception.DuplicateException;
import com.generoso.shopping.lib.model.OrderItems;
import com.generoso.shopping.lib.repository.OrderItemsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Validator to verify if a order item is duplicated
 *
 * @author Mauricio Generoso
 */
@Component
public class DuplicatedOrderItemValidator implements Validator<OrderItems, OrderItemsRepository> {

    @Override
    public void validate(OrderItemsRepository repository, OrderItems entity) {
        Optional<OrderItems> orderItem =
                repository.findByOrderIdAndItemId(entity.getOrderId(), entity.getItemId());

        if (entity.isNew() && orderItem.isPresent()) {
            throw new DuplicateException(
                    "Duplicated item with id " + entity.getItemId() + " in order with id " + entity.getOrderId());
        }

        if (!entity.isNew() && orderItem.isPresent() && orderItem.get().getId() != entity.getId()) {
            throw new DuplicateException(
                    "Duplicated item with id " + entity.getItemId() + " in order with id " + entity.getOrderId());
        }
    }
}
