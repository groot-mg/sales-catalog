package com.spring.crud.app.example.api.converters;

import com.spring.crud.app.example.models.Item;
import com.spring.crud.app.example.api.dtos.ItemV1Dto;
import org.springframework.stereotype.Service;

/**
 * Data Converter to {@link Item} object
 *
 * @author Mauricio Generoso
 */
@Service
public class ItemV1DataConverter implements V1DataConverter<Item, ItemV1Dto> {

    @Override
    public void convertToEntity(Item entity, ItemV1Dto dto) {
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setPrice(dto.getPrice());
    }

    @Override
    public ItemV1Dto convertToDto(Item entity, Expand expand) {
        ItemV1Dto dto = new ItemV1Dto();
        dto.setId(entity.getId().toString());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setPrice(entity.getPrice());
        dto.setActive(entity.isActive());
        return dto;
    }
}