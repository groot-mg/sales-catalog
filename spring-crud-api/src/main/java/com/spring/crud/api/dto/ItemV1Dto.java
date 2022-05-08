package com.spring.crud.api.dto;

import com.spring.crud.lib.model.TypeItem;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * The Item Data transfer Object
 *
 * @author Mauricio Generoso
 */
public class ItemV1Dto extends BasicV1Dto {

    @NotBlank
    @Size(max = 100, message = "The max allowed length is 100.")
    private String name;

    @NotNull
    private TypeItem type;

    @NotNull
    @Positive
    private Double price;

    private boolean active;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
