package com.spring.crud.api.error;

import lombok.*;

@Getter
public class ErrorDetail extends ErrorDetailModel {

    @Builder
    public ErrorDetail(String title, int status, String detail, long timestamp, String developerMessage) {
        super(title, status, detail, timestamp, developerMessage);
    }
}
