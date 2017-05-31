package com.choco.limsclient.Util;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by choco on 2017/4/17.
 */

public class Helper {

    public static List<String> convertJSONArrayToList(JSONArray ja){
        List<String> strList = new ArrayList<>();
        for(int i = 0;i<ja.length();i++){
            try {
                strList.add(ja.getString(i));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return strList;
    }


}
