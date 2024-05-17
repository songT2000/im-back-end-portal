package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.WithdrawOrderLog;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 提现订单日志
 *
 * @author Barry
 * @date 2021-03-24
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawOrderLogPageParam extends AbstractPageParam<WithdrawOrderLog> {
    @NotNull
    @ApiModelProperty(value = "提现订单ID", required = true, position = 1)
    private Long orderId;

    @Override
    public Wrapper<WithdrawOrderLog> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<WithdrawOrderLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(WithdrawOrderLog::getOrderId, orderId);

        wrapper.orderByDesc(CollectionUtil.newArrayList(WithdrawOrderLog::getCreateTime, WithdrawOrderLog::getId));

        return wrapper;
    }
}
