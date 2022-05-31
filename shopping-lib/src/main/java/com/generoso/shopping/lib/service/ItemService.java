package com.generoso.shopping.lib.service;

import com.generoso.shopping.lib.exception.BusinessException;
import com.generoso.shopping.lib.exception.ResourceNotFoundException;
import com.generoso.shopping.lib.model.Item;
import com.generoso.shopping.lib.repository.ItemRepository;
import com.generoso.shopping.lib.repository.OrderItemsRepository;
import com.generoso.shopping.lib.service.validators.DuplicatedItemValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to manage and validate an {@link Item}
 *
 * @author Mauricio Generoso
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository repository;
    private final OrderItemsRepository orderItemsRepository;

    private final DuplicatedItemValidator duplicatedItemValidator;

    public Page<Item> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Item findById(UUID id) {
        Optional<Item> item = repository.findById(id);

        if (item.isPresent()) {
            return item.get();
        }
        throw new ResourceNotFoundException("Not found item with id " + id);
    }

    @Transactional
    public void save(Item item) {
        this.validate(item);
        repository.save(item);
    }

    @Transactional
    public void deleteById(UUID id) {
        Item item = findById(id);
        if (orderItemsRepository.existsByItemId(id)) {
            throw new BusinessException("This item is in used");
        }
        delete(item);
    }

    @Transactional
    public void delete(Item item) {
        repository.delete(item);
    }

    @Transactional
    public void activate(Item item) {
        item.setActive(true);
        save(item);
    }

    @Transactional
    public void deactivate(Item item) {
        item.setActive(false);
        save(item);
    }

    private void validate(Item item) {
        duplicatedItemValidator.validate(repository, item);
    }
}