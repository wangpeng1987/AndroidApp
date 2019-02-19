package com.wop.common.ui.matisse.filter;

import android.content.Context;
import android.text.TextUtils;

import com.wop.common.ui.matisse.MimeType;
import com.wop.common.ui.matisse.internal.entity.IncapableCause;
import com.wop.common.ui.matisse.internal.entity.Item;
import com.wop.common.ui.matisse.internal.utils.PathUtils;

import java.io.File;
import java.util.Set;

/**
 * Created by liuwenji on 2017/12/28.
 */

public class ImgFilter extends Filter {

    private int mMaxSize;

    public ImgFilter(int maxSize) {
        mMaxSize = maxSize;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return MimeType.ofImage();
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;
        String path = PathUtils.getPath(context, item.getContentUri());
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {//如果目录为空或者文件不存在，点击时要给提示。
            return new IncapableCause(IncapableCause.DIALOG, "The file that you are trying to send is no exist.");
        }
        String error_img = "";
        if(item.isImage() && item.size > mMaxSize) {
            error_img = "The file that you are trying to send is too large.";
        }
        if (!TextUtils.isEmpty(error_img)) {
            return new IncapableCause(IncapableCause.DIALOG, error_img);
        }
        return null;
    }
}
