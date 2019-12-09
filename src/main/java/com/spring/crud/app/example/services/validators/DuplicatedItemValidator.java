package com.spring.crud.app.example.services.validators;

import com.spring.crud.app.example.models.Item;
import com.spring.crud.app.example.repositories.ItemRepository;
import com.spring.crud.app.example.services.exceptions.DuplicateException;

import java.util.Optional;

/**
 * Validator to verify if an item has duplicated name
 *
 * @author Mauricio Generoso
 */
public class DuplicatedItemValidator implements Validator<Item, ItemRepository> {

    @Override
    public void validate(ItemRepository repository, Item entity) {
        if (entity.isNew()) {
            Optional<Item> existingItem = repository.findByName(entity.getName());

            if (existingItem.isPresent()) {
                throw new DuplicateException("Duplicated item with the same name");
            }
        } else {
            Optional<Item> existingItem = repository.findByName(entity.getName());

            if (existingItem.isPresent() && !existingItem.get().getId().toString().equals(entity.getId().toString())) {
                throw new DuplicateException("Duplicated item with the same name");
            }
        }
    }
}
