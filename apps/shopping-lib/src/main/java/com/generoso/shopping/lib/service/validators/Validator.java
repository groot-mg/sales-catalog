package com.generoso.shopping.lib.service.validators;

import com.generoso.shopping.lib.model.BasicEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface to validate business rules.
 *
 * @author Mauricio Generoso
 */
@FunctionalInterface
public interface Validator<E extends BasicEntity, R extends CrudRepository> {

    void validate(R repository, E entity);
}
