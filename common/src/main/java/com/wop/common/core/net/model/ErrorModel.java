package com.wop.common.core.net.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author woniu
 * @title ErrorModel
 * @description
 * @since 2018/9/17 下午3:16
 */
public class ErrorModel {

    @JSONField(name = "code")
    private int code;
    @JSONField(name = "msg")
    private String msg;

    public ErrorModel() {
    }

    public ErrorModel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
