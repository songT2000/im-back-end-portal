package com.im.common.entity;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.MsgTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * app自动回复配置
 */
@Data
@NoArgsConstructor
public class AppAutoReplyConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 2040183512600686274L;

    /**
     * 管理员账号集合，多个使用逗号分割
     **/
    private String usernames;

    /**
     * 消息类型，TIMTextElem图片消息，TIMImageElem文本消息
     */
    private MsgTypeEnum msgType;

    /**
     * 自动回复内容
     */
    private String content;

    /**
     * 自动回复开始时间,格式：HH:mm:ss
     **/
    private String startTime;

    /**
     * 自动回复结束时间,格式：HH:mm:ss
     */
    private String endTime;

    /**
     * 存储图片消息元素
     */
    private String note;
}
