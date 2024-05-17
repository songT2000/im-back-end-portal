package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.entity.enums.*;
import com.im.common.param.TimGroupEditParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.io.Serializable;

/**
 * 修改群基础资料
 */
@Data
@NoArgsConstructor
public class TiGroupUpdateParam implements Serializable {

    private static final long serialVersionUID = 6794552939364879242L;

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(TimGroupEditParam.class, TiGroupUpdateParam.class, false);

    public TiGroupUpdateParam(TimGroupEditParam e) {
        BEAN_COPIER.copy(e, this, null);
        this.name = e.getGroupName();
    }

    /**
     * 群组ID（必填）
     */
    @JSONField(name = "GroupId")
    private String groupId;
    /**
     * 群名称（选填）
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
     * 设置全员禁言（选填）:"On"开启，"Off"关闭
     */
    @JSONField(name = "ShutUpAllMember")
    private OnOrOffEnum shutUpAllMember;

}
