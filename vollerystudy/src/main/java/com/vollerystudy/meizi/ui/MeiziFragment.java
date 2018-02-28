package com.vollerystudy.meizi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.vollerystudy.R;
import com.vollerystudy.common.VolleryHelp;
import com.vollerystudy.common.VolleryResponseCallback;
import com.vollerystudy.duanzi.utils.GsonHelper;
import com.vollerystudy.meizi.api.MeiziApi;
import com.vollerystudy.meizi.bean.MeiziBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/1.
 */

public class MeiziFragment extends Fragment {
    @Bind(R.id.meizi_rv_show_meizi)
    RecyclerView mRvShowMeizi;
    @Bind(R.id.meizi_refresh)
    SwipeRefreshLayout mRefresh;

    List<MeiziBean> meziBeanList = new ArrayList<>();
    private static String response ="" ;

    public static MeiziFragment newInstance() {
        return new MeiziFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_meizi,container,false);
        ButterKnife.bind(this,view);
        initView();
        refreshMeizi();
        return view;
    }


    private void refreshMeizi() {
        mRefresh.setColorSchemeResources(R.color.colorPrimary);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void initView(){
        VolleryHelp.sendHttpGet(getActivity(), MeiziApi.getMeiziApi(), new VolleryResponseCallback() {
            @Override
            public void onSuccess(String s) {
                response = s;
                meziBeanList = com.vollerystudy.meizi.utils.GsonHelper.getMeiziBean(response);
                mRvShowMeizi.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                Collections.shuffle(meziBeanList);
                mRvShowMeizi.setAdapter(new MeiziAdapter(meziBeanList,MeiziFragment.this));
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    @Override
    public void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
