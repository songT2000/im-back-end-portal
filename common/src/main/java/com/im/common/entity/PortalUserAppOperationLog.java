package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import com.im.common.entity.enums.PortalUserAppOperationLogTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户app操作日志，包含群组加人，踢人，好友加减，黑名单加减
 * 主要用于记录回调方法中的app用户操作日志
 */
@Getter
@Setter
@NoArgsConstructor
public class PortalUserAppOperationLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5108487428277522231L;

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 操作的用户ID
     **/
    private Long userId;
    /**
     * 被操作的用户ID
     **/
    private Long toUserId;

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 操作类型
     */
    private PortalUserAppOperationLogTypeEnum operationType;

    /**
     * IP
     **/
    private String clientIp;

    /**
     * 客户端平台
     **/
    private String optPlatform;

    /**
     * 操作内容
     **/
    private String content;
}
