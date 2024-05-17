package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserLoginLog;
import com.im.common.entity.UserOperationLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 供应商登录日志
 *
 * @author Barry
 * @date 2019-11-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserOperationLogPageAdminParam extends AbstractPageParam<UserOperationLog> {
    /**
     * 门户类型，不用前台传
     */
    @ApiModelProperty(hidden = true)
    private PortalTypeEnum portalType;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "IP")
    private String ip;

    @NotNull
    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss", required = true)
    private LocalDateTime startDateTime;

    @NotNull
    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss", required = true)
    private LocalDateTime endDateTime;

    @ApiModelProperty(value = "操作类型")
    private UserOperationLogTypeEnum operationType;

    @Override
    public Wrapper<UserOperationLog> toQueryWrapper(Object wrapperParam) {
        if (portalType == null) {
            return null;
        }

        LambdaQueryWrapper<UserOperationLog> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, portalType);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(UserOperationLog::getUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(ip), UserOperationLog::getIp, ip);
        wrapper.eq(UserOperationLog::getPortalType, portalType);
        wrapper.ge(UserOperationLog::getCreateTime, startDateTime);
        wrapper.le(UserOperationLog::getCreateTime, endDateTime);
        wrapper.eq(operationType != null, UserOperationLog::getOperationType, operationType);

        wrapper.orderByDesc(CollectionUtil.toList(UserOperationLog::getCreateTime, UserOperationLog::getId));

        return wrapper;
    }
}
