package com.im.common.util.api.im.tencent.entity.param.operation;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.ChatTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求下载消息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiDownloadMsgParam implements Serializable {

    /**
     * 消息类型，C2C 表示单发消息 Group 表示群组消息
     */
    @JSONField(name = "ChatType")
    private ChatTypeEnum chatType;
    /**
     * 需要下载的消息记录的时间段，2015120121表示获取2015年12月1日21:00 - 21:59的消息的下载地址。
     * <br>该字段需精确到小时。每次请求只能获取某天某小时的所有单发或群组消息记录
     */
    @JSONField(name = "MsgTime")
    private String msgTime;
}
