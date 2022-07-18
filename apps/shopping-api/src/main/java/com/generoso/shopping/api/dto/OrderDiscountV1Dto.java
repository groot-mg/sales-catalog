package com.generoso.shopping.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The Order discount to apply in an order
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
public class OrderDiscountV1Dto {

    @Min(0)
    @Max(100)
    private int discount;
}
