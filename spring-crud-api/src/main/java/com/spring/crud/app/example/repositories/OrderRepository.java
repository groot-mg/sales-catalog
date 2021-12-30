package com.spring.crud.app.example.repositories;

import com.spring.crud.app.example.models.Order;
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
