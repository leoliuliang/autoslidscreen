package com.mxkj.autoslidscreen.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JsonUtil {
    public static JSONArray getJStr(Context context) {
        try {
            AssetManager assetManager = context.getAssets(); //获得assets资源管理器（assets中的文件无法直接访问，可以使用AssetManager访问）
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open("packname.json"), "UTF-8"); //使用IO流读取json文件内容
            BufferedReader br = new BufferedReader(inputStreamReader);//使用字符高效流
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            inputStreamReader.close();

            JSONObject testJson = new JSONObject(builder.toString()); // 从builder中读取了json中的数据。
            // 直接传入JSONObject来构造一个实例
            JSONArray array = testJson.getJSONArray("pkgs");

            Log.e("pkgs", array.toString());
            return array;
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject jsonObject = array.getJSONObject(i);
//                String packagename = jsonObject.getString("packagename");
//                String toastStr = jsonObject.getString("toastStr");
//                Log.e("tag", "initData: " + packagename + toastStr);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
