package com.generoso.shopping.lib.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * Order items entity
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItems extends BasicEntity {

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, name = "item_id")
    private UUID itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_order_items_item"),
            insertable = false, updatable = false)
    private Item item;

    @Column(nullable = false, name = "order_id")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_items_order"),
            insertable = false, updatable = false)
    private Order order;
}
