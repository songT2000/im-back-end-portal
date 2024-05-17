package com.im.common.util.api.im.tencent.entity.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.beans.Transient;
import java.io.Serializable;

/**
 * 腾讯IM返回消息.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TiBaseResult implements Serializable {

    /**
     * 错误码，0为成功，其他为失败，可查询 错误码表(BaseErrorMsgEnum) 得到具体的原因.
     */
    @JSONField(name = "ErrorCode")
    private Integer errorCode;

    /**
     * 失败原因
     */
    @JSONField(name = "ErrorInfo")
    private String errorInfo;

    /**
     * 请求处理的结果，OK 表示处理成功，FAIL 表示失败，如果为 FAIL，ErrorInfo 带上失败原因
     */
    @JSONField(name = "ActionStatus")
    private String actionStatus;

    /**
     * 详细的客户端展示信息
     */
    @JSONField(name = "ErrorDisplay")
    private String errorDisplay;

    @JSONField(serialize = false, deserialize = false)
    @Transient
    public boolean isOk() {
        return errorCode != null && errorCode == CommonConstant.INT_0;
    }

    public static TiBaseResult success() {
        TiBaseResult result = new TiBaseResult();
        result.setActionStatus("OK");
        result.setErrorCode(CommonConstant.INT_0);
        return result;
    }
}
