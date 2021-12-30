package com.spring.crud.app.example.api.dtos;

import io.swagger.annotations.ApiModel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The Order Data transfer Object
 *
 * @author Mauricio Generoso
 */
@ApiModel(description = "Details about the order")
public class OrderV1Dto extends BasicV1Dto {

    private boolean open = true;
    private LocalDateTime closedAtDateTime;
    private int discount;
    private double totalPreview;

    private List<OrderItemsV1Dto> orderItems;

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

    public List<OrderItemsV1Dto> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemsV1Dto> orderItems) {
        this.orderItems = orderItems;
    }
}
