package com.spring.crud.lib.service.validators;

import com.spring.crud.lib.exception.DuplicateException;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.repository.ItemRepository;

import java.util.Optional;

/**
 * Validator to verify if an item has duplicated name
 *
 * @author Mauricio Generoso
 */
public class DuplicatedItemValidator implements Validator<Item, ItemRepository> {

    @Override
    public void validate(ItemRepository repository, Item entity) {
        Optional<Item> existingItem = repository.findByName(entity.getName());

        if (entity.isNew()) {
            if (existingItem.isPresent()) {
                throw new DuplicateException("Duplicated item with the same name");
            }
        } else {
            if (existingItem.isPresent() && !existingItem.get().getId().toString().equals(entity.getId().toString())) {
                throw new DuplicateException("Duplicated item with the same name");
            }
        }
    }
}
