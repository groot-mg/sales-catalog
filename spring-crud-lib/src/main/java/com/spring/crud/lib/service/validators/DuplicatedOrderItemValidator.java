package com.spring.crud.lib.service.validators;

import com.spring.crud.lib.exception.DuplicateException;
import com.spring.crud.lib.model.OrderItems;
import com.spring.crud.lib.repository.OrderItemsRepository;

import java.util.Optional;

/**
 * Validator to verify if a order item is duplicated
 *
 * @author Mauricio Generoso
 */
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