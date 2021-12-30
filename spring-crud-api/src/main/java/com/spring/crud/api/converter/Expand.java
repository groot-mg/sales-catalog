package com.spring.crud.api.converter;

/**
 * Expanded class.
 *
 * @author Maurício Generoso.
 */
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

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

}