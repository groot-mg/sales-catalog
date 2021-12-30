package com.spring.crud.api.converter;

import com.spring.crud.api.dto.OrderItemsV1Dto;
import com.spring.crud.lib.model.OrderItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Data Converter to {@link OrderItems} object
 *
 * @author Mauricio Generoso
 */
@Service
public class OrderItemsV1DataConverter implements V1DataConverter<OrderItems, OrderItemsV1Dto> {

    private ItemV1DataConverter itemConverter;

    @Autowired
    public OrderItemsV1DataConverter(ItemV1DataConverter itemConverter) {
        this.itemConverter = itemConverter;
    }

    @Override
    public void convertToEntity(OrderItems entity, OrderItemsV1Dto dto) {
        entity.setAmount(dto.getAmount());
        entity.setItemId(dto.getItemId());
    }

    @Override
    public OrderItemsV1Dto convertToDto(OrderItems entity, Expand expand) {
        OrderItemsV1Dto dto = new OrderItemsV1Dto();
        dto.setId(entity.getId().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setAmount(entity.getAmount());
        dto.setItemId(entity.getItemId());

        if (expand != null && expand.contains("itemExpanded")) {
            dto.setItem(itemConverter.convertToDto(entity.getItem()));
        }
        return dto;
    }
}
