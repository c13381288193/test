package com.redoop.monitor.cdh.yarn;

import com.google.gson.*;
import com.redoop.monitor.cdh.utils.HttpClientResult;
import com.redoop.monitor.cdh.utils.HttpClientUtils;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class test {
    public static void main(String[] args) throws IOException {
        //读取配置文件
        FileInputStream fis = new FileInputStream("src/main/shell/dengxiaolong/conf/url_jmx.json");
        byte[] b = new byte[fis.available()];
        fis.read(b);
        fis.close();
        String str2 = new String(b);
        //gson将配置文件内容转换成map
        Gson gson = new Gson();
        JsonObject asJsonObject = new JsonParser().parse(str2).getAsJsonObject();
        String s = asJsonObject.toString();
        Map map = gson.fromJson(s, Map.class);
        Set set = map.keySet();
        //解析每一个value，将["http://test03:8050/metrics", "http://test04:8050/metrics", "http://test05:8050/metrics", "http://test06:8050/metrics"],
        //将每一个地址存储到arrlist中
        ArrayList<String> arr = new ArrayList<>();
        for (Object o : set) {
            Object value = map.get(o);

            if (value.toString().startsWith("[") & value.toString().endsWith("]")) {
                String substring = value.toString().substring(1, value.toString().length() - 1);
                String[] split = substring.split(",");
                for (String s1 : split) {
                    String trim = s1.trim();
                    arr.add(trim);
                }
            }
            else {
                String[] split = value.toString().split(",");
                for (String s1 : split) {
                    String trim = s1.trim();
                    arr.add(trim);
                }
            }
        }
        //读取每一个地址中数据，将其存储到本地

        for (String url : arr) {
            try {
                HttpClientResult result = HttpClientUtils.doGet(url);
                JsonObject jsonObject = new JsonParser().parse(result.getContent()).getAsJsonObject();
                if (jsonObject.getAsJsonArray("beans") != null){
                    double appsRunningvalue = 0;
                    for (JsonElement jsonElement : jsonObject.getAsJsonArray("beans")) {
                        Map mapURL = gson.fromJson(jsonElement, Map.class);
                        //Object appsRunning = mapURL.get("AppsRunning");
                        if (mapURL.get("AppsRunning")!=null){
                            System.out.println(url);
                        }
                       /* if(appsRunning != null){
                            appsRunningvalue += Double.parseDouble(appsRunning.toString());
                        }*/
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
