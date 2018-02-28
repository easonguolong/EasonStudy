package com.eason.myanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/12.
 */

public class PropertyActivity extends AppCompatActivity {
    @BindView(R.id.btn_object)
    Button btnObject;
    @BindView(R.id.btn_value)
    Button btnValue;
    @BindView(R.id.btn_listen)
    Button btnListen;
    @BindView(R.id.btn_AnimatorSet)
    Button btnAnimatorSet;
    @BindView(R.id.id_ball)
    ImageView idBall;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_property);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_object, R.id.btn_value, R.id.btn_listen, R.id.btn_AnimatorSet, R.id.id_ball})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_object:
                ObjectAnimator.ofFloat(view, "rotationX", 0.0F, 360.0F)
                                .setDuration(500)
                                .start();
                break;
            case R.id.btn_value:
                break;
            case R.id.btn_listen:
                break;
            case R.id.btn_AnimatorSet:
                break;
            case R.id.id_ball:
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                        0f, 1f);
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,
                        0, 1f);
                PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,
                        0, 1f);
                ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY,pvhZ).setDuration(1000).start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
