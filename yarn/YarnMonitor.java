package com.redoop.monitor.cdh.yarn;


import com.google.gson.*;
import com.redoop.monitor.cdh.utils.HttpClientResult;
import com.redoop.monitor.cdh.utils.HttpClientUtils;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.util.Map;

@RestController
public class YarnMonitor {
    static final String pushGatewayUrl = "10.7.137.93:9091";
    static final PushGateway pg = new PushGateway(pushGatewayUrl);
    static final CollectorRegistry registry = new CollectorRegistry();
    static final Gauge appsRunning = Gauge.build().name("yarn_appsRunning").help("yarn_appsRunning").register(registry);
    static final Gauge appsSubmitted = Gauge.build().name("yarn_appsSubmitted").help("yarn_appsSubmitted").register(registry);
    static final Gauge appsPendingd = Gauge.build().name("yarn_appsPendingd").help("yarn_appsPendingd").register(registry);
    static final Gauge appsFailed = Gauge.build().name("yarn_appsFailed").help("yarn_appsFailed").register(registry);
    static final Gauge appsKilled= Gauge.build().name("yarn_appsKilled").help("yarn_appsKilled").register(registry);
    static final Gauge appsCompleted = Gauge.build().name("yarn_appsCompleted").help("yarn_appsCompleted").register(registry);


    @Scheduled(cron="0/1 * * * * ?")
    @RequestMapping("/getYarnGauge")
    public void getHbaseGauge() throws Exception {
        HttpClientResult result = HttpClientUtils.doGet("http://10.8.190.1:8088/jmx");

        JsonObject jsonObject = new JsonParser().parse(result.getContent()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("beans");
        Gson gson = new Gson();

        double appsRunningvalue = 0;
        for (JsonElement jsonElement : jsonArray) {
            Map map = gson.fromJson(jsonElement, Map.class);
            Object appsRunning = map.get("AppsRunning");
            if(appsRunning != null){
                appsRunningvalue += Double.parseDouble(appsRunning.toString());
            }
        }
        appsRunning.set(appsRunningvalue);

        double AppsSubmittedValue = 0;
        for (JsonElement jsonElement : jsonArray) {
            Map map = gson.fromJson(jsonElement, Map.class);
            Object appsRunning = map.get("AppsSubmitted");
            if(appsRunning != null){
                AppsSubmittedValue += Double.parseDouble(appsRunning.toString());
            }
        }
        appsSubmitted.set(AppsSubmittedValue);

        double AppsPendingdValue = 0;
        for (JsonElement jsonElement : jsonArray) {
            Map map = gson.fromJson(jsonElement, Map.class);
            Object appsRunning = map.get("AppsPending");
            if(appsRunning != null){
                AppsPendingdValue += Double.parseDouble(appsRunning.toString());
            }
        }
        appsPendingd.set(AppsPendingdValue);

        double AppsCompletedValue = 0;
        for (JsonElement jsonElement : jsonArray) {
            Map map = gson.fromJson(jsonElement, Map.class);
            Object appsRunning = map.get("AppsCompleted");
            if(appsRunning != null){
                AppsCompletedValue += Double.parseDouble(appsRunning.toString());
            }
        }
        appsCompleted.set(AppsCompletedValue);

        double AppsKilledValue = 0;
        for (JsonElement jsonElement : jsonArray) {
            Map map = gson.fromJson(jsonElement, Map.class);
            Object appsRunning = map.get("AppsKilled");
            if(appsRunning != null){
                AppsKilledValue += Double.parseDouble(appsRunning.toString());
            }
        }
        appsKilled.set(AppsKilledValue);

        double AppsFailedValue = 0;
        for (JsonElement jsonElement : jsonArray) {
            Map map = gson.fromJson(jsonElement, Map.class);
            Object appsRunning = map.get("AppsFailed");
            if(appsRunning != null){
                AppsFailedValue += Double.parseDouble(appsRunning.toString());
            }
        }
        appsFailed.set(AppsFailedValue);

        pg.pushAdd(registry, "yarn");
    }
}
