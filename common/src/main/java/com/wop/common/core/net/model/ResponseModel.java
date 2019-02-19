package com.wop.common.core.net.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author woniu
 * @title ResponseModel
 * @description
 * @since 2018/9/17 下午3:15
 */
public class ResponseModel<T> {

    @JSONField(name = "code")
    private int code;

    @JSONField(name = "data")
    private T data ;

    @JSONField(name = "error")
    private ErrorModel error;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ErrorModel getError() {
        return error;
    }

    public void setError(ErrorModel error) {
        this.error = error;
    }
}
