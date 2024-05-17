package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 离线推送 OfflinePushInfo
 * <br>OfflinePushInfo 是专用于离线推送配置的 JSON 对象，允许配置该条消息是否关闭推送、推送文本描述内容、推送透传字符串等。
 * 使用 OfflinePushInfo 可以方便地设置离线推送信息，无需再通过 TIMCustomElem 封装实现。
 */
@Data
@NoArgsConstructor
public class TiOfflinePushInfoParam implements Serializable {

    private static final long serialVersionUID = 813189134871287966L;
    
    /**
     * 离线推送标题。该字段为 iOS 和 Android 共用。
     */
    @JSONField(name = "Title")
    private String title;
    /**
     * 离线推送内容。该字段会覆盖上面各种消息元素 TIMMsgElement 的离线推送展示文本。
     * 若发送的消息只有一个 TIMCustomElem 自定义消息元素，该 Desc 字段会覆盖 TIMCustomElem 中的 Desc 字段。如果两个 Desc 字段都不填，将收不到该自定义消息的离线推送。
     */
    @JSONField(name = "Desc")
    private String desc;

    public TiOfflinePushInfoParam(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }
}
