/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wop.common.ui.matisse.engine.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.wop.common.ui.matisse.engine.ImageEngine;

/**
 * {@link ImageEngine} implementation using Glide.
 */

public class GlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(placeholder)
                .centerCrop()
                .override(resize, resize);
        requestBuilder
                .load(uri)
                .apply(myOptions)
                .into(imageView);
//
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()  // some .jpeg files are actually gif
//                .placeholder(placeholder)
//                .override(resize, resize)
//                .centerCrop()
//                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView,
                                 Uri uri) {
        RequestBuilder<Bitmap> requestBuilder = Glide.with(context).asBitmap();
        RequestOptions myOptions = new RequestOptions()
                .placeholder(placeholder)
                .centerCrop()
                .override(resize, resize);
        requestBuilder
                .load(uri)
                .apply(myOptions)
                .into(imageView);
//        Glide.with(context)
//                .load(uri)
//                .asBitmap()
//                .placeholder(placeholder)
//                .override(resize, resize)
//                .centerCrop()
//                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestOptions myOptions = new RequestOptions()
                .override(resizeX, resizeY)
                .priority(Priority.HIGH);
        Glide.with(context)
                .load(uri)
                .apply(myOptions)
                .into(imageView);

//        Glide.with(context)
//                .load(uri)
////                .override(resizeX, resizeY)
//                .priority(Priority.HIGH)
//                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        RequestBuilder<GifDrawable> requestBuilder = Glide.with(context).asGif();
        RequestOptions myOptions = new RequestOptions()
                .priority(Priority.HIGH)
                .override(resizeX, resizeY);
        requestBuilder
                .load(uri)
                .apply(myOptions)
                .into(imageView);

//        Glide.with(context)
//                .load(uri)
//                .asGif()
//                .override(resizeX, resizeY)
//                .priority(Priority.HIGH)
//                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }

}
