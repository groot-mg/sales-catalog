package com.spring.crud.app.example.api.converters;

import com.spring.crud.app.example.models.BasicEntity;
import com.spring.crud.app.example.api.dtos.BasicV1Dto;

/**
 * Interface to convert a {@link BasicEntity} to {@link BasicV1Dto}, and a {@link BasicV1Dto} to
 * {@link BasicEntity}.
 * 
 * @author Mauricio Generoso
 *
 * @param <E> a subclass of {@link BasicEntity}
 * @param <D> a subclass of {@link BasicV1Dto}
 */
interface V1DataConverter<E extends BasicEntity, D extends BasicV1Dto> {

  void convertToEntity(E entity, D dto);

  D convertToDto(E entity, Expand expand);

  default D convertToDto(E entity) {
    return convertToDto(entity, null);
  }

}
