package com.generoso.shopping.lib.service.validators;

import com.generoso.shopping.lib.exception.BusinessException;
import com.generoso.shopping.lib.model.Order;
import com.generoso.shopping.lib.model.OrderItems;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Validator to verify if an order has one item or more
 *
 * @author Mauricio Generoso
 */
@Component
public class CountOrderItemsValidator {

    public void validate(Order entity) {
        if (entity.isNew() && (entity.getOrderItems() == null || entity.getOrderItems().isEmpty())) {
            throw new BusinessException("It's necessary has one or more order item");
        }

        List<OrderItems> orderItems = entity.getOrderItems();
        Map<UUID, Long> countingByItemId = orderItems
                .stream().collect(Collectors.groupingBy(OrderItems::getItemId, Collectors.counting()));

        if (countingByItemId.values().stream().anyMatch(count -> count > 1)) {
            throw new BusinessException("There are items inserted more than one time");
        }
    }
}
