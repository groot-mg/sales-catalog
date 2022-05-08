package com.spring.crud.api.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * The Order Items Data transfer Object
 *
 * @author Mauricio Generoso
 */
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
