package com.spring.crud.lib.repository;

import com.spring.crud.lib.model.Item;
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
