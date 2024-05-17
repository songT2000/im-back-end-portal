package com.im.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.param.TimC2cMessagePortalPageParam;
import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.TimMessageC2cVO;

import java.util.List;

public interface TimMessageC2cService extends MyBatisPlusService<TimMessageC2c> {

    /**
     * 保存单聊消息
     *
     * @param entity    消息主体
     * @param msgBodies 消息元素内容
     */
    void save(TimMessageC2c entity, List<TiMsgBody> msgBodies);

    /**
     * 撤回单聊消息（只处理本地消息删除）
     *
     * @param fromUserId 发消息的用户ID
     * @param toUserId   接收消息的用户ID
     * @param msgKey     消息的唯一标识
     */
    void withdraw(Long fromUserId, Long toUserId, String msgKey);

    /**
     * 后台管理员撤回单聊消息
     *
     * @param fromUserId 发消息的用户ID
     * @param toUserId   接收消息的用户ID
     * @param msgKey     消息的唯一标识
     */
    RestResponse withdrawForAdmin(Long fromUserId, Long toUserId, String msgKey);

    /**
     * 同步昨天活跃过的用户近两天的所有单聊记录
     */
    void sync();

    /**
     * 同步两个好友之间近两天的单聊记录
     *
     * @param fromUserId 用户ID
     * @param toUserId   另一个用户ID
     */
    void sync(Long fromUserId, Long toUserId);

    /**
     * 用于导入消息到腾讯IM
     *
     * @param current  页码
     * @param pageSize 查询数量
     * @return 单聊消息包括单聊消息元素
     */
    Page<TimMessageC2c> getForImport(int current, int pageSize);

    /**
     * 管理员发送文本消息
     *
     * @param toUserId 接收人ID
     * @param content  消息内容
     */
    RestResponse sendTextMessageForAdmin(Long toUserId, String content);

    /**
     * 删除用户所有的单聊记录，包括撤回腾讯IM的聊天记录
     *
     * @param userId 用户ID
     */
    void deleteUserHistoryMessage(Long userId);

    /**
     * 前台查询单聊消息
     */
    PageVO<TimMessageC2cVO> pageFormPortal(TimC2cMessagePortalPageParam param);
}
