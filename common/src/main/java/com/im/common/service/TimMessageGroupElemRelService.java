package com.im.common.service;

import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.entity.tim.TimMessageC2cElemRel;
import com.im.common.entity.tim.TimMessageElemBo;
import com.im.common.entity.tim.TimMessageGroupElemRel;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.List;

public interface TimMessageGroupElemRelService extends MyBatisPlusService<TimMessageGroupElemRel> {
    /**
     * 保存群聊消息元素
     * @param messageId     消息ID
     * @param elem          8种消息元素
     */
    void saveGroupMessageElem(TiMsgTypeEnum msgType,Long messageId, Object elem);

    /**
     * 撤回消息
     * @param messageId     消息ID
     */
    void withdraw(Long messageId);
    /**
     * 批量删除消息元素
     * @param messageIds    消息ID集合
     */
    void deleteByMessageIds(List<Long> messageIds);

    /**
     * 批量新增消息元素
     */
    void batchSaveGroupMessageElem(List<TimMessageElemBo> list);

    /**
     * 根据消息ID集合查询消息元素
     * @param messageIds        消息ID集合
     */
    List<TimMessageBody> getByIds(List<Long> messageIds);

}
