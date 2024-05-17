package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.RechargeOrder;
import com.im.common.entity.enums.*;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 充值订单
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class RechargeOrderPageAdminParam extends AbstractPageParam<RechargeOrder> {
    @ApiModelProperty(value = "订单号", position = 1)
    private String orderNum;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "状态", position = 3)
    private RechargeOrderStatusEnum status;

    @ApiModelProperty(value = "充值方式", position = 4)
    private RechargeOrderTypeEnum type;

    @ApiModelProperty(value = "配置来源", position = 5)
    private RechargeConfigSourceEnum rechargeConfigSource;

    @ApiModelProperty(value = "充值配置ID", position = 6)
    private Long rechargeConfigId;

    @ApiModelProperty(value = "付款人", position = 7)
    private String userCardName;

    @ApiModelProperty(value = "订单完成方式", position = 8)
    private RechargeOrderFinishTypeEnum finishType;

    @ApiModelProperty(value = "记账日，记账日和开始结束时间必填一项，如果填了记账日，开始结束时间查询条件会失效", position = 9)
    private LocalDate reportDate;

    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间，记账日和开始结束时间必填一项", position = 10)
    private LocalDateTime startDateTime;

    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间，记账日和开始结束时间必填一项", position = 11)
    private LocalDateTime endDateTime;

    @Override
    public Wrapper<RechargeOrder> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<RechargeOrder> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }
            wrapper.eq(RechargeOrder::getUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(orderNum), RechargeOrder::getOrderNum, orderNum);
        wrapper.eq(status != null, RechargeOrder::getStatus, status);
        wrapper.eq(type != null, RechargeOrder::getType, type);
        wrapper.eq(rechargeConfigSource != null, RechargeOrder::getRechargeConfigSource, rechargeConfigSource);
        wrapper.eq(rechargeConfigId != null, RechargeOrder::getRechargeConfigId, rechargeConfigId);
        wrapper.eq(StrUtil.isNotBlank(userCardName), RechargeOrder::getUserCardName, userCardName);
        wrapper.eq(finishType != null, RechargeOrder::getFinishType, finishType);

        if (reportDate != null) {
            wrapper.eq(RechargeOrder::getReportDate, reportDate);
        } else if (startDateTime != null && endDateTime != null) {
            wrapper.ge(RechargeOrder::getCreateTime, startDateTime);
            wrapper.le(RechargeOrder::getCreateTime, endDateTime);
        } else {
            throw new ImException(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        wrapper.orderByDesc(RechargeOrder::getId);

        return wrapper;
    }
}
