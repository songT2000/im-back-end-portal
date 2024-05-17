package com.im.common.exception;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 通用的系统异常
 *
 * @author Barry
 * @date 2018/5/13
 */
@Getter
@Setter
@ToString
public class CommonException extends RuntimeException {
    /**
     * 返回状态码
     **/
    private String message;
    /**
     * 状态码中消息如果有占位符，设置这里
     **/
    private Object[] params;

    public CommonException(String message, Object[] params) {
        this.message = message;
        this.params = params;
    }
}
