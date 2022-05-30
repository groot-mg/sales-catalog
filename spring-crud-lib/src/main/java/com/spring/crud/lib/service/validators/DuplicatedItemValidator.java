package com.spring.crud.lib.service.validators;

import com.spring.crud.lib.exception.DuplicateException;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.repository.ItemRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Validator to verify if an item has duplicated name
 *
 * @author Mauricio Generoso
 */
@Component
public class DuplicatedItemValidator implements Validator<Item, ItemRepository> {

    @Override
    public void validate(ItemRepository repository, Item entity) {
        Optional<Item> existingItem = repository.findByName(entity.getName());
        if (isDuplicated(entity, existingItem)) {
            throw new DuplicateException("Duplicated item with the same name");
        }
    }

    private boolean isDuplicated(Item entity, Optional<Item> existingItem) {
        return entity.isNew() && existingItem.isPresent() ||
                existingItem.isPresent() && !existingItem.get().getId().toString().equals(entity.getId().toString());
    }
}
