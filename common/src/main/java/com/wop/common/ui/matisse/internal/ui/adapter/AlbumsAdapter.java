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
package com.wop.common.ui.matisse.internal.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wop.common.R;
import com.wop.common.ui.matisse.internal.entity.Album;
import com.wop.common.ui.matisse.internal.entity.SelectionSpec;

import java.io.File;

public class AlbumsAdapter extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> {

    private final Drawable mPlaceholder;
    private Context context;

    public AlbumsAdapter(Context context, Cursor c, boolean autoRequery) {
        super(c);
        this.context = context;
        mPlaceholder = new ColorDrawable(context.getResources().getColor(R.color.colour_5));
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final Album album = Album.valueOf(cursor);
            viewHolder.albumNameActv.setText(album.getDisplayName(context));
            viewHolder.albumCountActv.setText(String.valueOf(album.getCount()));

            // do not need to load animated Gif
            SelectionSpec.getInstance().imageEngine.loadThumbnail(context, context.getResources().getDimensionPixelSize(R
                            .dimen.dimen_48_dp), mPlaceholder,
                    viewHolder.albumThumbAciv, Uri.fromFile(new File(album.getCoverPath())));

            viewHolder.itemView.setOnClickListener(v -> {
                if (v.getContext() instanceof OnOpenAlbum) {
                    ((OnOpenAlbum) v.getContext()).open(album);
                }
            });
        }
    }

    @Override
    protected int getItemViewType(int position, Cursor cursor) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_list, parent, false);
        return new ViewHolder(v);
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView albumThumbAciv;
        private AppCompatTextView albumNameActv;
        private AppCompatTextView albumCountActv;

        ViewHolder(View itemView) {
            super(itemView);
            albumThumbAciv = itemView.findViewById(R.id.album_thumb_aciv);
            albumNameActv = itemView.findViewById(R.id.album_name_actv);
            albumCountActv = itemView.findViewById(R.id.album_count_actv);
        }
    }

    public interface OnOpenAlbum {
        void open(Album album);
    }
}
