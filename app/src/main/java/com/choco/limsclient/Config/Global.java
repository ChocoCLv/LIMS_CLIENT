package com.choco.limsclient.Config;

/**
 * Created by choco on 2017/2/28.
 */

public interface Global {
    String svrAddr = "192.168.1.155";
    int svrPort = 2222;
    int localPort = 3333;
    int FROM_LOGINACTIVITY = 0X001;
    int FROM_COMMTHREAD = 0x002;
    int FROM_LABADMIN_LENDDEVICE = 0x0003;
    int MAX_LENGTH = 4 * 1024;
}
