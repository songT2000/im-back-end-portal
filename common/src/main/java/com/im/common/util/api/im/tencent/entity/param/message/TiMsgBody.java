package com.im.common.util.api.im.tencent.entity.param.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.TiMsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 腾讯IM消息内容 MsgBody
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiMsgBody implements Serializable {

    /**
     * 消息类型
     *
     * @see TiMsgTypeEnum
     */
    @JSONField(name = "MsgType")
    private TiMsgTypeEnum msgType;

    /**
     * 消息类型
     */
    @JSONField(name = "MsgContent")
    private TiMsgContent msgContent;

}
