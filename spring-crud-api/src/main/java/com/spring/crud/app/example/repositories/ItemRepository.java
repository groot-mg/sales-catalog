package com.spring.crud.app.example.repositories;

import com.spring.crud.app.example.models.Item;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@link Item} repository
 *
 * @author Mauricio Generoso
 */
@Repository
public interface ItemRepository extends PagingAndSortingRepository<Item, UUID> {

    Optional<Item> findByName(String name);
}
