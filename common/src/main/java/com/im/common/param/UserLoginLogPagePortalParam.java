package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.UserLoginLog;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.CollectionUtil;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.redis.RedisSessionUser;
import com.im.common.util.spring.SpringContextUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 登录日志参数
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserLoginLogPagePortalParam extends AbstractPageParam<UserLoginLog> {
    @NotNull
    @ApiModelProperty(value = "开始日期，格式yyyy-MM-dd", required = true, position = 1)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "结束日期，格式yyyy-MM-dd", required = true, position = 2)
    private LocalDate endDate;

    @Override
    public Wrapper<UserLoginLog> toQueryWrapper(Object wrapperParam) {
        RedisSessionUser sessionUser = (RedisSessionUser) wrapperParam;

        if (sessionUser.getPortalType() == PortalTypeEnum.PORTAL) {
            SysConfigCache sysConfigCache = SpringContextUtil.getBean(SysConfigCache.class);
            ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
            DateTimeUtil.assertMaxQueryPastDay(startDate, reportConfig.getPortalMaxQueryPastDay());
            DateTimeUtil.assertMaxQueryPastDay(endDate, reportConfig.getPortalMaxQueryPastDay());
        }

        LambdaQueryWrapper<UserLoginLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.ge(UserLoginLog::getCreateTime, startDate.atStartOfDay());
        wrapper.le(UserLoginLog::getCreateTime, endDate.atTime(23, 59, 59));
        wrapper.eq(UserLoginLog::getUserId, sessionUser.getId());
        wrapper.eq(UserLoginLog::getPortalType, sessionUser.getPortalType());

        wrapper.orderByDesc(CollectionUtil.toList(UserLoginLog::getCreateTime, UserLoginLog::getId));

        return wrapper;
    }
}
