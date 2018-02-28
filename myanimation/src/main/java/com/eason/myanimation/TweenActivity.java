package com.eason.myanimation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2017/9/12.
 */

public class TweenActivity extends AppCompatActivity  {

    @BindView(R.id.alpha)
    Button alpha;
    @BindView(R.id.scale)
    Button scale;
    @BindView(R.id.rotate)
    Button rotate;
    @BindView(R.id.translate)
    Button translate;
    @BindView(R.id.combo)
    Button combo;
    @BindView(R.id.go_other_activity)
    Button goOtherActivity;
    @BindView(R.id.image1)
    ImageView image1;
    private Unbinder unbinder;


    private Animation alphaAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tween);
        ButterKnife.bind(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder = new Unbinder() {
            @Override
            public void unbind() {
                unbinder.unbind();
            }
        };
    }


    //可以设置动画监听器，在监听动画执行的各个阶段
    @OnClick({R.id.alpha, R.id.scale, R.id.rotate, R.id.translate, R.id.combo, R.id.go_other_activity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alpha:
                alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.alpha);
                alphaAnimation.setFillAfter(true);
                image1.startAnimation(alphaAnimation);
                break;
            case R.id.scale:
                alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.scale);
                alphaAnimation.setFillAfter(true);
                image1.startAnimation(alphaAnimation);
                break;
            case R.id.rotate:
                alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.rotate);
                alphaAnimation.setFillAfter(true);
                image1.startAnimation(alphaAnimation);
                break;
            case R.id.translate:
                alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.translate);
                alphaAnimation.setFillAfter(true);
                image1.startAnimation(alphaAnimation);
                break;
            case R.id.combo:
                alphaAnimation = AnimationUtils.loadAnimation(this,R.anim.combo);
                alphaAnimation.setFillAfter(true);
                image1.startAnimation(alphaAnimation);
                break;
            case R.id.go_other_activity:
                break;
        }
    }
}
