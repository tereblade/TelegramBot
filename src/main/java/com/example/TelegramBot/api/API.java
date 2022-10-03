package com.example.TelegramBot.api;

import com.example.TelegramBot.api.model.FileDownloadResponseHandler;
import com.example.TelegramBot.api.model.Response;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class API {
    protected final CloseableHttpClient httpClient = HttpClients.createDefault();
    protected final String BASE_URL = "https://api.opendota.com/api/";

    @SneakyThrows
    protected Response doGet(String url, Map<String, String> headers) {
        HttpGet request = new HttpGet(url);
        headers.forEach(request::setHeader);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            return new Response(EntityUtils.toString(responseEntity), response.getStatusLine().getStatusCode());
        }
    }

    protected Response doGet(String url) {
        return doGet(url, new HashMap<>());
    }

    protected Response doPostCustomHeaders(String url, String body, Map<String, String> headers) throws IOException {
        HttpPost request = new HttpPost(url);
        headers.forEach(request::setHeader);
        return sendRequestWithBody(request, body);
    }

    protected Response doPost(String url, String body) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        return sendRequestWithBody(request, body);
    }

    protected File doPost(String url, String dataRaw, File dstFile) throws IOException {
        HttpPost request = new HttpPost(url);
        StringEntity entity = new StringEntity(dataRaw);
        request.setEntity(entity);
        request.setHeader("Content-Type", "application/json");
        return httpClient.execute(request, new FileDownloadResponseHandler(dstFile));
    }

    private <T extends HttpEntityEnclosingRequestBase> Response sendRequestWithBody(T request, String body) throws IOException {
        StringEntity entity = new StringEntity(body, Charset.defaultCharset());
        request.setEntity(entity);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null)
                return new Response(EntityUtils.toString(responseEntity), response.getStatusLine().getStatusCode());
            else
                return new Response("", response.getStatusLine().getStatusCode());
        }
    }

    protected String generateQueryParam(Map<String, String> params) {
        StringBuilder queryParams = new StringBuilder();
        for (String key : params.keySet()) {
            queryParams.append(String.join("=", key, params.get(key)));
            if (params.keySet().iterator().hasNext())
                queryParams.append("&");
        }
        return String.valueOf(queryParams);
    }

    public static String getEncodedValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
