package com.myappiumproject.tests;

import okhttp3.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SmsSender {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String URL = "https://smp-api.seedlingintl.com/app-api/app/auth/send-sms-code";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // 所有要发送的手机号
    private static final List<String> phoneNumbers = Arrays.asList("19523303551","13671260144","13811259792","18600657063",
            "13931458554","17777786100","13720033241","13716838345","18001134726","18500750097","17701335552","18600390929",
            "13311391918","15110118956","18963021991");


    public static void main(String[] args) {
        int rounds = 4;
        for (int round = 1; round <= rounds; round++) {
            System.out.println("开始第 " + round + " 轮发送");
            for (String mobile : phoneNumbers) {
                sendSms(mobile);
                try {
                    TimeUnit.MILLISECONDS.sleep(3000); // 每个手机号间隔 3.5 秒
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (round < rounds) {
                System.out.println("等待 90 分钟进行下一轮...");
                try {
                    TimeUnit.MINUTES.sleep(90); // 每轮之间间隔 90 分钟
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        System.out.println("所有短信验证码发送完成！");
    }

    private static void sendSms(String mobile) {
        String jsonBody = "{\"mobile\": \"" + mobile + "\", \"scene\": 1}";
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println("发送失败：" + mobile + " 错误：" + e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println("短信已发送成功：" + mobile);
                } else {
                    System.out.println("短信发送失败：" + mobile + " 状态码：" + response.code());
                }
            }
        });
    }
}
