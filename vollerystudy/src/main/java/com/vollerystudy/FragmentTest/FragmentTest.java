package com.vollerystudy.FragmentTest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vollerystudy.R;

/**
 * Created by Administrator on 2017/7/6.
 */

public class FragmentTest extends Fragment {


   public static FragmentTest newInstance() {
        return new FragmentTest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.layout_fragment_test, container, false);
        return view;
    }
}
