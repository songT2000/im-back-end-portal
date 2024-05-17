package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PortalUserAppOperationLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.PortalUserAppOperationLogTypeEnum;
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
 * app操作日志
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserAppOperationLogPageParam extends AbstractPageParam<PortalUserAppOperationLog> {

    @ApiModelProperty(value = "用户名")
    private String username;

    @NotNull
    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss", required = true)
    private LocalDateTime startDateTime;

    @NotNull
    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss", required = true)
    private LocalDateTime endDateTime;

    @ApiModelProperty(value = "操作类型")
    private PortalUserAppOperationLogTypeEnum operationType;

    @Override
    public Wrapper<PortalUserAppOperationLog> toQueryWrapper(Object wrapperParam) {

        LambdaQueryWrapper<PortalUserAppOperationLog> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(PortalUserAppOperationLog::getUserId, id);
        }

        wrapper.ge(PortalUserAppOperationLog::getCreateTime, startDateTime);
        wrapper.le(PortalUserAppOperationLog::getCreateTime, endDateTime);
        wrapper.eq(operationType != null, PortalUserAppOperationLog::getOperationType, operationType);

        wrapper.orderByDesc(CollectionUtil.toList(PortalUserAppOperationLog::getCreateTime, PortalUserAppOperationLog::getId));

        return wrapper;
    }
}
