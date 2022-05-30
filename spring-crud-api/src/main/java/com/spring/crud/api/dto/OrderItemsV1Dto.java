package com.spring.crud.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * The Order Items Data transfer Object
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
public class OrderItemsV1Dto extends BasicV1Dto {

    @Min(1)
    private int amount;

    @NotNull
    private UUID itemId;

    private ItemV1Dto item;
}
