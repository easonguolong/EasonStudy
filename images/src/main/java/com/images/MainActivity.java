package com.images;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.images.widget.nineImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.nineImageView)
    com.images.widget.nineImageView nineImageView;
    @BindView(R.id.iamgeView)
    ImageView iamgeView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button1)
    Button button1;

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ImageSelectActivity.class);
                intent.putExtra("select",9);
                startActivityForResult(intent,0);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,ImageSelectActivity.class);
                intent1.putExtra("select",1);
                startActivityForResult(intent1,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            switch (requestCode){
                //多图
                case 0:
                    Bundle bundle = data.getExtras();
                    ArrayList<ImageBean> list = bundle.getParcelableArrayList("selectImages");
                    LayoutInflater inflater = LayoutInflater.from(this);
                    if (nineImageView.getChildCount()>0){
                        nineImageView.removeAllViews();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        ImageView imageView = (ImageView)inflater.inflate(R.layout.nine_image,nineImageView,false);
                        Glide.with(this).load(list.get(i).getPath()).skipMemoryCache(true).into(imageView);
                        nineImageView.addView(imageView);
                    }
                    break;
                //头像
                case 1:
                    byte [] bitmap=data.getByteArrayExtra("bitmap");
                    Glide.with(this).load(bitmap).into(iamgeView);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();

    }
}
