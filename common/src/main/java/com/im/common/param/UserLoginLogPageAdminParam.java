package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.PortalTypeEnum;
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
public class UserLoginLogPageAdminParam extends AbstractPageParam<UserLoginLog> {
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

    @Override
    public Wrapper<UserLoginLog> toQueryWrapper(Object wrapperParam) {
        if (portalType == null) {
            return null;
        }

        LambdaQueryWrapper<UserLoginLog> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, portalType);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(UserLoginLog::getUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(ip), UserLoginLog::getIp, ip);
        wrapper.eq(UserLoginLog::getPortalType, portalType);
        wrapper.ge(UserLoginLog::getCreateTime, startDateTime);
        wrapper.le(UserLoginLog::getCreateTime, endDateTime);

        wrapper.orderByDesc(CollectionUtil.toList(UserLoginLog::getCreateTime, UserLoginLog::getId));

        return wrapper;
    }
}
