package com.scroll_listview;

import android.annotation.TargetApi;
import android.drm.DrmManagerClient;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mlist;
    private ScrollView mscroll;
    private ArrayList<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        datas = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            datas.add(i,String.valueOf(i));
        }
        InitUi();
    }


    private void InitUi(){
        mscroll =(ScrollView)findViewById(R.id.scrolltest);
        mlist = (ListView)findViewById(R.id.listviewtest);
        mlist.setAdapter(new ListAdapter(this,datas));
        mlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        setListViewHeight(mlist);
    }


    private void setListViewHeight(ListView listview){
        if (listview==null){
            return ;
        }
       android.widget.ListAdapter listAdapter = listview.getAdapter();
        if (listAdapter==null)
            return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount() ; i++) {
            View listItem = listAdapter.getView(i,null,listview);
            listItem.measure(0,0);
            totalHeight+=listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams param = listview.getLayoutParams();
        param.height = totalHeight+(listview.getDividerHeight()*(listAdapter.getCount()-1));
        listview.setLayoutParams(param);
    }

}
