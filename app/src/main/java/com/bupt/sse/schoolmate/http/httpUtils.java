package com.bupt.sse.schoolmate.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by sai on 2017/12/10.
 */

public class httpUtils {
    public static String convertToString(Map<String,String> data){
        StringBuffer dataBuffer=new StringBuffer();
        for (String key:data.keySet()) {
            dataBuffer.append("&");
            dataBuffer.append(key);
            dataBuffer.append("=");
            dataBuffer.append(data.get(key));
        }
        dataBuffer.deleteCharAt(0);
        return dataBuffer.toString();
    }

    public static String sendPost(String path, Map<String,String> data){
        try {
            URL httpUrl=new URL(path);
            HttpURLConnection conn=(HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            OutputStream out=conn.getOutputStream();
            out.write(convertToString(data).getBytes());
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer result=new StringBuffer();
            String str;
            while((str=reader.readLine())!=null){
                result.append(str);
            }
            return result.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
