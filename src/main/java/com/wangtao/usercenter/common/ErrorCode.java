package com.wangtao.usercenter.common;

/**
 * 全局错误码
 * @author wangtao
 */
public enum ErrorCode {

    SUCCESS(0,"ok",""),
    PARAMS_ERROR(40000,"Request parameters error", ""),
    PARAMS_NULL_ERROR(40001, "Data requested is empty", ""),
    NOT_LOGIN(40100, "No Login",""),
    NO_AUTH(40101,"No authentication", ""),
    SYSTEM_ERROR(50000, "System error", "");


    /**
     * Status code information
     */
    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
