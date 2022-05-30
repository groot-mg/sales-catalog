package com.spring.crud.api.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResourceNotFoundDetails extends ErrorDetailModel {

    @Builder
    public ResourceNotFoundDetails(String title, int status, String detail, long timestamp, String developerMessage) {
        super(title, status, detail, timestamp, developerMessage);
    }
}
