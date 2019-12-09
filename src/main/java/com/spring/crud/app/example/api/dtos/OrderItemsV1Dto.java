package com.spring.crud.app.example.api.dtos;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * The Order Items Data transfer Object
 *
 * @author Mauricio Generoso
 */
@ApiModel(description = "Details about the order item")
public class OrderItemsV1Dto extends BasicV1Dto {

    @Min(1)
    private int amount;

    @NotNull
    private UUID itemId;

    private ItemV1Dto item;

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

    public ItemV1Dto getItem() {
        return item;
    }

    public void setItem(ItemV1Dto item) {
        this.item = item;
    }
}
