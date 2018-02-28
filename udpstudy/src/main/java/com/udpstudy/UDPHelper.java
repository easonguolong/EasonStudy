package com.udpstudy;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Administrator on 2017/12/9.
 */

public class UDPHelper {
    private final static String UDPLOCK = "UDPLOCK";
    private static UDPHelper udpHelper = null;
    private WeakReference<Context> weakReferenceContext ;
    MulticastSocket datagramSocket = null;
    android.os.Handler handler;
    byte [] data;  //需要发送的数据
    int port;   //监听的端口
    String ipAdress;   //连接的Ip地址
    SendThread sendThread;
    boolean isSend = false,isReceive = false;
    int Count =0;
    private WifiManager.MulticastLock lock;
    String contactId;
    public final static int REPLAY_DEVICE_SUCCESS=100;
    private boolean isListn=false;


    public UDPHelper(Context mContext){
        weakReferenceContext = new WeakReference<Context>(mContext);
        WifiManager manager = (WifiManager)weakReferenceContext.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        lock = manager.createMulticastLock(UDPLOCK);
    }

    public synchronized static UDPHelper getInstance(Context mContext) {
        if (null == udpHelper) {
            synchronized (UDPHelper.class) {
                udpHelper = new UDPHelper(mContext.getApplicationContext());
            }
        }
        return udpHelper;
    }

    public void setHandler(android.os.Handler handler){
      this.handler = handler;
    }


    public void send(byte[] message,int port,String ip,String contactId){
        this.data= message;
        this.port = port;
        this.ipAdress = ip;
        this.contactId = contactId;
        openThread();
    }

    public void startListen(final int port){
        new Thread(){
            @Override
            public void run() {
                listen(port);
            }
        }.start();
    }

    private void listen(int port){
        byte [] message = new byte[512];
        isListn = true;
        try {
            datagramSocket = new MulticastSocket(port);
            datagramSocket.setBroadcast(true);
            datagramSocket.setLoopbackMode(true);
            DatagramPacket datagramPacket = new DatagramPacket(message,message.length);
            while(isListn){
                MulticastLock();
                datagramSocket.receive(datagramPacket);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            MulticastUnLock();
            stopListen();
        }
    }

    class SendThread extends  Thread {
        @Override
        public void run() {
            DatagramSocket udpSocket = null;
            DatagramPacket dataPack  = null;
            try{
                Count = 0;
                udpSocket = new DatagramSocket();
                InetAddress local = null;
                local = InetAddress.getByName(ipAdress);
                dataPack = new DatagramPacket(data ,data.length,local,port);
                while(isSend){
                    try {
                        sleep(1000);
                        udpSocket.send(dataPack);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                byte [] receiveData = {52,0,0,0};
                dataPack = new DatagramPacket(receiveData,receiveData.length,local,port);
                while(isReceive&&Count < 10){
                    try {
                        udpSocket.send(dataPack);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (isReceive){
                    Message msg=new Message();
                    msg.what=17;
                    Bundle bundle=new Bundle();
                    bundle.putInt("result",REPLAY_DEVICE_SUCCESS);
                    bundle.putString("deviceId",contactId);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (udpSocket!=null){
                    udpSocket.close();
                    udpSocket = null;
                }
            }
        }
    }

    public void openThread(){
        isSend = true;
        if (null == sendThread || !sendThread.isAlive()){
            sendThread = new SendThread();
            sendThread.start();
        }
    }

    public void stopSend(){
        isSend = false;
        sendThread = null;
    }

    private void MulticastLock(){
        if (this.lock!=null){
            try {
                this.lock.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void MulticastUnLock(){
        if (this.lock!=null){
            try {
                this.lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopListen(){
        isListn = false;
        if (null!=datagramSocket){
            datagramSocket.close();
            datagramSocket = null;
        }
    }

}
