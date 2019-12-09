package com.spring.crud.app.example.services.validators;

import org.springframework.data.repository.CrudRepository;

import com.spring.crud.app.example.models.BasicEntity;

/**
 * Interface to validate business rules.
 * 
 * @author Mauricio Generoso
 */
public interface Validator<E extends BasicEntity, R extends CrudRepository> {

  void validate(R repository, E entity);
}
