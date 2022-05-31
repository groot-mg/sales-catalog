package com.generoso.shopping.lib.repository;

import com.generoso.shopping.lib.model.Order;
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
