package com.im.common.vo;

import com.im.common.entity.UserOperationLog;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 操作日志VO
 *
 * @author Barry
 * @date 2020-05-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserOperationLogPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserOperationLog.class, UserOperationLogPortalVO.class, false);

    public UserOperationLogPortalVO(UserOperationLog log) {
        BEAN_COPIER.copy(log, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(log.getUserId(), log.getPortalType());
    }

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("操作类型")
    private UserOperationLogTypeEnum operationType;

    @ApiModelProperty("操作IP")
    private String ip;

    @ApiModelProperty("操作区域")
    private String area;

    @ApiModelProperty("操作设备标识")
    private String deviceId;

    @ApiModelProperty("操作设备类型")
    private DeviceTypeEnum deviceType;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户名")
    private String username;
}
