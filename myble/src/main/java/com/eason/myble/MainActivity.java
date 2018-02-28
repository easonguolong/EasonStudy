package com.eason.myble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eason.myble.manager.BLEManager;
import com.eason.myble.manager.BleService;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button open,close,scan;
    private BluetoothAdapter bluetoothAdapter;
    private Handler mHandler;
    private ListView devices ;
    private DeviceAdapter deviceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter =BLEManager.getInStance().initBle(this);
        mHandler = new Handler();
        deviceAdapter = new DeviceAdapter();
        initUI();

    }

    private void initUI(){
        open = (Button) findViewById(R.id.ble_open);
        close = (Button)findViewById(R.id.ble_close);
        scan = (Button)findViewById(R.id.scan);
        devices = (ListView)findViewById(R.id.blelist) ;
        scan.setOnClickListener(this);
        open.setOnClickListener(this);
        close.setOnClickListener(this);
        devices.setAdapter(deviceAdapter);
        devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final BluetoothDevice itemDevice = deviceAdapter.getDevice(i);
                if (null==itemDevice) return;
                Intent intent = new Intent(MainActivity.this,ConnActivity.class);
                intent.putExtra(ConnActivity.Device_name,itemDevice.getName());
                intent.putExtra(ConnActivity.Device_mac,itemDevice.getAddress());
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ble_open:
                openBle();
                break;
            case R.id.ble_close:
                if (bluetoothAdapter.isEnabled())
                    bluetoothAdapter.disable();
                break;
            case R.id.scan:
                deviceAdapter.clear();
                if (bluetoothAdapter.isEnabled()) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bluetoothAdapter.stopLeScan(mLeScanCallback);
                        }
                    }, 10000);
                    bluetoothAdapter.startLeScan(mLeScanCallback);
                }else
                    Toast.makeText(MainActivity.this,"blue closed ,please open it",Toast.LENGTH_SHORT).show();
                break;
        }
    }



    // Device scan callback.   扫描蓝牙设备结果回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                deviceAdapter.addDevice(device);
                                deviceAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };



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


    private void openBle(){
        if (bluetoothAdapter==null)
            bluetoothAdapter =BLEManager.getInStance().initBle(this);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this,"this device not support BLE Feature",Toast.LENGTH_SHORT).show();
            return;
        }
        if (null==bluetoothAdapter){
            Toast.makeText(this,"this device not support BLE Feature",Toast.LENGTH_SHORT).show();
            return;
        }
        //android 6.0以上需要开启位置权限
        if (Build.VERSION.SDK_INT>23){
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission!=PackageManager.PERMISSION_GRANTED)
                //判断是否需要 向用户解释，为什么要申请该权限
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this,"Android6.0使用BLE需要开启定位权限", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            return;
        }
        if (!bluetoothAdapter.isEnabled()){
            Intent enableBle = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBle,1);
        }
    }

    class DeviceAdapter extends BaseAdapter {

        private ArrayList<BluetoothDevice> BleDevices ;
        private LayoutInflater layoutInflater;

        public DeviceAdapter() {
            super();
            BleDevices = new ArrayList<BluetoothDevice>();
            layoutInflater = MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device){
            if (!BleDevices.contains(device))
                BleDevices.add(device);
        }

        public BluetoothDevice getDevice(int position){
            return BleDevices.get(position);
        }

        public void clear(){
            if (BleDevices.size()>0)
                BleDevices.clear();
        }

        @Override
        public int getCount() {
            return BleDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return BleDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (null==view){
                view = layoutInflater.inflate(R .layout.layout_device_list,null);
                viewHolder = new ViewHolder();
                viewHolder.deviceName = (TextView)view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            }else
                viewHolder = (ViewHolder) view.getTag();
            BluetoothDevice device = BleDevices.get(i);
            final String deviceName = device.getName();
            if (null!=deviceName&&deviceName.length()>0){
                viewHolder.deviceName.setText(deviceName);
            }else{
                viewHolder.deviceName.setText("Unknow Device");
            }
            return view;
        }

        class ViewHolder{
            TextView deviceName;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode== Activity.RESULT_CANCELED){
            finish();
            return;
        }
    }
}
