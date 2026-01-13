package com.wisps.flowgate.common.exception;

import com.wisps.flowgate.common.consts.RespCode;

public class BaseException extends RuntimeException{
    private static final long serialVersionUID = -1022831284211374134L;

    public BaseException() {
    }

    protected RespCode code;

    public BaseException(String message, RespCode code) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, Throwable cause, RespCode code) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RespCode code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public RespCode getCode(){
        return code;
    }
}
