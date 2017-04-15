package com.choco.limsclient.Config;

/**
 * Created by choco on 2017/2/28.
 */

public interface Global {
    String svrAddr = "192.168.1.105";
    int svrPort = 2222;
    int localPort = 3333;
    int FROM_LOGINACTIVITY = 0X001;
    int FROM_COMMTHREAD = 0x002;
    int FROM_LABADMIN_LENDDEVICE = 0x0003;
    int FROM_LADADMIN_UPDATEDEVICEINFO = 0x0004;
    int MAX_LENGTH = 4 * 1024;  //配置通信模块发送的最大数据包的大小
    CurrentUserInformation userInfo = CurrentUserInformation.getInstance();
}
