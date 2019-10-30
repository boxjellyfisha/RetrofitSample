package com.deer.retrofitapisample.obj;

import com.google.gson.annotations.Expose;

public class BaseResponse {
    @Expose
    private int statusCode;

    @Expose
    private String errorCode;

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
    //todo do some action to deal with the error code
}
