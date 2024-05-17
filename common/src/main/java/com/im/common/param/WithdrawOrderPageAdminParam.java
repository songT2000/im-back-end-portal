package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.WithdrawOrder;
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
 * 提现订单
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class WithdrawOrderPageAdminParam extends AbstractPageParam<WithdrawOrder> {
    @ApiModelProperty(value = "订单号", position = 1)
    private String orderNum;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "状态", position = 3)
    private WithdrawOrderStatusEnum status;

    @ApiModelProperty(value = "提现方式", position = 4)
    private WithdrawOrderTypeEnum type;

    @ApiModelProperty(value = "提现配置来源", position = 5)
    private WithdrawConfigSourceEnum requestWithdrawConfigSource;

    @ApiModelProperty(value = "提现配置ID", position = 6)
    private Long requestWithdrawConfigId;

    @ApiModelProperty(value = "收款银行ID", position = 7)
    private Long userBankId;

    @ApiModelProperty(value = "收款卡姓名", position = 8)
    private String userBankCardName;

    @ApiModelProperty(value = "收款卡号", position = 9)
    private String userBankCardNum;

    @ApiModelProperty(value = "代付配置ID", position = 10)
    private Long apiWithdrawConfigId;

    @ApiModelProperty(value = "订单完成方式", position = 11)
    private WithdrawOrderFinishTypeEnum finishType;

    @ApiModelProperty(value = "记账日，记账日和开始结束时间必填一项，如果填了记账日，开始结束时间查询条件会失效", position = 12)
    private LocalDate reportDate;

    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间，记账日和开始结束时间必填一项", position = 13)
    private LocalDateTime startDateTime;

    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间，记账日和开始结束时间必填一项", position = 14)
    private LocalDateTime endDateTime;

    @Override
    public Wrapper<WithdrawOrder> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<WithdrawOrder> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }
            wrapper.eq(WithdrawOrder::getUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(orderNum), WithdrawOrder::getOrderNum, orderNum);
        wrapper.eq(status != null, WithdrawOrder::getStatus, status);
        wrapper.eq(type != null, WithdrawOrder::getType, type);
        wrapper.eq(requestWithdrawConfigSource != null, WithdrawOrder::getRequestWithdrawConfigSource, requestWithdrawConfigSource);
        wrapper.eq(requestWithdrawConfigId != null, WithdrawOrder::getRequestWithdrawConfigId, requestWithdrawConfigId);
        wrapper.eq(userBankId != null, WithdrawOrder::getUserBankId, userBankId);
        wrapper.eq(StrUtil.isNotBlank(userBankCardName), WithdrawOrder::getUserBankCardName, userBankCardName);
        wrapper.eq(StrUtil.isNotBlank(userBankCardNum), WithdrawOrder::getUserBankCardNum, userBankCardNum);
        wrapper.eq(apiWithdrawConfigId != null, WithdrawOrder::getApiWithdrawConfigId, apiWithdrawConfigId);
        wrapper.eq(finishType != null, WithdrawOrder::getFinishType, finishType);

        if (reportDate != null) {
            wrapper.eq(WithdrawOrder::getReportDate, reportDate);
        } else if (startDateTime != null && endDateTime != null) {
            wrapper.ge(WithdrawOrder::getCreateTime, startDateTime);
            wrapper.le(WithdrawOrder::getCreateTime, endDateTime);
        } else {
            throw new ImException(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        wrapper.orderByDesc(WithdrawOrder::getId);

        return wrapper;
    }
}
