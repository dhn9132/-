package com.dhnsoft.testandroid.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpURLConnectionHelper {

    public static String sendRequest(String urlParam) {

        URLConnection con = null;

        BufferedReader buffer = null;
        StringBuffer resultBuffer = null;

        try {
            URL url = new URL(urlParam);
            con = url.openConnection();

            //设置请求需要返回的数据类型和字符集类型
            con.setRequestProperty("Content-Type", "application/json;charset=GBK");
            //这是使用post请求
            /*//允许写出
            con.setDoOutput(true);
            //允许读入
            con.setDoInput(true);*/
            //不使用缓存
            con.setUseCaches(false);
            //使用get请求
            con.connect();
            //得到响应流
            InputStream inputStream = con.getInputStream();
            System.out.println(inputStream);
            //将响应流转换成字符串
            resultBuffer = new StringBuffer();
            String line;
            buffer = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            while ((line = buffer.readLine()) != null) {
                resultBuffer.append(line);
            }
            return resultBuffer.toString();

        }catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
