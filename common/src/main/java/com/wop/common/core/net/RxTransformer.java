package com.wop.common.core.net;

import com.wop.common.core.net.model.ErrorModel;
import com.wop.common.core.net.model.ResponseModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

import static com.wop.common.core.net.NetConstants.HTTP_SUCCESS_CODE;


/**
 * @author woniu
 * @title RxTransformer
 * @description
 * @since 2018/9/17 下午3:14
 */
public class RxTransformer {

    public static <T> ObservableTransformer<? super ResponseModel<T>, ?> handleData() {
        return (ObservableTransformer<ResponseModel<T>, T>) upstream -> upstream.flatMap((Function<ResponseModel<T>, ObservableSource<T>>) response -> {
            if (HTTP_SUCCESS_CODE == response.getCode()) {
                return Observable.just(response.getData());
            } else {
                int code = response.getCode();
                if (code == 400) {
                    return Observable.error(new ApiException(response.getError()));
                } else {
                    return Observable.error(new ApiException(new ErrorModel(code, "error, code: " + response.getError().getCode() + "   msg : " + response.getError().getMsg())));
                }
            }
        });
    }

}
