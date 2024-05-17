package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.RechargeOrderLog;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 充值订单日志
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderLogPageParam extends AbstractPageParam<RechargeOrderLog> {
    @NotNull
    @ApiModelProperty(value = "充值订单ID", required = true, position = 1)
    private Long orderId;

    @Override
    public Wrapper<RechargeOrderLog> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<RechargeOrderLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(RechargeOrderLog::getOrderId, orderId);

        wrapper.orderByDesc(CollectionUtil.newArrayList(RechargeOrderLog::getCreateTime, RechargeOrderLog::getId));

        return wrapper;
    }
}
