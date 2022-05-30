package com.spring.crud.lib.service;

import com.spring.crud.lib.exception.BusinessException;
import com.spring.crud.lib.exception.ResourceNotFoundException;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.model.Order;
import com.spring.crud.lib.model.OrderItems;
import com.spring.crud.lib.model.TypeItem;
import com.spring.crud.lib.repository.OrderItemsRepository;
import com.spring.crud.lib.repository.OrderRepository;
import com.spring.crud.lib.service.validators.CountOrderItemsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test {@link OrderService}
 *
 * @author Mauricio Generoso
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @Mock
    private ItemService itemService;

    @Spy
    private CountOrderItemsValidator countOrderItemsValidator;

    @InjectMocks
    private OrderService service;

    @Test
    void findAll_shouldCallMethodToUpdateTotalPreview() {
        // Arrange
        OrderService spyService = Mockito.spy(service);
        Order order = new Order();

        Pageable pageable = PageRequest.of(0, 1);
        List<Order> orderList = Collections.singletonList(order);
        Page<Order> pageOrders = new PageImpl<>(orderList, pageable, orderList.size());

        doReturn(pageOrders).when(repository).findAll(pageable);
        doNothing().when(spyService).updateTotalPreview(order);

        // Act
        spyService.findAll(pageable);

        // Assert
        verify(repository, times(1)).findAll(pageable);
        verify(spyService, times(1)).updateTotalPreview(order);
    }

    @Test
    void findById_shouldReturnWhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        Order order = new Order();

        doReturn(Optional.of(order)).when(repository).findById(id);

        // Act
        Order result = service.findById(id);

        // Assert
        assertNotNull(result);
    }

    @Test
    void findById_shouldThrowsExceptionWhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();

        doReturn(Optional.empty()).when(repository).findById(id);

        // Act
        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
    }

    @Test
    void customFindById_shouldCallMethodToUpdateTotalPreview() {
        // Arrange
        OrderService spyService = Mockito.spy(service);
        UUID id = UUID.randomUUID();

        doReturn(new Order()).when(spyService).findById(id);
        doNothing().when(spyService).updateTotalPreview(any(Order.class));

        // Act
        spyService.customFindById(id);

        // Assert
        verify(spyService, times(1)).findById(id);
        verify(spyService, times(1)).updateTotalPreview(any(Order.class));
    }

    @Test
    void applyDiscount_shouldPassWhenOrderIsOpen() {
        // Arrange
        OrderService spyService = Mockito.spy(service);

        Order order = new Order();
        int discount = 10;

        doReturn(new Order()).when(spyService).save(order);

        // Act
        spyService.applyDiscount(order, discount);

        // Assert
        verify(spyService, times(1)).save(order);
    }

    @Test
    void applyDiscount_shouldThrowExceptionWhenOrderIsNotOpen() {
        // Arrange
        Order order = new Order();
        order.setOpen(false);
        int discount = 10;

        // Act
        assertThrows(BusinessException.class, () -> service.applyDiscount(order, discount));
    }

    @Test
    void updateTotalPreview_shouldNotApplyDiscountWhenThereIsNot() {
        // Arrange
        Order order = new Order();
        order.setDiscount(0);
        order.setTotalPreview(0);

        Item product = new Item();
        product.setType(TypeItem.PRODUCT);
        product.setPrice(100);

        Item serviceItem = new Item();
        serviceItem.setType(TypeItem.SERVICE);
        serviceItem.setPrice(100);

        OrderItems orderItemProduct = new OrderItems();
        orderItemProduct.setItem(product);
        orderItemProduct.setAmount(2);

        OrderItems orderItemService = new OrderItems();
        orderItemService.setItem(serviceItem);
        orderItemService.setAmount(2);

        order.setOrderItems(Arrays.asList(orderItemProduct, orderItemService));

        // Act
        service.updateTotalPreview(order);

        // Assert
        assertEquals(400, order.getTotalPreview());
    }

    @Test
    void updateTotalPreview_shouldApplyDiscountOnProducts() {
        // Arrange
        Order order = new Order();
        order.setDiscount(10);
        order.setTotalPreview(0);

        Item product = new Item();
        product.setType(TypeItem.PRODUCT);
        product.setPrice(100);

        Item serviceItem = new Item();
        serviceItem.setType(TypeItem.SERVICE);
        serviceItem.setPrice(100);

        OrderItems orderItemProduct = new OrderItems();
        orderItemProduct.setItem(product);
        orderItemProduct.setAmount(2);

        OrderItems orderItemService = new OrderItems();
        orderItemService.setItem(serviceItem);
        orderItemService.setAmount(2);

        order.setOrderItems(Arrays.asList(orderItemProduct, orderItemService));

        // Act
        service.updateTotalPreview(order);

        // Assert
        assertEquals(380, order.getTotalPreview());
    }
}