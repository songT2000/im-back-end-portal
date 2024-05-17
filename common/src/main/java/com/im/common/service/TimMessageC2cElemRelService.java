package com.im.common.service;

import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.*;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.List;

public interface TimMessageC2cElemRelService extends MyBatisPlusService<TimMessageC2cElemRel> {

    /**
     * 保存单聊消息元素
     * @param messageId     消息ID
     * @param elem          8种消息元素
     */
    void saveC2cMessageElem(TiMsgTypeEnum msgType, Long messageId, Object elem);

    /**
     * 撤回消息
     * @param messageId     消息ID
     */
    void withdraw(Long messageId);

    /**
     * 批量新增消息元素
     */
    void batchSaveGroupMessageElem(List<TimMessageElemBo> list);

    /**
     * 根据消息ID查询消息元素
     * @param messageIds        消息ID集合
     */
    List<TimMessageBody> getByIds(List<Long> messageIds);

}
