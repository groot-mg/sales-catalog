package com.spring.crud.lib.repository;

import com.spring.crud.lib.model.OrderItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * The {@link OrderItems} repository
 *
 * @author Mauricio Generoso
 */
@Repository
public interface OrderItemsRepository extends PagingAndSortingRepository<OrderItems, UUID> {

    boolean existsByItemId(UUID id);

    Page<OrderItems> findAllByOrderId(UUID orderId, Pageable pageable);

    Optional<OrderItems> findByOrderIdAndId(UUID orderId, UUID id);

    Optional<OrderItems> findByOrderIdAndItemId(UUID orderId, UUID itemId);

    long countByOrderId(UUID orderId);
}
