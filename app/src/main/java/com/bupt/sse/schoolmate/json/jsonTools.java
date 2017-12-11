package com.bupt.sse.schoolmate.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sai on 2017/12/10.
 */

public class jsonTools {
    public static List<Map<String,String>> parseFriendsList(String jsonString){
        List<Map<String,String>> result=new ArrayList<Map<String,String>>();
        try {
            JSONObject json=new JSONObject(jsonString);
            boolean resultCode=json.getBoolean("result");
            if (resultCode){
                JSONArray friendsList=json.getJSONArray("users");
                for(int i=0;i<friendsList.length();i++){
                    Map<String,String> aFriend=new HashMap<String,String>();
                    JSONObject item=friendsList.getJSONObject(i);
                    aFriend.put("id",item.getString("id"));
                    aFriend.put("username",item.getString("username"));
                    aFriend.put("name",item.getString("name"));
                    aFriend.put("city",item.getString("city"));
                    result.add(aFriend);
                }
                return result;
            }else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String,String> parseFriendDetail(String jsonString){
        Map<String,String> result=new HashMap<String,String>();
        try {
            JSONObject json=new JSONObject(jsonString);
            boolean resultCode=json.getBoolean("result");
            if(resultCode){
                JSONObject infoJson=json.getJSONObject("userInfo");
                result.put("id",infoJson.getString("id"));
                result.put("username",infoJson.getString("username"));
                result.put("name",infoJson.getString("name"));
                result.put("sex",infoJson.getString("sex"));
                result.put("phone",infoJson.getString("phone"));
                result.put("city",infoJson.getString("city"));
                result.put("location",infoJson.getString("location"));
                return result;
            }else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String parseDelete(String jsonString){
        String result=null;
        try {
            JSONObject json=new JSONObject(jsonString);
            result=json.getString("result");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
