package com.spring.crud.app.example.services;

import com.spring.crud.app.example.models.Item;
import com.spring.crud.app.example.repositories.ItemRepository;
import com.spring.crud.app.example.repositories.OrderItemsRepository;
import com.spring.crud.app.example.services.exceptions.BusinessException;
import com.spring.crud.app.example.services.exceptions.ResourceNotFoundException;
import com.spring.crud.app.example.services.validators.DuplicatedItemValidator;
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
@Transactional(readOnly = true)
public class ItemService {

    private ItemRepository repository;
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    public ItemService(ItemRepository repository, OrderItemsRepository orderItemsRepository) {
        this.repository = repository;
        this.orderItemsRepository = orderItemsRepository;
    }

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
        new DuplicatedItemValidator().validate(repository, item);
    }
}