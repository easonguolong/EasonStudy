package com.retrofit_gson;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.retrofit_gson.Net.CommObserver;
import com.retrofit_gson.Net.Composer;
import com.retrofit_gson.Net.NetService;
import com.retrofit_gson.Net.RetrofitManager;
import com.retrofit_gson.model.Login;
import com.retrofit_gson.model.LoginResult;

import java.security.MessageDigest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.code)
    EditText code;
    @BindView(R.id.login)
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RetrofitManager.setBaseUrl("http://api.songlai.wang");
        unbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.login)
    public void onViewClick(){
       String user_phone = phone.getText().toString().trim();
       String user_id = code.getText().toString().trim();
        String deivce_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String times = String.valueOf(System.currentTimeMillis());
        String nonce = "321";
        String signature = encode(user_id+deivce_id+nonce)+encode(times+"BEbri4pjRAumyjYbLUF5mA1a7Yw");
        //获取token值，并注册到第三方服务器
        RetrofitManager.createService(NetService.class)
                .getTokens(new Login(user_id,deivce_id,times,nonce,signature))
                .compose(Composer.<LoginResult>commSchedulers())
                .subscribe(new CommObserver<LoginResult>() {
                    @Override
                    public void onNext(LoginResult result) {
                        String token = result.getToken();
                        String userId = result.getUserId();
                        phone.setText(userId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public static String encode(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int i : b) {
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String res = buf.toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
