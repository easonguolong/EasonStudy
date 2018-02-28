package com.recyclestudy;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/7/19.
 */

public class MyOnScrollListener extends RecyclerView.OnScrollListener {

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState == recyclerView.SCROLL_STATE_IDLE){  //recycleView空闲时
            recyclerView.removeOnScrollListener(this);
        }
    }
}
