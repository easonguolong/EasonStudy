package com.eason.databinding;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.eason.databinding.Bean.User;
import com.eason.databinding.databinding.ActivityMainBinding;
import com.eason.databinding.listen.MyHandler;


public class MainActivity extends AppCompatActivity {
    private User user;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        user = new User();
        user.setFirstName("guo");
        user.setLastName("eason");
        binding.setUser(user);

        binding.setHandler(myHandler);
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                user.setLastName("Hello eason guo ");
            }
        }.start();
    }

    MyHandler myHandler = new MyHandler(){
        @Override
        public void onClickFriend(View view) {
//            EasonTest easonTest = new EasonTest();
//            easonTest.init(MainActivity.this);
//            easonTest.toToast();
//            RYDMqttServer.getInstance().initServer(MainActivity.this,"tcp://112.74.38.244:61613");
//            RYDMqttHelper.getInstance().init(new RYDMqttCallBack() {
//                @Override
//                public void gwAddDevResult(JSONObject jsonObjec) {
//
//                }
//
//                @Override
//                public void serAddDevResult(JSONObject jsonObjec) {
//
//                }
//
//                @Override
//                public void gwDelDevResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void serDelDevResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void getWGInfoResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void getAllDevSimaple(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void singleDevChanged(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void gwOffLine(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void getSceneResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void delGWResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void exeSceneResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void addSceneResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void editSceneResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void delSceneResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void getTimerResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void delTimerResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void addTimerResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void addLinkResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void delLinkResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void getLinkResult(JSONObject jsonObject) {
//
//                }
//
//                @Override
//                public void gwUpdateResult(JSONObject jsonObject) {
//
//                }
//
//            });
//            try {
//                RYDMqttHelper.getInstance().getGWInfo("2017011001");
//                RYDMqttHelper.getInstance().getGWHardwareInfo("2017011001");
//                RYDMqttHelper.getInstance().getScene("2017011001");
//            } catch (MqttException e) {
//                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
//            } catch (JSONException e) {
//                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
            user.setLastName("Hello World !!!");
        }
    };

}
