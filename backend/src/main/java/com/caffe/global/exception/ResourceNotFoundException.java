package com.caffe.global.exception;

import com.caffe.global.rsData.RsData;

public class ResourceNotFoundException extends RuntimeException {
    private final String resultCode;
    private final String msg;

    public ResourceNotFoundException(String resultCode, String msg) {
        super(resultCode + ":" + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public RsData<Void> getRsData() {
        return new RsData<>(resultCode, msg, null);
    }
}
