package com.eason.myble;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eason.myble.manager.BleService;

/**
 * Created by Administrator on 2017/12/10.
 */

public class ConnActivity extends AppCompatActivity {
    private Button btn_send, btn_clear;
    private EditText sendtv;
    private TextView receiveTv,Connted;

    public static String Device_name = "DEVICE_NAME", Device_mac = "DEVICE_MAC";

    private String mDeviceName, mDeviceMac;

    private BleService bleService;

    private StringBuilder mData = new StringBuilder();

    byte[] sendData;

    int sendIndex,sendDatalength;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_connect);
        initUI();
        Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(Device_name);
        mDeviceMac = intent.getStringExtra(Device_mac);

        Intent gattService = new Intent(ConnActivity.this, BleService.class);
        bindService(gattService, mServiceConnecetion, BIND_AUTO_CREATE);
    }

    private void initUI() {
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_clear = (Button) findViewById(R.id.btn_clean);
        sendtv = (EditText) findViewById(R.id.sendData);
        receiveTv = (TextView) findViewById(R.id.receData);
        Connted = (TextView) findViewById(R.id.connState);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData = sendtv.getText().toString().getBytes();
                if (sendData.length > 0) {
                    sendIndex = 0;    //发送数据的下标
                    sendDatalength = sendData.length;   //发送数据的总长度
                    sendData();
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receiveTv.getText().length() > 0) {
                    receiveTv.setText("");
                    mData.delete(0, mData.length());
                }
            }
        });
    }


    public void sendData(){
        if (sendDatalength>20){            //BLE限制不能超过20字节 , 则分包发送
            final byte[] buf = new byte[20];
            for (int i = 0; i <20 ; i++) {
                buf[i]= sendData[sendIndex+i];
            }
            sendIndex+=20;
            bleService.writeData(buf);
            sendDatalength-=20;
        }else{
            final byte[] buf = new byte[sendDatalength];
            for (int i = 0; i <sendDatalength ; i++) {
                buf[i] = sendData[sendIndex+i];
            }
            bleService.writeData(buf);
            sendDatalength = 0;
            sendIndex = 0;
        }
    }

    //管理蓝牙service的生命周期
    private final ServiceConnection mServiceConnecetion = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bleService = ((BleService.LocalBinder) iBinder).getService();
            if (!bleService.initialize()) {    //判断是否支持低功耗蓝牙
                finish();
            }
            bleService.connect(mDeviceMac); //支持则连接设备
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;
        }
    };


    private final BroadcastReceiver mReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {

            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)){
                //BlueGatt 没有找到则再次尝试连接
                updateState(false);
                bleService.connect(mDeviceMac);
            }else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)){
                ////特征值找到才代表连接成功
                updateState(true);
            }else if (BleService.ACTION_GATT_SERVICES_NO_DISCOVERED.equals(action)){
                bleService.connect(mDeviceMac);
            }else if (BleService.ACTION_DATA_AVAILABLE.equals(action)){
               //收到设备发送给app端的讯息，显示在UI上面
                disPlayReceive(intent.getByteArrayExtra(BleService.EXTRA_DATA));
            }else if (BleService.ACTION_WRITE_SUCCESSFUL.equals(action)){
                //App端写入数据成功回调，PC端串口工具会显示
                //20字节发送完毕，则发送后面的字节数
                if (sendDatalength>0){
                    sendData();
                }
            }
        }
    };

    private void updateState(boolean isConnted){
        if (isConnted)
            Connted.setText("Connted");
        else
            Connted.setText("disConnted");
    }

    private void registBroad(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BleService.ACTION_WRITE_SUCCESSFUL);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_NO_DISCOVERED);
        registerReceiver(mReceive,intentFilter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registBroad();
        if (bleService!=null)
            bleService.connect(mDeviceMac);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceive);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnecetion);
        bleService=null;
    }


    private void disPlayReceive(byte[] buf){
        String s = asciiToString(buf);
        mData.append(s);
        receiveTv.setText(mData.toString());

    }

    public String bytesToString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[v >>> 4];
            hexChars[i * 2 + 1] = hexArray[v & 0x0F];

            sb.append(hexChars[i * 2]);
            sb.append(hexChars[i * 2 + 1]);
            sb.append(' ');
        }
        return sb.toString();
    }

    public String asciiToString(byte[] bytes) {
        char[] buf = new char[bytes.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (char) bytes[i];
            sb.append(buf[i]);
        }
        return sb.toString();
    }
}
