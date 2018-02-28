package com.eason.myanimation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_move,btn_tween,btn_property;
    float dx,dy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_move = (Button)findViewById(R.id.btn_move);
        btn_tween = (Button)findViewById(R.id.btn_tween);
        btn_property = (Button)findViewById(R.id.btn_property);
        btn_tween.setOnClickListener(this);
        btn_property.setOnClickListener(this);
        btn_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                {
                    dx=motionEvent.getX();
                    dy=motionEvent.getY();
                }else if (motionEvent.getAction()==MotionEvent.ACTION_MOVE){
                    view.setX(motionEvent.getRawX()-dx);
                    view.setY(motionEvent.getRawY()-dy);
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_tween:
                Intent intent = new Intent(this,TweenActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_property:
                Intent intent1 = new Intent(this,PropertyActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
