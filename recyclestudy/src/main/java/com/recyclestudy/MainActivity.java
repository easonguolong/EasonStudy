package com.recyclestudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRvLayer;
    private RecyclerView mRvRoom;
    private ObservableHorizontalScrollView mSvRoom;
    private List mData = new ArrayList();

    private final RecyclerView.OnScrollListener mLayerOSL = new MyOnScrollListener(){
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            mRvRoom.scrollBy(dx,dy);
        }
    };

    private final RecyclerView.OnScrollListener mRoomOSL = new MyOnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 当单元（房间）列表滑动时，楼层列表也滑动
            mRvLayer.scrollBy(dx, dy);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitView();
    }

    private void InitView(){
        mRvLayer = (RecyclerView)findViewById(R.id.rv_layer);
        mRvRoom = (RecyclerView)findViewById(R.id.rv_room);
        mSvRoom = (ObservableHorizontalScrollView)findViewById(R.id.sv_room);

        mRvRoom.setLayoutManager(new LinearLayoutManager(this));
    }
}
