package com.im.common.entity.tim;

import com.im.common.entity.enums.TiMsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimMessageElemBo {
    /**
     * 消息ID
     */
    private Long messageId;
    /**
     * 8种消息元素
     */
    private Object elem;
    /**
     * 消息类型
     */
    private TiMsgTypeEnum msgType;


}
