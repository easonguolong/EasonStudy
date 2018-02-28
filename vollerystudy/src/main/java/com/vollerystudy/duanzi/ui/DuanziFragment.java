package com.vollerystudy.duanzi.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vollerystudy.R;
import com.vollerystudy.common.VolleryHelp;
import com.vollerystudy.common.VolleryResponseCallback;
import com.vollerystudy.duanzi.api.DuanziApi;
import com.vollerystudy.duanzi.bean.DuanziBean;
import com.vollerystudy.duanzi.utils.GsonHelper;
import com.vollerystudy.duanzi.utils.ItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/1.
 */

public class DuanziFragment extends Fragment {

    @Bind(R.id.duanzi_rv_show_duanzi)
    RecyclerView mRvShowDuanzi;
    @Bind(R.id.duanzi_refresh)
    SwipeRefreshLayout mRefresh;
    private DuanziAdapter adapter;

    public static DuanziFragment newInstance() {
        return new DuanziFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_duanzi,container,false);
        ButterKnife.bind(this,view);
        initView();
        initRefresh();
        return view;
    }

    private void initRefresh(){
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
        VolleryHelp.sendHttpGet(getActivity(), DuanziApi.GET_DUANZI, new VolleryResponseCallback() {
            @Override
            public void onSuccess(String s) {
                List<DuanziBean> mDuanziBeanList = GsonHelper.getDuanziBeanList(s);
                mDuanziBeanList.remove(3);
                adapter = new DuanziAdapter(DuanziFragment.this,mDuanziBeanList);
                mRvShowDuanzi.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRvShowDuanzi.setAdapter(adapter);
                adapter.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        Toast.makeText(getActivity(),"position"+position,Toast.LENGTH_LONG).show();
                    }
                });
            }
            @Override
            public void onError(VolleyError error) {
                System.out.println("study"+error);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
