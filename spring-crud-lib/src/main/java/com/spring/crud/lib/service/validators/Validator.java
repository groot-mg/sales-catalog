package com.spring.crud.lib.service.validators;

import com.spring.crud.lib.model.BasicEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface to validate business rules.
 *
 * @author Mauricio Generoso
 */
public interface Validator<E extends BasicEntity, R extends CrudRepository> {

    void validate(R repository, E entity);
}
