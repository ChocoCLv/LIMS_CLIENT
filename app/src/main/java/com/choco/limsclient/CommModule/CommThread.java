package com.choco.limsclient.CommModule;

/**
 * Created by choco on 2017/2/28.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.choco.limsclient.Util.Global;

/**
 * Created by choco on 2017/1/23.
 */

public class CommThread implements Runnable{
    private static final CommThread commThread = new CommThread();
    public Handler rcvHandler;
    private Handler handler;
    private InetAddress svrAddress;
    private DatagramPacket packet;
    private DatagramSocket socket;
    private static final int svrPort = Global.svrPort;


    private CommThread(){
        try {
            svrAddress = InetAddress.getByName(Global.svrAddr);
            socket = new DatagramSocket();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setHandler(Handler handler){
        this.handler = handler;
        Log.i("login","get handler");
    }

    public static CommThread getInstance(){
        Log.i("login","get instance");
        return commThread;
    }

    public void run(){
        try
        {
            Looper.prepare();
            Log.i("login","thread start");
            rcvHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == Global.FROM_UITHREAD){
                        Log.i("login","get message from ui thread");
                        packet = new DatagramPacket(msg.obj.toString().getBytes(),
                                msg.obj.toString().length(),svrAddress,svrPort);
                        try{
                            socket.send(packet);
                            Log.i("login","send message to server");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
