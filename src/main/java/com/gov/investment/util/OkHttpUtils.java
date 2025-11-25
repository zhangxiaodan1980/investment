package com.gov.investment.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    private static final OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 发送GET请求
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("GET请求失败，URL: {}", url);
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * 发送POST请求，请求体为JSON
     * @param url 请求URL
     * @param json 请求体JSON字符串
     * @param headers 请求头
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String postJson(String url, String json, Map<String, String> headers) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, mediaType);
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("POST请求失败，URL: {}", url);
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * 发送POST请求，请求体为表单
     * @param url 请求URL
     * @param formParams 表单参数
     * @param headers 请求头
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String postForm(String url, Map<String, String> formParams, Map<String, String> headers) throws IOException {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (formParams != null && !formParams.isEmpty()) {
            for (Map.Entry<String, String> entry : formParams.entrySet()) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = formBodyBuilder.build();
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("POST表单请求失败，URL: {}", url);
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * 发送PUT请求，请求体为JSON
     * @param url 请求URL
     * @param json 请求体JSON字符串
     * @param headers 请求头
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String putJson(String url, String json, Map<String, String> headers) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, mediaType);
        Request.Builder builder = new Request.Builder().url(url).put(body);
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("PUT请求失败，URL: {}", url);
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }

    /**
     * 发送DELETE请求
     * @param url 请求URL
     * @param headers 请求头
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String delete(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url).delete();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("DELETE请求失败，URL: {}", url);
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            return responseBody != null ? responseBody.string() : null;
        }
    }
}