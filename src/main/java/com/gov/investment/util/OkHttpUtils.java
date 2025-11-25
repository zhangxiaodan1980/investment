package com.gov.investment.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 发送GET请求
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应结果
     */
    public static String get(String url, Map<String, String> headers) {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                logger.error("GET请求失败，URL: {}, 状态码: {}", url, response.code());
                return null;
            }
        } catch (IOException e) {
            logger.error("GET请求异常，URL: {}", url, e);
            return null;
        }
    }

    /**
     * 发送POST请求
     * @param url 请求URL
     * @param headers 请求头
     * @param body 请求体
     * @return 响应结果
     */
    public static String post(String url, Map<String, String> headers, String body) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(body, mediaType);
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                logger.error("POST请求失败，URL: {}, 状态码: {}", url, response.code());
                return null;
            }
        } catch (IOException e) {
            logger.error("POST请求异常，URL: {}", url, e);
            return null;
        }
    }

    /**
     * 发送POST请求（表单提交）
     * @param url 请求URL
     * @param headers 请求头
     * @param formParams 表单参数
     * @return 响应结果
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> formParams) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formParams != null && !formParams.isEmpty()) {
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = formBodyBuilder.build();
        Request.Builder builder = new Request.Builder().url(url).post(requestBody);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                logger.error("POST表单请求失败，URL: {}, 状态码: {}", url, response.code());
                return null;
            }
        } catch (IOException e) {
            logger.error("POST表单请求异常，URL: {}", url, e);
            return null;
        }
    }
}