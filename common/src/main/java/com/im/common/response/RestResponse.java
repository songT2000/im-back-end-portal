package com.im.common.response;

import com.im.common.exception.CommonRestResponse;
import com.im.common.exception.ImException;
import io.swagger.annotations.ApiModel;

/**
 * 为保证所有接口数据格式统一
 * 所有Controller只能返回这个对象，不建议返回其它对象
 * Service中可根据情况返回这个对象，如删除角色时已分配有用户，则可返回该对象
 *
 * @author Barry
 * @date 2018/5/12
 */
@ApiModel
public class RestResponse<T> extends CommonRestResponse<T> {
    public static final RestResponse OK = new RestResponse(true, false, null, ResponseCode.OK);
    public static final RestResponse SYS_DATA_STATUS_ERROR = new RestResponse(false, false, null, ResponseCode.SYS_DATA_STATUS_ERROR);

    public RestResponse() {
    }

    public RestResponse(boolean success, boolean exception, T data, ResponseCode rspCode, Object... params) {
        super(success, exception, data, rspCode, params);
    }

    public RestResponse(Boolean success, Boolean exception, Boolean forceLogout, String code, String message, T data) {
        super(success, exception, forceLogout, code, message, data);
    }

    public static <T> RestResponse<T> ok(T data) {
        return new RestResponse(true, false, data, ResponseCode.OK);
    }

    public static RestResponse failed(ResponseCode rsp) {
        return new RestResponse(false, false, null, rsp);
    }

    public static RestResponse failed(ResponseCode rsp, Object... params) {
        return build(false, false, null, rsp, params);
    }

    public static RestResponse logout(ResponseCode rsp) {
        RestResponse restResponse = new RestResponse(false, false, null, rsp);
        restResponse.setForceLogout(true);
        return restResponse;
    }

    public static RestResponse exception(ResponseCode rsp) {
        return new RestResponse(false, true, null, rsp);
    }

    public static RestResponse exception(ResponseCode rsp, Object... params) {
        return build(false, true, null, rsp, params);
    }

    public static RestResponse exception(ImException e) {
        return new RestResponse(e.isOkRsp(), true, e.getData(), e.getCode(), e.getParams());
    }

    public static RestResponse build(boolean success, boolean exception, Object data, ResponseCode rsp, Object... params) {
        return new RestResponse(success, exception, data, rsp, params);
    }

    public static RestResponse build(RestResponse rsp) {
        return new RestResponse<>(rsp.getSuccess(), rsp.getException(), rsp.getForceLogout(), rsp.getCode(), rsp.getMessage(), rsp.getData());
    }

    public static RestResponse buildClearData(RestResponse rsp) {
        return new RestResponse<>(rsp.getSuccess(), rsp.getException(), rsp.getForceLogout(), rsp.getCode(), rsp.getMessage(), null);
    }
}