package com.im.common.entity.tim;

import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.MsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 群聊消息与消息元素关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageGroupElemRel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 8720067146210214308L;

    private MsgTypeEnum msgType;

    private Long messageId;

    private Long elemId;

}
