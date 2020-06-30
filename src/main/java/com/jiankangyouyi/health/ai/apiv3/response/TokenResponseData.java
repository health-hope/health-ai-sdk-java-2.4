package com.jiankangyouyi.health.ai.apiv3.response;

/**
 * Token Response data
 *
 * @author yangsongbo
 */
public class TokenResponseData implements HealthAiData {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 有效期，单位：秒
     */
    private Long expireTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "TokenData{" +
                "token='" + token + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}