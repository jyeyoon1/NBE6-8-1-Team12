package com.caffe.global.rsData;

public record RsData<T>(
        int statusCode,
        String msg,
        T data) {
    public RsData(int statusCode, String msg) {
        this(statusCode, msg, null);
    }
}
