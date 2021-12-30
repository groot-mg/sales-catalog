package com.spring.crud.lib.service;

import com.spring.crud.lib.exception.BusinessException;
import com.spring.crud.lib.exception.ResourceNotFoundException;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.model.Order;
import com.spring.crud.lib.model.OrderItems;
import com.spring.crud.lib.repository.OrderItemsRepository;
import com.spring.crud.lib.service.validators.DuplicatedOrderItemValidator;
import com.spring.crud.lib.service.validators.OrderItemIsOpenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to manage and validate an {@link OrderItems}
 *
 * @author Mauricio Generoso
 */
@Service
@Transactional(readOnly = true)
public class OrderItemsService {

    private OrderItemsRepository repository;
    private ItemService itemService;

    private OrderItemIsOpenValidator orderItemIsOpenValidator;
    private DuplicatedOrderItemValidator duplicatedOrderItemValidator;

    @Autowired
    public OrderItemsService(OrderItemsRepository repository, ItemService itemService) {
        this.repository = repository;
        this.itemService = itemService;

        this.orderItemIsOpenValidator = new OrderItemIsOpenValidator();
        this.duplicatedOrderItemValidator = new DuplicatedOrderItemValidator();
    }

    public Page<OrderItems> findAll(UUID orderId, Pageable pageable) {
        return repository.findAllByOrderId(orderId, pageable);
    }

    public OrderItems findById(UUID orderId, UUID id) {
        Optional<OrderItems> orderItem = repository.findByOrderIdAndId(orderId, id);

        if (orderItem.isPresent()) {
            return orderItem.get();
        }
        throw new ResourceNotFoundException("Not found order item id " + id);
    }

    @Transactional
    public void save(Order order, OrderItems orderItem) {
        Item item = itemService.findById(orderItem.getItemId());

        orderItem.setOrderId(order.getId());
        orderItem.setItemId(item.getId());
        orderItem.setOrder(order);
        orderItem.setItem(item);

        validate(orderItem);
        repository.save(orderItem);
    }

    @Transactional
    public void delete(OrderItems orderItem) {
        long amount = repository.countByOrderId(orderItem.getOrderId());

        if (amount == 1) {
            throw new BusinessException("Not allowed. It's necessary has one order item in a order");
        }

        orderItemIsOpenValidator.validate(orderItem);
        repository.delete(orderItem);
    }

    private void validate(OrderItems orderItem) {
        orderItemIsOpenValidator.validate(orderItem);
        duplicatedOrderItemValidator.validate(repository, orderItem);
    }
}
