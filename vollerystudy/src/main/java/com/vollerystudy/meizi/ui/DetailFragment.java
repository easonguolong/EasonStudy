package com.vollerystudy.meizi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.vollerystudy.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/7/1.
 */

public class DetailFragment extends Fragment {
    private static final String IMAGE_URL = "imageUrl";
    @Bind(R.id.detail_pv_show_photo)
    PhotoView mPvShowPhoto;

    public static DetailFragment newInstance(String imageUrl) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail,container,false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        String imageUrl = bundle.getString(IMAGE_URL);
        Glide.with(this).load(imageUrl).into(mPvShowPhoto);
        mPvShowPhoto.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
