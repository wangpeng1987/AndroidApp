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
package com.wop.common.ui.matisse.internal.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.wop.common.R;
import com.wop.common.ui.matisse.internal.entity.Item;
import com.wop.common.ui.matisse.internal.utils.PathUtils;
import com.wop.common.ui.matisse.internal.utils.PhotoMetadataUtils;

import java.io.File;

import me.relex.photodraweeview.PhotoDraweeView;

public class PreviewItemFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";

    public static PreviewItemFragment newInstance(Item item) {
        PreviewItemFragment fragment = new PreviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_ITEM, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_album_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Item item = getArguments().getParcelable(ARGS_ITEM);
        if (item == null) {
            return;
        }

        View videoPlayButton = view.findViewById(R.id.video_play_button);
        if (item.isVideo()) {
            videoPlayButton.setVisibility(View.VISIBLE);
            videoPlayButton.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(item.uri, "video/*");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), R.string.error_no_video_activity, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            videoPlayButton.setVisibility(View.GONE);
        }

        //ImageViewTouch image = (ImageViewTouch) view.findViewById(R.id.image_view);
        final PhotoDraweeView image = (PhotoDraweeView) view.findViewById(R.id.image_view);
        //image.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        final Point size = PhotoMetadataUtils.getBitmapSize(item.getContentUri(), getActivity());
        if (item.isGif()) {
//            SelectionSpec.getInstance().imageEngine.loadGifImage(getContext(), size.x, size.y, image,
//                    item.getContentUri());
            String path = PathUtils.getPath(getContext(), item.getContentUri());
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller.setUri(Uri.fromFile(new File(path)));//item.getContentUri()
            controller.setOldController(image.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    image.update(imageInfo.getWidth(), imageInfo.getHeight());
                    if (animatable != null) {
                        animatable.start();
                    }
                }
            });
            image.setController(controller.build());
        } else {
//            SelectionSpec.getInstance().imageEngine.loadImage(getContext(), size.x, size.y, image,
//                    item.getContentUri());
            String path = PathUtils.getPath(getContext(), item.getContentUri());
            PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            //controller.setUri(item.getContentUri());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(item.getContentUri())
                    .setResizeOptions(new ResizeOptions(size.x,size.y)).build();
            controller.setImageRequest(request);
            controller.setOldController(image.getController());
            controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);
                    image.update(imageInfo.getWidth(), imageInfo.getHeight());
                }
            });
            image.setController(controller.build());
        }
    }

    public void resetView() {
        if (getView() != null) {
            //((ImageViewTouch) getView().findViewById(R.id.image_view)).resetMatrix();
            ((PhotoDraweeView) getView().findViewById(R.id.image_view)).setScale(1.0f);
        }
    }
}
