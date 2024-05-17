package com.im.common.vo;

import com.im.common.entity.PortalUserAppOperationLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.PortalUserAppOperationLogTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * app操作日志VO
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserAppOperationLogVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalUserAppOperationLog.class, PortalUserAppOperationLogVO.class, false);

    /**
     * 操作的用户ID
     **/
    @ApiModelProperty("操作的用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;
    /**
     * 被操作的用户ID
     **/
    @ApiModelProperty("被操作的用户ID")
    private Long toUserId;

    /**
     * 群组ID
     */
    @ApiModelProperty("群组ID")
    private String groupId;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private PortalUserAppOperationLogTypeEnum operationType;

    /**
     * IP
     **/
    @ApiModelProperty("IP")
    private String clientIp;

    /**
     * 客户端平台
     **/
    @ApiModelProperty("客户端平台")
    private String optPlatform;

    /**
     * 日志内容
     **/
    @ApiModelProperty("日志内容")
    private String content;

    /**
     * 操作时间
     **/
    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    public PortalUserAppOperationLogVO(PortalUserAppOperationLog log) {
        BEAN_COPIER.copy(log, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(log.getUserId(), PortalTypeEnum.PORTAL);
    }
}
