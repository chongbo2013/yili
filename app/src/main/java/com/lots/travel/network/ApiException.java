package com.lots.travel.network;

public class ApiException extends RuntimeException {
    private int code;
    private String errorMsg;

    public ApiException(int code,String errorMsg){
        super("code="+code+" "+"errorMsg="+errorMsg);
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
