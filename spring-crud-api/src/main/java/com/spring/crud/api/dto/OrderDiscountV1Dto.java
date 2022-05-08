package com.spring.crud.api.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * The Order discount to apply in an order
 *
 * @author Mauricio Generoso
 */
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
