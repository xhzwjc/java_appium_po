package com.myappiumproject.tests;

import com.myappiumproject.data.EmailSender;
import okhttp3.*;
import com.google.gson.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ExamProgressMonitor {
    private final EmailSender emailSender;

    private static final List<String> ALERT_RECIPIENTS = Arrays.asList(
            "w2983423303@163.com",
            "2983423303@qq.com",
            "handsomexhzjc@gmail.com"
    );


    // 预期响应JSON（格式化后的完整响应）
    private static final String EXPECTED_RESPONSE_JSON =
            "{\n" +
                    "    \"total\":1,\n" +
                    "    \"rows\":[\n" +
                    "        {\n" +
                    "            \"id\":\"60737\",\n" +
                    "            \"examTypeCode\":\"1\",\n" +
                    "            \"examTypeName\":\"取证\",\n" +
                    "            \"personTypeCode\":\"03\",\n" +
                    "            \"personTypeName\":\"特种作业人员\",\n" +
                    "            \"industryTypeCode\":\"0319\",\n" +
                    "            \"industryTypeName\":\"有限空间作业\",\n" +
                    "            \"operateItemCode\":\"031901\",\n" +
                    "            \"operateItemName\":\"地下有限空间监护作业\",\n" +
                    "            \"signUpType\":\"2\",\n" +
                    "            \"signUpTime\":\"2025-04-29\",\n" +
                    "            \"progressStatus\":\"1\",\n" +
                    "            \"conditionAuditStatus\":\"2\",\n" +
                    "            \"qualifiedAuditStatus\":\"1\",\n" +
                    "            \"trainClassId\":5736,\n" +
                    "            \"trainStudentId\":82627,\n" +
                    "            \"planId\":\"41\",\n" +
                    "            \"planName\":\"2025年5月特种作业考试\",\n" +
                    "            \"planYearMonth\":\"2025-05\",\n" +
                    "            \"examSTime\":\"2025-05-19T00:00:00.000+08:00\",\n" +
                    "            \"examETime\":\"2025-05-24T00:00:00.000+08:00\",\n" +
                    "            \"conditionReviewETime\":\"2025-05-09\",\n" +
                    "            \"attachReviewETime\":null,\n" +
                    "            \"releaseTime\":\"2025-04-28\",\n" +
                    "            \"originalExamineeId\":null,\n" +
                    "            \"checkoutTime\":null,\n" +
                    "            \"permitModify\":\"1\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"code\":200,\n" +
                    "    \"msg\":\"查询成功\"\n" +
                    "}";

    private final OkHttpClient client;
    private final Gson gson = new GsonBuilder().setLenient().create();
    private final JsonObject expectedJson;

    public ExamProgressMonitor() throws Exception {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        // 解析预期JSON为JsonObject用于后续比较
        this.expectedJson = JsonParser.parseString(EXPECTED_RESPONSE_JSON).getAsJsonObject();

        // 初始化邮件发送器(替换为您的实际邮箱和授权码)
        this.emailSender = new EmailSender(
                "w2983423303@163.com",
                "YAeZnz3vaKgKzfby"
        );
    }

    public void startMonitoring() {
        System.out.println("开始监控考试进度API，随机间隔10-30秒");

        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 1. 执行API请求
                String responseJson = fetchExamProgress();

                // 2. 比较响应结果
                if (!compareJson(responseJson)) {
                    triggerAlert("API响应不匹配！\n当前响应:\n" + responseJson);
                } else {
                    System.out.println("[" + System.currentTimeMillis() + "] 检查通过，响应符合预期");
                }

                // 3. 生成随机间隔时间（10-30秒）
                int nextInterval = 60 + ThreadLocalRandom.current().nextInt(41);
                System.out.println("下次检查将在 " + nextInterval + " 秒后执行");
                TimeUnit.SECONDS.sleep(nextInterval);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("监控线程被中断");
            } catch (Exception e) {
                triggerAlert("监控过程中发生异常: " + e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(5); // 异常后固定等待5秒
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private String fetchExamProgress() throws IOException {
        String url = "http://hd.yjglj.beijing.gov.cn/stage-api/exam/examineebj/examExamineeProgressPage/web?type=1&idCard=" +
                URLEncoder.encode("500237200308190399", StandardCharsets.UTF_8) +
                "&name=" + URLEncoder.encode("王京川", StandardCharsets.UTF_8) +
                "&pageNum=1&pageSize=10";

        Request request = new Request.Builder()
                .url(url)
                .header("accept", "application/json, text/plain, */*")
                .header("authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImIwYTA3NWQwLWZkZGYtNDI4OC05NzZlLTE0OTVkZTgyZDdmYyJ9.rz4fo7sB1tUvGocdZXYmGwVva9_wFnY7K3Yl1XXGSapirpHdYdVpKqsXpUydONn3yeZVmVddAuJwCyL8wrXyPw")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败: " + response.code());
            }
            return Objects.requireNonNull(response.body()).string();
        }
    }

    private boolean compareJson(String actualJson) {
        try {
            JsonObject actual = JsonParser.parseString(actualJson).getAsJsonObject();

            // 1. 先比较code和msg字段
            if (actual.get("code").getAsInt() != expectedJson.get("code").getAsInt() ||
                    !actual.get("msg").getAsString().equals(expectedJson.get("msg").getAsString())) {
                return false;
            }

            // 2. 比较整个JSON结构（忽略JSON格式差异）
            return gson.toJson(actual).equals(gson.toJson(expectedJson));

        } catch (Exception e) {
            return false; // 任何解析异常视为不匹配
        }
    }

    private void triggerAlert(String message) {
        // 实现你的告警机制，这里用System.err模拟
        System.err.println("\u001B[31m" + "❌ 告警: " + message + "\u001B[0m");
        // 实际项目中可以加入:
        // 1. 发送邮件
        // 2. 发送短信
        // 3. 调用Webhook
        // 4. 记录到日志系统
        try {
            // 发送邮件告警
            emailSender.sendAlertEmail(
                    ALERT_RECIPIENTS,  // 接收邮箱
                    "【重要】考试进度API监控告警",
                    message
            );
            System.out.println("告警邮件已发送至" + ALERT_RECIPIENTS.size() + "个收件人");
        } catch (Exception e) {
            System.err.println("发送邮件失败: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        ExamProgressMonitor monitor = new ExamProgressMonitor();
        monitor.startMonitoring();
    }
}