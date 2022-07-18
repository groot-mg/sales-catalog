package com.generoso.shopping.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDetailModel {

    private String title;
    private int status;
    private String detail;
    private long timestamp;
    private String developerMessage;
}