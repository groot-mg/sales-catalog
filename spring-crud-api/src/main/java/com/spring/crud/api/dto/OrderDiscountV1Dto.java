package com.spring.crud.api.dto;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The Order discount to apply in an order
 *
 * @author Mauricio Generoso
 */
@ApiModel(description = "Dto to receive a discount value and apply to an order")
public class OrderDiscountV1Dto {

    @Min(0)
    @Max(100)
    private int discount;

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
