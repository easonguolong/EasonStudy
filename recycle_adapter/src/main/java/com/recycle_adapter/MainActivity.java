package com.recycle_adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> mDatas;
    private MyRecyclerAdapter recycleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recycleList);
        initData();
        recycleAdapter = new MyRecyclerAdapter(MainActivity.this,mDatas);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        //设置Adapter
        recycleAdapter.setItemOnclickListener(new MyRecyclerAdapter.ItemOnclickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this,"position="+position,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(MainActivity.this,"position="+position,Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter( recycleAdapter);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));

    }

    private void initData() {
        mDatas = new ArrayList<String>();
        for ( int i=0; i < 40; i++) {
            mDatas.add( "item"+i);
        }
    }

}
