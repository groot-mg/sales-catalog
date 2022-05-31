package com.generoso.shopping.api.converter;

import com.generoso.shopping.api.dto.BasicV1Dto;
import com.generoso.shopping.lib.model.BasicEntity;

/**
 * Interface to convert a {@link BasicEntity} to {@link BasicV1Dto}, and a {@link BasicV1Dto} to
 * {@link BasicEntity}.
 *
 * @param <E> a subclass of {@link BasicEntity}
 * @param <D> a subclass of {@link BasicV1Dto}
 * @author Mauricio Generoso
 */
interface V1DataConverter<E extends BasicEntity, D extends BasicV1Dto> {

    void convertToEntity(E entity, D dto);

    D convertToDto(E entity, Expand expand);

    default D convertToDto(E entity) {
        return convertToDto(entity, null);
    }

}
