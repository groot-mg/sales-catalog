package com.spring.crud.lib.repository;

import com.spring.crud.lib.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * The {@link Order} repository
 *
 * @author Mauricio Generoso
 */
@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {
}
