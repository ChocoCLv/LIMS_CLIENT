package com.choco.limsclient.Util;

/**
 * Created by choco on 2017/4/17.
 */

public class StringParseHelper {
    public static String getDeviceIdFromDeviceInfo(String deviceInfo){
        String[] res = deviceInfo.split("\\n");
        String deviceId = res[3].split("：")[1];
        return deviceId;
    }
}
