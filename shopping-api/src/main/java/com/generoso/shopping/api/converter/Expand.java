package com.generoso.shopping.api.converter;

import lombok.Getter;
import lombok.Setter;

/**
 * Expanded class.
 *
 * @author Maurício Generoso.
 */
@Getter
@Setter
public class Expand {

    private String names = "";

    public Expand() {
    }

    public Expand(String names) {
        this.names = names;
    }

    /**
     * Return if exists a item in expanded value.
     *
     * @param columnName name of the columns to expan.
     */
    boolean contains(String columnName) {
        return names.contains("all") || names.contains(columnName);
    }

}