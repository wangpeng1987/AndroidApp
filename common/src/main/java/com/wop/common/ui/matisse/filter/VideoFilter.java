package com.wop.common.ui.matisse.filter;

import android.content.Context;
import android.text.TextUtils;

import com.wop.common.ui.matisse.MimeType;
import com.wop.common.ui.matisse.internal.entity.IncapableCause;
import com.wop.common.ui.matisse.internal.entity.Item;

import java.util.Set;

/**
 * Created by liuwenji on 2017/12/28.
 */

public class VideoFilter extends Filter{

    private int mMaxDuration;

    public VideoFilter(int maxDuration) {
        mMaxDuration = maxDuration;
    }

    @Override
    public Set<MimeType> constraintTypes() {
        return MimeType.ofVideo();
    }

    @Override
    public IncapableCause filter(Context context, Item item) {
        if (!needFiltering(context, item))
            return null;
        String error_video = "";
        if (item.duration < 1000 || item.size < 1024) {
            error_video = "The file that you are trying to send is too short.";
        } else if (item.duration > mMaxDuration){
            error_video = "The file that you are trying to send is too large.";
        }
        if (!TextUtils.isEmpty(error_video)) {
            return new IncapableCause(IncapableCause.DIALOG, error_video);
        }
        return null;
    }
}
