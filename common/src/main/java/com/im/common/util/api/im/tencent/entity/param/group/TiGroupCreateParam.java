package com.im.common.util.api.im.tencent.entity.param.group;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.GroupApplyJoinOptionEnum;
import com.im.common.entity.enums.GroupMemberRoleEnum;
import com.im.common.entity.enums.GroupTypeEnum;
import com.im.common.entity.tim.TimGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 创建群组参数
 */
@Data
@NoArgsConstructor
public class TiGroupCreateParam implements Serializable {

    private static final long serialVersionUID = 6794552939364879242L;
    /**
     * 群组ID（必填）
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 群主的 UserId（必填）
     */
    @JSONField(name = "Owner_Account")
    private String ownerAccount;
    /**
     * 群组类型（必填）,默认是public
     */
    @JSONField(name = "Type")
    private GroupTypeEnum type = GroupTypeEnum.Public;
    /**
     * 群名称（必填）
     */
    @JSONField(name = "Name")
    private String name;
    /**
     * 群简介（选填）
     */
    @JSONField(name = "Introduction")
    private String introduction;
    /**
     * 群公告（选填）
     */
    @JSONField(name = "Notification")
    private String notification;
    /**
     * 群头像 URL（选填）
     */
    @JSONField(name = "FaceUrl")
    private String faceUrl;
    /**
     * 申请加群处理方式（选填）默认是需要审核
     */
    @JSONField(name = "ApplyJoinOption")
    private GroupApplyJoinOptionEnum applyJoinOption = GroupApplyJoinOptionEnum.NeedPermission;

    /**
     * 最大群成员数量（选填）
     */
    @JSONField(name = "MaxMemberCount")
    private Integer maxMemberCount;

    /**
     * 待添加的群成员数组
     */
    @JSONField(name = "MemberList")
    private List<Member> members = new ArrayList<>();

    @NoArgsConstructor
    @Data
    public static class Member{
        /**
         * 待导入的群成员帐号(必填)
         */
        @JSONField(name = "Member_Account")
        private String memberAccount;
        /**
         * 待导入群成员角色；目前只支持填 Admin，不填则为普通成员 Member (选填)
         */
        @JSONField(name = "Role")
        private GroupMemberRoleEnum role;
    }

    /**
     * 构建群组api对接参数
     * @param timGroup          群组实体
     * @param ownerAccount      群主account
     * @param memberAccounts    成员account集合，注意不要包含群主
     */
    public TiGroupCreateParam(TimGroup timGroup, String ownerAccount, List<String> memberAccounts) {
        this.groupId = String.valueOf(timGroup.getId());
        this.faceUrl = timGroup.getFaceUrl();
        this.introduction = timGroup.getIntroduction();
        this.notification = timGroup.getNotification();
        this.ownerAccount = ownerAccount;
        this.name = timGroup.getGroupName();

        List<Member> collect = memberAccounts.stream().map(p -> {
            Member member = new Member();
            member.setMemberAccount(p);
            return member;
        }).collect(Collectors.toList());
        this.members.addAll(collect);
    }

    /**
     * 构建群组api对接参数
     * @param timGroup          群组实体
     * @param ownerAccount      群主account
     * @param name              群名称
     */
    public TiGroupCreateParam(String groupId,TimGroup timGroup,String ownerAccount,String name) {
        this.groupId = groupId;
        this.faceUrl = timGroup.getFaceUrl();
        this.introduction = timGroup.getIntroduction();
        this.notification = timGroup.getNotification();
        this.ownerAccount = ownerAccount;
        this.name = name;

    }

    /**
     * 构建群组api对接参数
     * @param timGroup          群组实体
     * @param ownerAccount      群主account
     * @param name              群名称
     */
    public TiGroupCreateParam(String groupId,TimGroup timGroup,String ownerAccount,String name,List<Member> members) {
        this.groupId = groupId;
        this.faceUrl = timGroup.getFaceUrl();
        this.introduction = timGroup.getIntroduction();
        this.notification = timGroup.getNotification();
        this.ownerAccount = ownerAccount;
        this.name = name;
        this.members = members;
    }
}
