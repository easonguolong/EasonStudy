package com.eventbusstudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/7/12.
 */

public class SecondActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @BindView(R.id.test2)
    Button mtest2;
    @BindView(R.id.tvTest2)
    TextView tst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_second);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }


    @OnClick(R.id.test2)
    public void onViewClicked() {
        Intent in=new Intent (this,MainActivity.class);
        startActivity(in);
    }


    @Subscribe
    public void onEventMainThread(EventMessage event) {
        String msg = event.getMsg();
        tst.setText(msg);
       // MainActivity.this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
