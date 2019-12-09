package com.spring.crud.app.example.models;

import javax.persistence.*;
import java.util.List;

/**
 * Item entity.
 *
 * @author Mauricio Generoso
 */
@Entity
@Table(name = "item")
public class Item extends BasicEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeItem type;

    @Column
    private double price;

    @Column
    private boolean active = true;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeItem getType() {
        return type;
    }

    public void setType(TypeItem type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }
}