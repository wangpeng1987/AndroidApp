package com.wop.common.core.net;

import android.text.TextUtils;

import com.wop.common.core.net.model.ErrorModel;

/**
 * @author woniu
 * @title ApiException
 * @description
 * @since 2018/9/17 下午3:20
 */
public class ApiException extends RuntimeException {

    private ErrorModel mModel;

    public ApiException(ErrorModel mModel) {
        this.mModel = mModel;
    }

    public ApiException(int code, String msg) {
        mModel = new ErrorModel(code, msg);
    }

    public ErrorModel getErrorModel() {
        return mModel;
    }

    @Override
    public String getMessage() {
        if (null != mModel && !TextUtils.isEmpty(mModel.getMsg())) {
            return mModel.getMsg();
        } else {
            return super.getMessage();
        }
    }

    public int getCode() {
        if (null != mModel) {
            return mModel.getCode();
        } else {
            return -1;
        }
    }

}
