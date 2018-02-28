package com.eventbusstudy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test)
    Button test;
    @BindView(R.id.tvTest)
    TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Unbinder unbinder = new Unbinder() {
            @Override
            public void unbind() {

            }
        };
    }

    @OnClick(R.id.test)
    public void onViewClicked() {
        EventBus.getDefault().post(new EventMessage("fdsfsd"));
    }
}
