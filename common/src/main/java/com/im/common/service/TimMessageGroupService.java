package com.im.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.param.TimGroupMessagePortalPageParam;
import com.im.common.param.TimGroupSystemMessageSendParam;
import com.im.common.param.TimMessageGroupWithdrawParam;
import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.TimMessageGroupVO;

import java.time.LocalDateTime;
import java.util.List;

public interface TimMessageGroupService extends MyBatisPlusService<TimMessageGroup> {

    /**
     * 保存群聊消息
     *
     * @param entity    消息主体
     * @param msgBodies 消息元素内容
     */
    void save(TimMessageGroup entity, List<TiMsgBody> msgBodies);

    /**
     * 删除群组了解记录（适用于群组解散）
     *
     * @param groupId 群组ID
     */
    void removeByGroupId(String groupId);

    /**
     * 定时同步群组聊天信息
     */
    void sync();

    /**
     * 同步单个群组聊天信息
     *
     * @param groupId 群组ID
     */
    void sync(String groupId);

    /**
     * 用于导入群组消息
     *
     * @param groupId   群组ID
     * @param startTime 同步消息的开始时间
     * @param current   当前页
     * @param pageSize  页码
     */
    Page<TimMessageGroup> getForImport(String groupId, LocalDateTime startTime, int current, int pageSize);

    /**
     * 撤回群组消息
     */
    RestResponse withdraw(TimMessageGroupWithdrawParam param);

    /**
     * 删除本地消息，适用群聊撤回消息回调事件
     */
    void deleteLocalMessage(TimMessageGroupWithdrawParam param);

    /**
     * 后台管理系统发送群组文本消息
     */
    RestResponse sendGroupSystemNotification(TimGroupSystemMessageSendParam param);

    /**
     * 删除用户所有的群聊记录，包括撤回腾讯IM的聊天记录
     *
     * @param userId 用户ID
     */
    void deleteUserHistoryMessage(Long userId);

    /**
     * 前台查询群聊消息
     */
    PageVO<TimMessageGroupVO> pageFormPortal(TimGroupMessagePortalPageParam param);
}
