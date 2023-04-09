package com.example.mymvvmmovieapplicationjava;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, String> getHeadersMap(){
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer 89a571acaf96541bdee2b19060fc9980");
        return headers;
    }

    public static String getLanguage(){
        return "en-US";
    }
}
