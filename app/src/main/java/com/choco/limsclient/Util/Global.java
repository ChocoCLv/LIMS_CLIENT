package com.choco.limsclient.Util;

import java.io.File;

/**
 * Created by choco on 2017/2/28.
 */

public interface Global {
    String svrAddr = "192.168.1.222";
    int svrPort = 2222;
    int localPort = 3333;
    int FROM_LOGINACTIVITY = 0X001;
    int FROM_COMMTHREAD = 0x002;
    int FROM_LABADMIN_LENDDEVICE = 0x0003;
    int FROM_LADADMIN_UPDATEDEVICEINFO = 0x0004;
    int FROM_STUDENT_BORROWDEVICE = 0x0005;
    int FROM_TEACHER_PUBLISHEEXPERIMENT = 0x0006;
    int FROM_STUDENT_CHECK_PROJECT = 0x0007;
    int FROM_STUDENT_PROJECT_INFO = 0x0010;
    int MAX_LENGTH = 4 * 1024;  //配置通信模块发送的最大数据包的大小

    int FACE_REGISTER = 0x0008;
    int FACE_VERIFY = 0x0009;

    String APP_DIR = File.separator + "LIMS" + File.separator;
    String DEVICE_PHOTOS = "device photos" + File.separator;
    CurrentUserInfo userInfo = CurrentUserInfo.getInstance();
}
