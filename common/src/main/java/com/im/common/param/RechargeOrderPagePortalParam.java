package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ReportConfigBO;
import com.im.common.entity.RechargeOrder;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.spring.SpringContextUtil;
import com.im.common.vo.PortalSessionUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 充值订单分页
 *
 * @author Barry
 * @date 2021-09-04
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderPagePortalParam extends AbstractPageParam<RechargeOrder> {
    @NotNull
    @ApiModelProperty(value = "开始日期，yyyy-MM-dd", required = true, position = 1)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "结束日期，yyyy-MM-dd", required = true, position = 2)
    private LocalDate endDate;

    @Override
    public Wrapper<RechargeOrder> toQueryWrapper(Object wrapperParam) {
        SysConfigCache sysConfigCache = SpringContextUtil.getBean(SysConfigCache.class);
        ReportConfigBO reportConfig = sysConfigCache.getReportConfigFromRedis();
        DateTimeUtil.assertMaxQueryPastDay(startDate, reportConfig.getPortalMaxQueryPastDay());
        DateTimeUtil.assertMaxQueryPastDay(endDate, reportConfig.getPortalMaxQueryPastDay());

        PortalSessionUser sessionUser = (PortalSessionUser) wrapperParam;

        LambdaQueryWrapper<RechargeOrder> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(RechargeOrder::getUserId, sessionUser.getId());

        // 用记账日来查询
        wrapper.ge(RechargeOrder::getReportDate, DateTimeUtil.toDateStr(startDate));
        wrapper.le(RechargeOrder::getReportDate, DateTimeUtil.toDateStr(endDate));

        wrapper.orderByDesc(RechargeOrder::getId);

        return wrapper;
    }
}
