package com.spring.crud.app.example.models;

import javax.persistence.*;
import java.util.UUID;

/**
 * Order items entity
 *
 * @author Mauricio Generoso
 */
@Entity
@Table(name = "ordered_items")
public class OrderItems extends BasicEntity {

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, name = "item_id")
    private UUID itemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_ordered_items_item"),
            insertable = false, updatable = false)
    private Item item;

    @Column(nullable = false, name = "ordered_id")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordered_id", foreignKey = @ForeignKey(name = "fk_ordered_items_ordered"),
            insertable = false, updatable = false)
    private Order order;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
