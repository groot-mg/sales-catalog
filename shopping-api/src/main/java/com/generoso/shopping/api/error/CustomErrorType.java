package com.generoso.shopping.api.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomErrorType {

    private final String errorMessage;
}
