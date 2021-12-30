package com.spring.crud.api.error;

public class CustomErrorType {

    private String errorMessage;

    public CustomErrorType(String errorMessage) {  this.errorMessage = errorMessage; }

    public String getErrorMessage() {  return errorMessage; }
}
