package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.tim.TimMessageElemCustom;
import com.im.common.util.fastjson.EncryptSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 自定义消息元素
 * <br>当消息中只有一个 TIMCustomElem 自定义消息元素时，如果 Desc 字段和 OfflinePushInfo.Desc 字段都不填写，将收不到该条消息的离线推送，
 * 需要填写 OfflinePushInfo.Desc 字段才能收到该消息的离线推送。
 */
@Data
@NoArgsConstructor
public class TIMCustomElem extends TiMsgContent {
    private static final long serialVersionUID = 8009876575278473614L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimMessageElemCustom.class, TIMCustomElem.class, false);

    public TIMCustomElem(TimMessageElemCustom elem) {
        BEAN_COPIER.copy(elem, this, null);
    }
    /**
     * 自定义消息数据。 不作为 APNs 的 payload 字段下发，故从 payload 中无法获取 Data 字段。
     */
    @JSONField(name = "Data",serializeUsing = EncryptSerializer.class)
    private String data;
    /**
     * 自定义消息描述信息。当接收方为 iOS 或 Android 后台在线时，做离线推送文本展示。
     * <br>若发送自定义消息的同时设置了 OfflinePushInfo.Desc 字段，此字段会被覆盖，请优先填 OfflinePushInfo.Desc 字段。
     */
    @JSONField(name = "Desc")
    private String description;
    /**
     * 扩展字段。当接收方为 iOS 系统且应用处在后台时，此字段作为 APNs 请求包 Payloads 中的 Ext 键值下发，Ext 的协议格式由业务方确定，APNs 只做透传。
     */
    @JSONField(name = "Ext")
    private String ext;
    /**
     * 自定义 APNs 推送铃音
     */
    @JSONField(name = "Sound")
    private String sound;
}
