package com.im.common.entity.tim;

import com.baomidou.mybatisplus.annotation.TableField;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 腾讯IM群聊消息
 */
@Data
@NoArgsConstructor
public class TimMessageGroup extends BaseEntity implements Serializable,TimMessageElem {

    private static final long serialVersionUID = -4657907167455017064L;
    /**
     * 消息接收群组ID
     */
    private String groupId;
    /**
     * 消息发送方 UserID
     */
    private Long fromUserId;
    /**
     * 消息随机数，后台用于同一秒内的消息去重。
     */
    private Long msgRandom;
    /**
     * 消息序列号，后台会根据该字段去重及进行同秒内消息的排序
     */
    private Long msgSeq;

    /**
     * 消息来自哪个客户端
     */
    private String msgFromPlatform;
    /**
     * 消息发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 消息来自哪个IP
     */
    private String clientIp;

    /**
     * 消息内容，不需入库，用于解析传递
     */
    @TableField(exist = false)
    private List<TimMessageBody> msgBody = new ArrayList<>();
}
