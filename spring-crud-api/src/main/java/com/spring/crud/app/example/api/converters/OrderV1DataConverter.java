package com.spring.crud.app.example.api.converters;

import com.spring.crud.app.example.api.dtos.OrderItemsV1Dto;
import com.spring.crud.app.example.api.dtos.OrderV1Dto;
import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.models.OrderItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data Converter to {@link Order} object
 *
 * @author Mauricio Generoso
 */
@Service
public class OrderV1DataConverter implements V1DataConverter<Order, OrderV1Dto> {

    private OrderItemsV1DataConverter orderItemsConverter;

    @Autowired
    public OrderV1DataConverter(OrderItemsV1DataConverter orderItemsConverter) {
        this.orderItemsConverter = orderItemsConverter;
    }

    @Override
    public void convertToEntity(Order entity, OrderV1Dto dto) {
        if (Objects.nonNull(dto.getOrderItems())) {
            List<OrderItems> orderItemsList = new ArrayList<>();
            dto.getOrderItems().forEach(orderItemsV1Dto -> {
                OrderItems orderItems = new OrderItems();
                orderItemsConverter.convertToEntity(orderItems, orderItemsV1Dto);
                orderItemsList.add(orderItems);
            });
            entity.setOrderItems(orderItemsList);
        }
    }

    @Override
    public OrderV1Dto convertToDto(Order entity, Expand expand) {
        OrderV1Dto dto = new OrderV1Dto();
        dto.setId(entity.getId().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setOpen(entity.isOpen());
        dto.setClosedAtDateTime(entity.getClosedAtDateTime());
        dto.setDiscount(entity.getDiscount());
        dto.setTotalPreview(entity.getTotalPreview());

        if (expand != null && expand.contains("orderItemsExpanded")) {
            List<OrderItemsV1Dto> orderItemsV1Dtos = new ArrayList<>();
            entity.getOrderItems().forEach(orderItem ->
                    orderItemsV1Dtos.add(orderItemsConverter.convertToDto(orderItem, new Expand("itemExpanded"))));
            dto.setOrderItems(orderItemsV1Dtos);
        }

        return dto;
    }
}
