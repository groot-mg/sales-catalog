package com.spring.crud.app.example.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order entity
 *
 * @author Mauricio Generoso
 */
@Entity
@Table(name = "ordered")
public class Order extends BasicEntity {

    @Column(nullable = false)
    private boolean open = true;

    @Column
    private LocalDateTime closedAtDateTime;

    @Column(nullable = false)
    private int discount = 0;

    @Transient
    private double totalPreview;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<OrderItems> orderItems;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public LocalDateTime getClosedAtDateTime() {
        return closedAtDateTime;
    }

    public void setClosedAtDateTime(LocalDateTime closedAtDateTime) {
        this.closedAtDateTime = closedAtDateTime;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getTotalPreview() {
        return totalPreview;
    }

    public void setTotalPreview(double totalPreview) {
        this.totalPreview = totalPreview;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }
}
