package com.im.common.service;

import com.im.common.entity.tim.TimGroupMember;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.TimGroupMemberVO;

import java.util.List;
import java.util.Set;

/**
 * 群组成员服务接口
 */
public interface TimGroupMemberService extends MyBatisPlusService<TimGroupMember> {
    /**
     * @param groupId
     * @param userId
     * @return
     */
    TimGroupMember getByGroupIdAndUserId(String groupId, Long userId);

    /**
     * 退出群组
     *
     * @param groupId       群组ID
     * @param memberUserIds 退群的成员用户ID集合
     */
    void exit(String groupId, List<Long> memberUserIds);

    /**
     * 加入群组
     *
     * @param groupId       群组ID
     * @param memberUserIds 加入群组的成员用户ID集合
     */
    void join(String groupId, List<Long> memberUserIds);

    /**
     * 删除群内所有成员(适用解散群组)
     *
     * @param groupId 群组ID
     */
    void removeByGroupId(String groupId);

    /**
     * 替换群内所有成员（适用于定时同步数据）
     *
     * @param groupId 群组ID
     * @param members 群内成员
     */
    void replace(String groupId, List<TimGroupMember> members);

    /**
     * 给管理后台用的查询列表
     *
     * @param param 参数
     * @return PageVO
     */
    PageVO<TimGroupMemberVO> pageVOForAdmin(TimGroupMemberPageParam param);

    /**
     * 禁言群组成员
     */
    RestResponse shutUp(TimGroupMemberShutUpParam param);

    /**
     * 删除群组成员
     */
    RestResponse deleteMember(TimGroupMemberDeleteParam param);

    /**
     * 后台添加群组成员
     */
    RestResponse addMemberForAdmin(TimGroupMemberAddParam param);

    /**
     * 客户端添加群组成员
     */
    RestResponse addMemberBatch(String groupId, Set<String> usernameList);

    /**
     * 设置/取消管理员
     */
    RestResponse setManager(TimGroupManagerParam param);

}
