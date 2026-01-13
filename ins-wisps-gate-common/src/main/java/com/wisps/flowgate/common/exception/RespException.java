package com.wisps.flowgate.common.exception;

import com.wisps.flowgate.common.consts.RespCode;

public class RespException extends BaseException{
    private static final long serialVersionUID = -2022932784211374134L;

    public RespException() {
        this(RespCode.INTERNAL_ERROR);
    }

    public RespException(RespCode code) {
        super(code.getMsg(), code);
    }

    public RespException(Throwable cause, RespCode code) {
        super(code.getMsg(), cause, code);
    }
}