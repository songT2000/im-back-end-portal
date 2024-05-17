package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PersonalRedEnvelope;
import com.im.common.entity.enums.PersonalRedEnvelopeStatusEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 个人红包
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class PersonalRedEnvelopePageAdminParam extends AbstractPageParam<PersonalRedEnvelope> {
    @ApiModelProperty(value = "订单号", position = 1)
    private String orderNum;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "领取用户名", position = 3)
    private String receiveUsername;

    @ApiModelProperty(value = "状态", position = 4)
    private PersonalRedEnvelopeStatusEnum status;

    @NotNull
    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间", required = true, position = 5)
    private LocalDateTime startDateTime;

    @NotNull
    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间", required = true, position = 6)
    private LocalDateTime endDateTime;

    @Override
    public Wrapper<PersonalRedEnvelope> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<PersonalRedEnvelope> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(PersonalRedEnvelope::getUserId, id);
        }

        if (StrUtil.isNotBlank(receiveUsername)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(receiveUsername, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(PersonalRedEnvelope::getReceiveUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(orderNum), PersonalRedEnvelope::getOrderNum, orderNum);
        wrapper.eq(status != null, PersonalRedEnvelope::getStatus, status);
        wrapper.ge(PersonalRedEnvelope::getCreateTime, startDateTime);
        wrapper.le(PersonalRedEnvelope::getCreateTime, endDateTime);

        wrapper.orderByDesc(PersonalRedEnvelope::getId);
        return wrapper;
    }
}
