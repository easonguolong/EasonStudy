package com.eason.myble.manager;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.eason.myble.ConnActivity;

import java.util.UUID;

/**
 * Created by Administrator on 2017/9/22.
 */

public class BleService extends Service {
    private BluetoothGatt bluetoothGatt;
    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String BleDeviceAddress;

    public int CurConnectState = STATE_DISCONNECTED;
    private static final int STATE_DISCONNECTED = 0,STATE_CONNECTTING=1,STATE_CONNECTED = 2;


    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    //B-0002/B-0004/TLS-01/STB-01
//    Service UUID：fee0
//    Notify：fee1
//    Write:fee1
    public static String BLE_SPP_Service = "0000fee0-0000-1000-8000-00805f9b34fb";
    public static String BLE_SPP_Notify_Characteristic = "0000fee1-0000-1000-8000-00805f9b34fb";
    public static String  BLE_SPP_Write_Characteristic = "0000fee2-0000-1000-8000-00805f9b34fb";
    public static String  BLE_SPP_AT_Characteristic = "0000fee3-0000-1000-8000-00805f9b34fb";

    //ble characteristic
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mWriteCharacteristic;


    //广播特定符
    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACTION_WRITE_SUCCESSFUL =
            "com.example.bluetooth.le.WRITE_SUCCESSFUL";
    public final static String ACTION_GATT_SERVICES_NO_DISCOVERED =
            "com.example.bluetooth.le.GATT_SERVICES_NO_DISCOVERED";

    public final static UUID UUID_BLE_SPP_NOTIFY = UUID.fromString(BLE_SPP_Notify_Characteristic);


    //设备连接状态回调
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {   //连接状态发生变化回调
            String intentAction ;
            if (newState == BluetoothProfile.STATE_CONNECTED){
                intentAction = ACTION_GATT_CONNECTED;
                CurConnectState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                //Attempting to start service discovery
                bluetoothGatt.discoverServices();
            }else if (newState == BluetoothProfile.STATE_DISCONNECTED){
                intentAction = ACTION_GATT_DISCONNECTED;
                CurConnectState = STATE_DISCONNECTED;
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {     //发现设备服务回调
            if (status == BluetoothGatt.GATT_SUCCESS){
                BluetoothGattService service = gatt.getService(UUID.fromString(BLE_SPP_Service));
                if (service!=null){
                    //找到服务，继续找特征值
                    mNotifyCharacteristic = service.getCharacteristic(UUID.fromString(BLE_SPP_Notify_Characteristic));
                    mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BLE_SPP_Write_Characteristic));
                }
                if (mNotifyCharacteristic!=null){
                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                    //使能notify
                    setNotifyGatt(mNotifyCharacteristic,true);
                }
                if (mWriteCharacteristic==null)  //适配没有FEE2的B-0002/04
                    mWriteCharacteristic = service.getCharacteristic(UUID.fromString(BLE_SPP_Notify_Characteristic));
                if (null==service)
                    broadcastUpdate(ACTION_GATT_SERVICES_NO_DISCOVERED);
            }else{

            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {     //读终端设备data结果回调，要启用这个回调，必须使能Gatt连接，也就是writeDescriptor值，比如本例中的setNotifyGatt();
            if (status==BluetoothGatt.GATT_SUCCESS)
                broadcastUpdate(ACTION_DATA_AVAILABLE,characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {     //写入data回调结果
            if (status==BluetoothGatt.GATT_SUCCESS)
                broadcastUpdate(ACTION_WRITE_SUCCESSFUL);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

    };

    public class LocalBinder extends Binder{
        public BleService getService(){
            return BleService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }

    private void close(){
        if (bluetoothGatt==null)
            return;
        bluetoothGatt.close();
        bluetoothGatt = null;
    }


    public boolean connect(final String deviceAddress){
        if (bluetoothAdapter==null || deviceAddress==null){
            return false;
        }
        //之前连接的设备，再次尝试连接
        if (BleDeviceAddress!=null && deviceAddress.equals(BleDeviceAddress) && bluetoothGatt!=null){
            if (bluetoothGatt.connect()){
                CurConnectState = STATE_CONNECTTING;
                return true;
            }else
                return false;
        }
       final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);  //首次连接设备,获取设备
        if (device==null){
            return false;
        }
        bluetoothGatt = device.connectGatt(this,true,gattCallback);    //首次设备连接，回调callBack
        BleDeviceAddress = deviceAddress;
        CurConnectState = STATE_CONNECTTING;
        return true;
    }




    private void broadcastUpdate( String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action,BluetoothGattCharacteristic characteristic){
        final Intent intent = new Intent(action);
        if (UUID_BLE_SPP_NOTIFY.equals(characteristic.getUuid())){
            final byte[] data = characteristic.getValue();
            if (null!=data && data.length>0){
                intent.putExtra(EXTRA_DATA,data);
            }
        }
        sendBroadcast(intent);
    }


    private void setNotifyGatt(BluetoothGattCharacteristic characteristic,boolean enable){
        if (bluetoothAdapter==null || bluetoothGatt == null){
            return;
        }
        bluetoothGatt.setCharacteristicNotification(characteristic,enable);
        if (UUID_BLE_SPP_NOTIFY.equals(characteristic.getUuid())){
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public boolean initialize() {
        if (bluetoothManager == null) {
            bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager == null)
                return false;
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null)
            return false;
        return true;
    }

    public void writeData(byte[] data){
        //String str = ConnActivity.bytesToHexString(data);
        if (mWriteCharacteristic!=null &&data!=null){
            mWriteCharacteristic.setValue(data[0],BluetoothGattCharacteristic.FORMAT_UINT8,0);
            mWriteCharacteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(mWriteCharacteristic);
        }
    }

}
