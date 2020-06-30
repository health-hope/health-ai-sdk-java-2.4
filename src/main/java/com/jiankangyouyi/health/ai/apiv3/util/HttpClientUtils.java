package com.jiankangyouyi.health.ai.apiv3.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http Client 工具类
 *
 * @author yangsongbo
 */
public class HttpClientUtils {


    /**
     * 默认字符集
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";

    /**
     * 默认连接超时时间，毫秒
     */
    private static final int DEFAULT_CONNECT_TIME_OUT = 30000;


    /**
     * Get 请求
     *
     * @param url 请求链接，不能为空
     * @return 请求结果
     */
    public static String get(String url) {
        return get(url, null, null, null);
    }

    /**
     * Get 请求
     *
     * @param url            请求链接，不能为空
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String get(String url, Integer connectTimeout) {
        return get(url, null, null, connectTimeout);
    }

    /**
     * Get 请求
     *
     * @param url    请求链接，不能为空
     * @param params 请求参数，可以为空
     * @return 请求结果
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, params, null, null);
    }

    /**
     * Get 请求
     *
     * @param url            请求链接，不能为空
     * @param params         请求参数，可以为空
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String get(String url, Map<String, String> params, Integer connectTimeout) {
        return get(url, params, null, connectTimeout);
    }


    /**
     * POST请求，Content-Type 使用默认的application/x-www-form-urlencoded
     *
     * @param url            请求URL，不能为空
     * @param params         请求参数，可以为空
     * @param headers        Header参数，可以为空
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String post(
            String url, Map<String, String> params, Map<String, String> headers,
            Integer connectTimeout) {

        return post(url, params, headers, connectTimeout, null);
    }


    /**
     * POST请求，Content-Type 使用默认的application/x-www-form-urlencoded
     *
     * @param url    请求URL，不能为空
     * @param params 请求参数，可以为空
     * @return 请求结果
     */
    public static String post(String url, Map<String, String> params) {

        return post(url, params, null, null, null);
    }

    /**
     * POST请求，Content-Type 使用默认的application/x-www-form-urlencoded
     *
     * @param url            请求URL，不能为空
     * @param params         请求参数，可以为空
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String post(
            String url, Map<String, String> params, Integer connectTimeout) {
        return post(url, params, null, connectTimeout, null);
    }


    /**
     * POST 请求
     *
     * @param url            请求URL，不能为空
     * @param param          请求参数，不能为null
     * @param headers        Header参数，可以为空
     * @param contentType    Http Content-Type
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String post(
            String url, String param, Map<String, String> headers,
            String contentType, Integer connectTimeout) {

        return post(url, param, headers, contentType, connectTimeout, null);
    }


    /**
     * POST 请求
     *
     * @param url            请求URL，不能为空
     * @param param          请求参数，不能为null
     * @param contentType    Http Content-Type
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String post(
            String url, String param, String contentType, Integer connectTimeout) {

        return post(url, param, null, contentType, connectTimeout, null);
    }


    /**
     * POST 请求
     *
     * @param url         请求URL，不能为空
     * @param param       请求参数，不能为null
     * @param contentType Http Content-Type
     * @return 请求结果
     */
    public static String post(
            String url, String param, String contentType) {
        return post(url, param, null, contentType, null, null);
    }

    /**
     * POST请求
     *
     * @param url     请求URL，不能为空
     * @param param   请求参数，不能为null
     * @param headers Header参数，可以为空
     * @return 请求结果
     */
    public static String post(
            String url, String param, Map<String, String> headers) {
        return post(url, param, headers, null, null, null);
    }


    public static SSLContext getSSLContext(InputStream input, String password) {
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            char[] keyPassword = password.toCharArray();
            keystore.load(input, keyPassword);
            return SSLContexts.custom().loadKeyMaterial(keystore, keyPassword).build();
        } catch (Exception e) {
            throw new RuntimeException("获取SSL Context 异常");
        }
    }


    /**
     * Get请求
     *
     * @param url            请求地址,不能为空
     * @param params         请求参数，可以为空
     * @param headers        header参数，可以为空
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @return 请求结果
     */
    public static String get(
            String url, Map<String, String> params, Map<String, String> headers, Integer connectTimeout) {


        isTrue(url != null && !"".equals(url.trim()),
                "this url must have text; it must not be null, empty, or blank");

        return executeReturnString(null, buildHttpGet(url, params, headers), connectTimeout);
    }


    /**
     * POST请求，Content-Type 使用默认的application/x-www-form-urlencoded
     *
     * @param url            请求URL，不能为空
     * @param params         请求参数，可以为空
     * @param headers        Header参数，可以为空
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @param sslContext     加密套接字协议层
     * @return 请求结果
     */
    public static String post(
            String url, Map<String, String> params, Map<String, String> headers,
            Integer connectTimeout, SSLContext sslContext) {

        isTrue(url != null && !"".equals(url.trim()),
                "this url must have text; it must not be null, empty, or blank");


        return executeReturnString(sslContext, buildHttpPost(url, params, headers), connectTimeout);
    }

    /**
     * POST 请求
     *
     * @param url            请求URL，不能为空
     * @param param          请求参数，不能为null
     * @param headers        Header参数，可以为空
     * @param contentType    Http Content-Type
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @param sslContext     加密套接字协议层
     * @return 请求结果
     */
    public static String post(
            String url, String param, Map<String, String> headers,
            String contentType, Integer connectTimeout, SSLContext sslContext) {

        isTrue(url != null && !"".equals(url.trim()),
                "this url must have text; it must not be null, empty, or blank");

        isTrue(param != null,
                "this params is required; it must not be null");

        return executeReturnString(sslContext, buildHttpPost(url, param, headers, contentType), connectTimeout);
    }


    /**
     * POST 请求
     *
     * @param url            请求URL，不能为空
     * @param param          请求参数，不能为null
     * @param headers        Header参数，可以为空
     * @param contentType    Http Content-Type
     * @param connectTimeout 连接超时时间,毫秒，为空使用默认连接超时时间
     * @param sslContext     加密套接字协议层
     * @return 请求结果
     */
    public static HttpResult postReturnByteArray(
            String url, String param, Map<String, String> headers,
            String contentType, Integer connectTimeout, SSLContext sslContext) {

        isTrue(url != null && !"".equals(url.trim()),
                "this url must have text; it must not be null, empty, or blank");

        isTrue(param != null,
                "this params is required; it must not be null");


        return executeReturnByteArray(sslContext, buildHttpPost(url, param, headers, contentType), connectTimeout);
    }

    private static HttpPost buildHttpPost(String url, String param, Map<String, String> headers, String contentType) {
        HttpPost httpPost = new HttpPost(url);

        if (contentType != null && contentType.length() > 0) {
            httpPost.setHeader("Content-Type", contentType);
        }
        httpPost.setEntity(new StringEntity(param, DEFAULT_CHARSET));

        /*填充Header*/
        setHeaders(httpPost, headers);

        return httpPost;
    }


    private static HttpPost buildHttpPost(String url, Map<String, String> params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        if (params != null && !params.isEmpty()) {
            for (Entry<String, String> entry : params.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        /*填充Header*/
        setHeaders(httpPost, headers);

        return httpPost;
    }


    private static void setHeaders(HttpUriRequest request, Map<String, String> headers) {
        /*填充Header*/
        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }


    private static HttpGet buildHttpGet(String url, Map<String, String> params, Map<String, String> headers) {
        if (params != null && !params.isEmpty()) {
            StringBuilder param = new StringBuilder();
            // 是否开始
            boolean flag = true;
            for (Entry<String, String> entry : params.entrySet()) {
                if (flag && !url.contains("?")) {
                    param.append("?");
                    flag = false;
                } else {
                    param.append("&");
                }
                param.append(entry.getKey()).append("=");
                try {
                    param.append(URLEncoder.encode(entry.getValue(), DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            url += param.toString();
        }

        HttpGet httpGet = new HttpGet(url);

        /*填充Header*/
        setHeaders(httpGet, headers);

        return httpGet;
    }


    private static String executeReturnString(
            SSLContext sslContext, HttpUriRequest request, Integer connectTimeout) {


        try (
                CloseableHttpClient httpClient = buildCloseableHttpClient(sslContext, connectTimeout);
                CloseableHttpResponse response = httpClient.execute(request)
        ) {
            return EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static HttpResult executeReturnByteArray(
            SSLContext sslContext, HttpUriRequest request, Integer connectTimeout) {

        try (
                CloseableHttpClient httpClient = buildCloseableHttpClient(sslContext, connectTimeout);
                CloseableHttpResponse response = httpClient.execute(request)
        ) {

            return new HttpResult(
                    response.getEntity().getContentType().getValue(), EntityUtils.toByteArray(response.getEntity()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static CloseableHttpClient buildCloseableHttpClient(SSLContext sslContext, Integer connectTimeout) {

        HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(
                RequestConfig.custom().setConnectTimeout(
                        connectTimeout == null ? DEFAULT_CONNECT_TIME_OUT : connectTimeout).build()

        );
        if (sslContext != null) {
            httpClientBuilder.setSSLSocketFactory(getSSLConnectionSocket(sslContext));
        }
        return httpClientBuilder.build();
    }


    private static SSLConnectionSocketFactory getSSLConnectionSocket(SSLContext sslContext) {
        return new SSLConnectionSocketFactory(
                sslContext, new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"}, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    private static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }


    /**
     * Http result byte array
     */
    public static class HttpResult {

        private String contentType;

        private byte[] result;

        public HttpResult() {
        }

        public HttpResult(String contentType, byte[] result) {
            this.contentType = contentType;
            this.result = result;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public byte[] getResult() {
            return result;
        }

        public void setResult(byte[] result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "HttpResult{" +
                    "contentType='" + contentType + '\'' +
                    ", result=" + Arrays.toString(result) +
                    '}';
        }
    }

}
