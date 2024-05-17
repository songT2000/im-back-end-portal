package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBill;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.enums.UserBillTypeEnum;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应商账变
 *
 * @author Barry
 * @date 2019-11-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBillPageAdminParam extends AbstractPageParam<UserBill> {
    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "账变类型", position = 2)
    private UserBillTypeEnum type;

    @ApiModelProperty(value = "订单号", position = 3)
    private String orderNum;

    @ApiModelProperty(value = "记账日，记账日和开始结束时间必填一项，如果填了记账日，开始结束时间查询条件会失效", position = 4)
    private LocalDate reportDate;

    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间，记账日和开始结束时间必填一项", position = 5)
    private LocalDateTime startDateTime;

    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间，记账日和开始结束时间必填一项", position = 6)
    private LocalDateTime endDateTime;

    @Override
    public Wrapper<UserBill> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserBill> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(UserBill::getUserId, id);
        }

        wrapper.eq(type != null, UserBill::getType, type);
        wrapper.eq(StrUtil.isNotBlank(orderNum), UserBill::getOrderNum, orderNum);

        if (reportDate != null) {
            wrapper.eq(UserBill::getReportDate, reportDate);
        } else if (startDateTime != null && endDateTime != null) {
            wrapper.ge(UserBill::getCreateTime, startDateTime);
            wrapper.le(UserBill::getCreateTime, endDateTime);
        } else {
            throw new ImException(ResponseCode.SYS_REQUEST_PARAM_ERROR);
        }

        wrapper.orderByDesc(CollectionUtil.newArrayList(UserBill::getCreateTime, UserBill::getId));

        return wrapper;
    }
}
