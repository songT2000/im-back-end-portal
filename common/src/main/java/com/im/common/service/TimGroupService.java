package com.im.common.service;

import com.im.common.entity.tim.TimGroup;
import com.im.common.param.TimGroupAddParam;
import com.im.common.param.TimGroupEditParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 群组接口
 */
public interface TimGroupService extends MyBatisPlusService<TimGroup> {

    TimGroup getByGroupId(String groupId);

    /**
     * 客户端创建群组
     *
     * @param param 创建群组的参数
     */
    void create(TimGroupAddParam param);

    /**
     * 客户端修改群组信息
     *
     * @param param 修改群组的参数
     */
    RestResponse edit(TimGroupEditParam param);

    /**
     * 解散群组，删除本地群组信息
     *
     * @param groupId 群组ID
     */
    void destroyLocal(String groupId);

    /**
     * 后台管理员解散群组
     *
     * @param groupId 群组ID
     */
    RestResponse destroyForAdmin(String groupId);

    /**
     * 定时全量更新群组信息
     */
    void sync();

    /**
     * 同步单个群组信息
     *
     * @param groupId 群组ID
     */
    void sync(String groupId);

    /**
     * 全员禁言OR解除全员禁言
     *
     * @param groupId 群组ID
     * @param enable  true：禁言，false：不禁言
     */
    RestResponse shutUpAllMember(String groupId, boolean enable);

    /**
     * 显示群内成员启/禁
     *
     * @param groupId 群组ID
     * @param enable  true：启用，false：禁用
     */
    RestResponse updateShowMemberEnabled(String groupId, boolean enable, String opUsername);

    /**
     * 允许成员私聊启/禁
     *
     * @param groupId 群组ID
     * @param enable  true：启用，false：禁用
     */
    RestResponse updateAnonymousChatEnabled(String groupId, boolean enable, String opUsername);

    /**
     * 普通成员上传文件权限启/禁
     *
     * @param groupId 群组ID
     * @param enable  true：启用，false：禁用
     */
    RestResponse updateUploadEnabled(String groupId, boolean enable, String opUsername);

    /**
     * 允许普通成员拉人进群权限启/禁
     *
     * @param groupId 群组ID
     * @param enable  true：启用，false：禁用
     */
    RestResponse updateAddMemberEnabled(String groupId, boolean enable, String opUsername);

    /**
     * 允许普通成员拉人进群权限启/禁
     *
     * @param groupId      群组ID
     * @param introduction 群简介
     */
    RestResponse updateIntroduction(String groupId, String introduction, String opUsername);

    /**
     * 允许普通成员拉人进群权限启/禁
     *
     * @param groupId      群组ID
     * @param notification 群公告
     */
    RestResponse updateNotification(String groupId, String notification, String opUsername);

    /**
     * 群内私加好友权限启/禁
     *
     * @param groupId 群组ID
     * @param enable  true：启用，false：禁用
     */
    RestResponse updateAddFriendEnabled(String groupId, boolean enable, String opUsername);

    /**
     * 普通成员退出群组权限启/禁
     *
     * @param groupId 群组ID
     * @param enable  true：启用，false：禁用
     */
    RestResponse updateExitEnabled(String groupId, boolean enable, String opUsername);

    /**
     * 一键复制创建新群
     *
     * @param groupId   原群组ID
     * @param groupName 新群的名称
     */
    RestResponse copyCreateNewGroup(String groupId, String groupName);
}
