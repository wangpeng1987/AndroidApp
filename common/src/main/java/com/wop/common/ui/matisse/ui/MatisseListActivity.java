package com.wop.common.ui.matisse.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wop.common.R;
import com.wop.common.ui.matisse.internal.entity.Album;
import com.wop.common.ui.matisse.internal.ui.AlbumsListFragment;
import com.wop.common.ui.matisse.internal.ui.adapter.AlbumsAdapter;

public class MatisseListActivity extends MatisseBaseActivity implements AlbumsAdapter.OnOpenAlbum {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matisse_list);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_fl,AlbumsListFragment.newInstance(),AlbumsListFragment.class.getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public void open(Album album) {
        Intent intent = new Intent(MatisseListActivity.this,MatisseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("select_album",album);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_from_left);
    }
}
