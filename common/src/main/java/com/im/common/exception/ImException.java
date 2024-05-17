package com.im.common.exception;


import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统异常
 *
 * @author Barry
 * @date 2020-01-04
 */
@Getter
@Setter
@ToString
public class ImException extends RuntimeException {
    /**
     * 返回状态码
     **/
    private ResponseCode code;
    /**
     * 状态码中消息如果有占位符，设置这里
     **/
    private Object[] params;
    /**
     * 如果需要传递一些数据，设置这里
     **/
    private Object data;
    /**
     * 是否强制登出
     **/
    private Boolean forceLogout;

    /**
     * 初始化方法
     *
     * @param code 错误码
     */
    public ImException(RestResponse rsp) {
        super();
        this.code = ResponseCode.getEnum(rsp.getCode());
        this.params = rsp.getParams();
    }

    /**
     * 初始化方法
     *
     * @param code 错误码
     */
    public ImException(ResponseCode code) {
        super();
        this.code = code;
    }

    /**
     * 初始化方法
     *
     * @param code        错误码
     * @param forceLogout 是否强制登出
     */
    public ImException(ResponseCode code, boolean forceLogout) {
        super();
        this.code = code;
        this.forceLogout = forceLogout;
    }

    /**
     * 初始化方法
     *
     * @param code   错误码
     * @param params 错误码对应的参数
     */
    public ImException(ResponseCode code, Object[] params) {
        super();
        this.code = code;
        this.params = params;
    }

    /**
     * 初始化方法
     *
     * @param code        错误码
     * @param params      错误码对应的参数
     * @param forceLogout 是否强制登出
     */
    public ImException(ResponseCode code, Object[] params, boolean forceLogout) {
        super();
        this.code = code;
        this.params = params;
        this.forceLogout = forceLogout;
    }

    /**
     * 初始化方法
     *
     * @param code 错误码
     * @param data 携带相应数据
     */
    public ImException(ResponseCode code, Object data) {
        super();
        this.code = code;
        this.data = data;
    }

    /**
     * 初始化方法
     *
     * @param code        错误码
     * @param data        携带相应数据
     * @param forceLogout 是否强制登出
     */
    public ImException(ResponseCode code, Object data, boolean forceLogout) {
        super();
        this.code = code;
        this.data = data;
        this.forceLogout = forceLogout;
    }

    /**
     * 初始化方法
     *
     * @param code   错误码
     * @param data   携带相应数据
     * @param params 错误码对应的参数
     */
    public ImException(ResponseCode code, Object data, Object[] params) {
        super();
        this.code = code;
        this.data = data;
        this.params = params;
    }

    /**
     * 初始化方法
     *
     * @param code        错误码
     * @param data        携带相应数据
     * @param params      错误码对应的参数
     * @param forceLogout 是否强制登出
     */
    public ImException(ResponseCode code, Object data, Object[] params, boolean forceLogout) {
        super();
        this.code = code;
        this.data = data;
        this.params = params;
        this.forceLogout = forceLogout;
    }

    @JSONField(serialize = false)
    public boolean isOkRsp() {
        return ResponseCode.isOkRsp(code);
    }
}
