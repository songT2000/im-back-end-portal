package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.UserBill;
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
 * 前台账变
 *
 * @author Barry
 * @date 2020-08-06
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBillPagePortalParam extends AbstractPageParam<UserBill> {
    @NotNull
    @ApiModelProperty(value = "开始日期，yyyy-MM-dd", required = true, position = 1)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "结束日期，yyyy-MM-dd", required = true, position = 2)
    private LocalDate endDate;

    @Override
    public Wrapper<UserBill> toQueryWrapper(Object wrapperParam) {
        RedisSessionUser sessionUser = (RedisSessionUser) wrapperParam;

        SysConfigCache sysConfigCache = SpringContextUtil.getBean(SysConfigCache.class);
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        DateTimeUtil.assertMaxQueryPastDay(startDate, reportConfig.getPortalMaxQueryPastDay());
        DateTimeUtil.assertMaxQueryPastDay(endDate, reportConfig.getPortalMaxQueryPastDay());

        LambdaQueryWrapper<UserBill> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(UserBill::getUserId, sessionUser.getId());

        // 用记账日来查询
        wrapper.ge(UserBill::getReportDate, DateTimeUtil.toDateStr(startDate));
        wrapper.le(UserBill::getReportDate, DateTimeUtil.toDateStr(endDate));

        wrapper.orderByDesc(CollectionUtil.toList(UserBill::getCreateTime, UserBill::getId));

        return wrapper;
    }
}
