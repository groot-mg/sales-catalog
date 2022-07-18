package com.generoso.shopping.api.dto;

import com.generoso.shopping.lib.model.TypeItem;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * The Item Data transfer Object
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
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
}
