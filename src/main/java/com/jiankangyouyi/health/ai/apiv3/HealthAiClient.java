package com.jiankangyouyi.health.ai.apiv3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jiankangyouyi.health.ai.apiv3.exception.AuthException;
import com.jiankangyouyi.health.ai.apiv3.request.ServiceRequest;
import com.jiankangyouyi.health.ai.apiv3.response.HealthAiData;
import com.jiankangyouyi.health.ai.apiv3.response.ServiceResponse;
import com.jiankangyouyi.health.ai.apiv3.response.TokenResponseData;
import com.jiankangyouyi.health.ai.apiv3.util.HttpClientUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用token访问API
 *
 * @author yangsongbo
 */
public class HealthAiClient {


    /**
     * token有效期
     */
    private Calendar expire;

    /**
     * 访问令牌
     */
    private String token;

    /**
     * APP ID
     */
    private String appId;

    /**
     * API Key
     */
    private String apiKey;

    /**
     * 请求Json字符串
     */
    private String requestJson;

    /**
     * 响应Json字符串
     */
    private String responseJson;


    public HealthAiClient(String appId, String apiKey) {
        this.appId = appId;
        this.apiKey = apiKey;
    }


    /**
     * 在执行execute方法后，获取请求Json串
     *
     * @return 请求Json串
     */
    public String getRequestJson() {
        return requestJson;
    }


    private void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    /**
     * 在执行execute方法后，获取响应Json串
     *
     * @return 响应Json串
     */
    public String getResponseJson() {
        return responseJson;
    }


    private void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    /**
     * 调用API
     *
     * @param requestJson 请求数据
     * @param url         请求URL
     * @return 响应数据
     */
    public String execute(String requestJson, String url) {

        if (needAuth()) {
            createToken();
        }

        this.setRequestJson(requestJson);

        Map<String, String> header = new HashMap<>(1);
        header.put("token", this.token);

        String responseJson = HttpClientUtils.post(
                url, requestJson, header, HttpClientUtils.CONTENT_TYPE_JSON, 30000);

        this.setResponseJson(responseJson);

        return responseJson;
    }

    /**
     * 调用API
     *
     * @param requestMap 请求数据
     * @param url        请求URL
     * @return 响应数据
     */
    public String execute(Map<String, Object> requestMap, String url) {

        return this.execute(JSON.toJSONString(requestMap), url);
    }

    /**
     * 调用API
     *
     * @param request 请求数据
     * @param url     请求URL
     * @param <T>     Response 中的data对象
     * @return 响应数据
     */
    public <T extends HealthAiData> ServiceResponse<T> execute(ServiceRequest request, String url) {


        String result = this.execute(JSON.toJSONString(request), url);

        try {

            return JSON.parseObject(result, new TypeReference<ServiceResponse<T>>() {
            });
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 获取Token
     *
     * @param appId  应用ID
     * @param apiKey API密钥
     */
    public ServiceResponse<TokenResponseData> token(String appId, String apiKey) {

        String url = "https://api.jiankangyouyi.com/v2/auth/token";

        Map<String, String> params = new HashMap<>(2);
        params.put("appId", appId);
        params.put("apiKey", apiKey);

        String requestJson = JSON.toJSONString(params);

        this.setRequestJson(requestJson);

        String res = HttpClientUtils.post(url, requestJson, HttpClientUtils.CONTENT_TYPE_JSON);

        this.setResponseJson(res);

        if (res == null || "".equals(res.trim())) {
            throw new AuthException("鉴权失败，调用鉴权接口返回为空");
        }

        return JSON.parseObject(res, new TypeReference<ServiceResponse<TokenResponseData>>() {
        });
    }


    private synchronized void createToken() {

        if (!needAuth()) {
            return;
        }

        ServiceResponse<TokenResponseData> res = this.token(this.appId, this.apiKey);

        if (res == null) {
            throw new AuthException("获取token为空");
        }

        if (res.getResultCode() == null || res.getResultCode() != 0) {
            throw new AuthException("获取token返回错误，" +
                    "resultCode[" + res.getResultCode() + "],message[" + res.getMessage() + "]");
        }

        if (res.getData() == null || res.getData().getToken() == null
                || "".equals(res.getData().getToken().trim())) {
            throw new AuthException("AuthResponse 中的data为空，或token为空：" + res.getData());
        }

        this.token = res.getData().getToken();

        Long expireTime = res.getData().getExpireTime();

        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, expireTime == null ? 86400 : expireTime.intValue());

        this.expire = c;
    }


    private boolean needAuth() {

        boolean tokenBlank = (this.token == null || "".equals(token.trim()));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 1);

        return tokenBlank || c.after(this.expire);
    }

}
