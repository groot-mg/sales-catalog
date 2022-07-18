package com.generoso.shopping.lib.service;

import com.generoso.shopping.lib.exception.BusinessException;
import com.generoso.shopping.lib.exception.ResourceNotFoundException;
import com.generoso.shopping.lib.model.Item;
import com.generoso.shopping.lib.model.Order;
import com.generoso.shopping.lib.model.TypeItem;
import com.generoso.shopping.lib.repository.OrderItemsRepository;
import com.generoso.shopping.lib.repository.OrderRepository;
import com.generoso.shopping.lib.service.validators.CountOrderItemsValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to manage and validate an {@link Order}
 *
 * @author Mauricio Generoso
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository repository;
    private final OrderItemsRepository orderItemsRepository;
    private final ItemService itemService;

    private final CountOrderItemsValidator countOrderItemsValidator;

    public Page<Order> findAll(Pageable pageable) {
        Page<Order> orderPage = repository.findAll(pageable);
        orderPage.getContent().forEach(this::updateTotalPreview);
        return orderPage;
    }

    public Order findById(UUID id) {
        Optional<Order> order = repository.findById(id);

        if (order.isPresent()) {
            return order.get();
        }

        throw new ResourceNotFoundException("Not found order with id " + id);
    }

    public Order customFindById(UUID id) {
        Order order = findById(id);
        updateTotalPreview(order);
        return order;
    }

    @Transactional(rollbackFor = Exception.class)
    public Order save(Order order) {
        boolean isNew = order.isNew();
        validate(order);
        repository.save(order);
        if (isNew) {
            saveOrderItems(order);
        }

        updateTotalPreview(order);
        return order;
    }

    @Transactional
    public void applyDiscount(Order order, int discount) {
        if (!order.isOpen()) {
            throw new BusinessException("It's not possible apply a discount to a closed order");
        }

        order.setDiscount(discount);
        save(order);
    }

    @Transactional
    public void close(Order order) {
        order.setOpen(false);
        save(order);
    }

    @Transactional
    public void delete(Order order) {
        repository.delete(order);
    }

    void updateTotalPreview(Order order) {
        double totalServices = getTotalProducts(order);
        double totalProducts = getTotalServices(order);
        double totalOrder = totalServices + totalProducts;

        if (order.getDiscount() > 0) {
            double discount = order.getDiscount() / 100.0;
            double totalPreview = totalOrder - totalProducts * discount;
            order.setTotalPreview(totalPreview);
        } else {
            order.setTotalPreview(totalOrder);
        }
    }

    private double getTotalProducts(Order order) {
        return order.getOrderItems().stream()
                .filter(orderItem -> TypeItem.PRODUCT.equals(orderItem.getItem().getType()))
                .mapToDouble(orderItem -> orderItem.getAmount() * orderItem.getItem().getPrice())
                .sum();
    }

    private double getTotalServices(Order order) {
        return order.getOrderItems().stream()
                .filter(orderItem -> TypeItem.SERVICE.equals(orderItem.getItem().getType()))
                .mapToDouble(orderItem -> orderItem.getAmount() * orderItem.getItem().getPrice())
                .sum();
    }

    private void validate(Order order) {
        countOrderItemsValidator.validate(order);
    }

    private void saveOrderItems(Order order) {
        order.getOrderItems().forEach(orderItem -> {
            Item item = itemService.findById(orderItem.getItemId());

            if (!item.isActive()) {
                throw new BusinessException("Disabled item cannot be add in a new order");
            }

            orderItem.setOrderId(order.getId());
            orderItemsRepository.save(orderItem);
            orderItem.setItem(item);
        });
    }
}