package com.myappiumproject.tests;

import okhttp3.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SmsSender {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String URL = "https://smp-api.seedlingintl.com/app-api/app/auth/send-sms-code";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // 所有要发送的手机号
//    private static final List<String> phoneNumbers = Arrays.asList("19523303551","13671260144","13811259792","18600657063",
//            "13931458554","17777786100","13720033241","13716838345","18001134726","18500750097","17701335552","18600390929",
//            "13311391918","15110118956","18963021991");

    private static final List<String> phoneNumbers = Arrays.asList("19523303551","17274802003");

    // 配置参数
    private static final int ROUNDS = 2;                 // 轮数
    private static final boolean PARALLEL = true;       // 是否并发模式，true = 并发，false = 顺序
    private static final int CONCURRENCY = 50;           // 并发线程数（仅在并发模式下有效）
    private static final int SLEEP_BETWEEN_EACH_MS = 3000; // 顺序模式时两个手机号间的间隔
    private static final int INTERVAL_MINUTES = 90;      // 每轮间隔分钟

    public static void main(String[] args) throws InterruptedException {
        if (PARALLEL) {
            runParallel();
        } else {
            runSequential();
        }
    }

    // 顺序模式
    private static void runSequential() throws InterruptedException {
        for (int round = 1; round <= ROUNDS; round++) {
            System.out.println("🔁 第 " + round + " 轮 - 顺序发送开始");
            for (String mobile : phoneNumbers) {
                sendSms(mobile);
                TimeUnit.MILLISECONDS.sleep(SLEEP_BETWEEN_EACH_MS);
            }
            if (round < ROUNDS) {
                System.out.println("⏱️ 等待 " + INTERVAL_MINUTES + " 分钟进入下一轮...");
                TimeUnit.MINUTES.sleep(INTERVAL_MINUTES);
            }
        }
        System.out.println("✅ 所有短信验证码发送完成！");
    }

    // 并发模式
    private static void runParallel() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENCY);

        for (int round = 1; round <= ROUNDS; round++) {
            System.out.println("🔁 第 " + round + " 轮 - 并发发送开始");
            CountDownLatch latch = new CountDownLatch(phoneNumbers.size());

            for (String mobile : phoneNumbers) {
                executor.submit(() -> {
                    try {
                        sendSms(mobile);
                        TimeUnit.MILLISECONDS.sleep(100); // 稍微防止服务端过载
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            System.out.println("✅ 第 " + round + " 轮发送完成");

            if (round < ROUNDS) {
                System.out.println("⏱️ 等待 " + INTERVAL_MINUTES + " 分钟进入下一轮...");
                TimeUnit.MINUTES.sleep(INTERVAL_MINUTES);
            }
        }

        executor.shutdown();
        System.out.println("✅ 所有短信验证码发送完成！");
    }

    private static void sendSms(String mobile) {
        String jsonBody = "{\"mobile\": \"" + mobile + "\", \"scene\": 1}";
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("✅ 成功发送验证码至：" + mobile);
            } else {
                System.out.println("❌ 发送失败：" + mobile + "，状态码：" + response.code());
            }
        } catch (IOException e) {
            System.out.println("❌ 异常发送失败：" + mobile + "，错误：" + e.getMessage());
        }
    }
}
