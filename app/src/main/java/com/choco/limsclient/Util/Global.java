package com.choco.limsclient.Util;

/**
 * Created by choco on 2017/2/28.
 */

public interface Global {
    String svrAddr = "192.168.199.222";
    int svrPort = 2222;
    int localPort = 3333;
    int FROM_UITHREAD = 0X001;
    int FROM_COMMTHREAD =0x002;
    int MAX_LENGTH = 4*1024;
    enum LOGIN_STATUS{SUCCESS,FAIL};
}
