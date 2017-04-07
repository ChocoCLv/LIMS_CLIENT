package com.choco.limsclient.CommModule;

/**
 * Created by choco on 2017/2/28.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.choco.limsclient.Config.Global;

/**
 * Created by choco on 2017/1/23.
 */

public class CommThread implements Runnable{
    private static CommThread commThread = null;
    public Handler rcvHandler;
    private Handler handler;
    private InetAddress svrAddress;
    private DatagramPacket sndPacket;
    private DatagramPacket inPacket;
    private DatagramSocket socket;
    private static final int svrPort = Global.svrPort;
    private static final int localPort = Global.localPort;


    private CommThread(){
        try {
            Log.i("login","create socket");
            svrAddress = InetAddress.getByName(Global.svrAddr);
            socket = new DatagramSocket(localPort);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setHandler(Handler handler){
        this.handler = handler;
        Log.i("login","get handler");
    }

    public static CommThread getInstance(){
        if(commThread == null){
            commThread = new CommThread();
        }
        Log.i("login","get instance");
        return commThread;
    }

    public void run(){
        try
        {
            //启动新线程来读取服务器的数据
            new Thread(){
                @Override
                public void run(){
                    Log.i("login","recv thread start");
                    byte[] data = new byte[Global.MAX_LENGTH];
                    inPacket = new DatagramPacket(data,data.length);
                    while(true){
                        try{
                            socket.receive(inPacket);
                            Log.i("login","recv response from server");
                            String resp = new String(inPacket.getData() , inPacket.getOffset() , inPacket.getLength());
                            Message msg = new Message();
                            msg.what = Global.FROM_COMMTHREAD;
                            msg.obj = resp;
                            handler.sendMessage(msg);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            Looper.prepare();
            Log.i("login","thread start");
            rcvHandler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == Global.FROM_UITHREAD){
                        Log.i("login","get message from ui thread");
                        sndPacket = new DatagramPacket(msg.obj.toString().getBytes(),
                                msg.obj.toString().length(),svrAddress,svrPort);
                        try{
                            socket.send(sndPacket);
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
