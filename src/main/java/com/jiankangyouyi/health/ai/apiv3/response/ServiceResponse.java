package com.jiankangyouyi.health.ai.apiv3.response;

/**
 * Api Response
 *
 * @author yangsongbo
 */
public class ServiceResponse<T extends HealthAiData> {

    /**
     * 返回0表示成功，大于0表示失败
     */
    private Integer resultCode;

    /**
     * 接口处理时间，单位毫秒
     */
    private Long spendTime;

    /**
     * 当前服务器时间戳，毫秒
     */
    private Long serverTime;

    /**
     * 处理信息
     */
    private String message;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 业务数据
     */
    private T data;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
