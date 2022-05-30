package com.spring.crud.api.utilities;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public final class PageOptions {

    @Min(0)
    private int pageNumber = 0;

    @Min(0)
    private int pageSize = 15;
}
