package com.castify.tv.models;

public class ErrorModel {
    private String errorCode;
    private int errorMsg;

    public ErrorModel(String errorCode, int errorMsg ){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getErrorMsg() {
        return errorMsg;
    }
}
