package com.eason.myble.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;

/**
 * Created by Administrator on 2017/9/21.
 */

public class BLEManager {
    private BluetoothGatt bluetoothGatt;

    public static volatile BLEManager inStance;

    public  static BLEManager getInStance(){
        if (null==inStance){
            synchronized (BLEManager.class){
                if (null==inStance)
                    inStance = new BLEManager();
            }
        }
            return inStance;
    }

    public BluetoothAdapter initBle(Context context){
        BluetoothAdapter bluetoothAdapter;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (null==bluetoothManager)
            return null ;
        bluetoothAdapter = bluetoothManager.getAdapter();
        if (null==bluetoothAdapter)
            return null;
        return bluetoothAdapter;
        }
}

