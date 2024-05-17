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
public class UserOperationLogAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserOperationLog.class, UserOperationLogAdminVO.class, false);

    public UserOperationLogAdminVO(UserOperationLog log) {
        BEAN_COPIER.copy(log, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(log.getUserId(), log.getPortalType());
    }

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("操作类型")
    private UserOperationLogTypeEnum operationType;

    @ApiModelProperty("IP")
    private String ip;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("设备标识")
    private String deviceId;

    @ApiModelProperty("设备类型")
    private DeviceTypeEnum deviceType;

    @ApiModelProperty("请求参数")
    private String requestParam;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户名")
    private String username;
}
