package com.savemyreceipt.smr.exception.model;

import com.savemyreceipt.smr.exception.ErrorStatus;

public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public int getHttpStatus() {
        return errorStatus.getHttpStatusCode();
    }

}
