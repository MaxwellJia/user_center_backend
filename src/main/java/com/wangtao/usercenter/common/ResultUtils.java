package com.wangtao.usercenter.common;

/**
 * 返回工具类
 * @author wangtao
 */
public class ResultUtils {

    /**
     * Success
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<T>(0,data,"ok");
    }

    /**
     * Error
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }

    /**
     * Error
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description){
        return new BaseResponse<>(errorCode.getCode(), message, null, description);
    }

    /**
     * Error
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String description){
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), null, description);
    }

    /**
     * Error
     * @param code
     * @return
     */
    public static BaseResponse error(int code, String message, String description){
        return new BaseResponse<>(code, message, null, description);
    }

}
