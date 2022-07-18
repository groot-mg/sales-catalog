package com.generoso.shopping.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The Order Data transfer Object
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
public class OrderV1Dto extends BasicV1Dto {

    private boolean open = true;
    private LocalDateTime closedAtDateTime;
    private int discount;
    private double totalPreview;

    private List<OrderItemsV1Dto> orderItems;
}
