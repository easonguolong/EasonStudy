package com.eason.picturevery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luozm.captcha.Captcha;

public class MainActivity extends AppCompatActivity {
    private Captcha captcha;
    private Button btnMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        captcha = (Captcha) findViewById(R.id.captCha);
        btnMode = (Button)findViewById(R.id.btn_mode);
        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (captcha.getMode()==Captcha.MODE_BAR){
                    btnMode.setText("滑动模式");
                    captcha.setMode(captcha.MODE_NONBAR);
                }else{
                    btnMode.setText("无滑动模式");
                    captcha.setMode(captcha.MODE_BAR);
                }
            }
        });
        captcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                Toast.makeText(MainActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                return "验证通过";
            }

            @Override
            public String onFailed(int count) {
                Toast.makeText(MainActivity.this, "验证失败,失败次数" + count, Toast.LENGTH_SHORT).show();
                return "验证失败";
            }

            @Override
            public String onMaxFailed() {
                Toast.makeText(MainActivity.this, "验证超过次数，你的帐号被封锁", Toast.LENGTH_SHORT).show();
                return "可以走了";
            }
        });
    }

}
