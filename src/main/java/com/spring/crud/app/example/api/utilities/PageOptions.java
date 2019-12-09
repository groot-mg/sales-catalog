package com.spring.crud.app.example.api.utilities;

import javax.validation.constraints.Min;

public final class PageOptions {

    @Min(0)
    private int pageNumber = 0;

    @Min(0)
    private int pageSize = 15;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
