package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.GroupRedEnvelope;
import com.im.common.entity.enums.GroupRedEnvelopeStatusEnum;
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
 * 群红包
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class GroupRedEnvelopePageAdminParam extends AbstractPageParam<GroupRedEnvelope> {
    @ApiModelProperty(value = "订单号", position = 1)
    private String orderNum;

    @ApiModelProperty(value = "组ID", position = 2)
    private Long groupId;

    @ApiModelProperty(value = "用户名", position = 3)
    private String username;

    @ApiModelProperty(value = "状态", position = 4)
    private GroupRedEnvelopeStatusEnum status;

    @NotNull
    @ApiModelProperty(value = "开始时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间", required = true, position = 5)
    private LocalDateTime startDateTime;

    @NotNull
    @ApiModelProperty(value = "结束时间，yyyy-MM-dd HH:mm:ss，默认当天，记得要偏移时间", required = true, position = 6)
    private LocalDateTime endDateTime;

    @Override
    public Wrapper<GroupRedEnvelope> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<GroupRedEnvelope> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(GroupRedEnvelope::getUserId, id);
        }

        wrapper.eq(StrUtil.isNotBlank(orderNum), GroupRedEnvelope::getOrderNum, orderNum);
        wrapper.eq(groupId != null, GroupRedEnvelope::getGroupId, groupId);
        wrapper.eq(status != null, GroupRedEnvelope::getStatus, status);
        wrapper.ge(GroupRedEnvelope::getCreateTime, startDateTime);
        wrapper.le(GroupRedEnvelope::getCreateTime, endDateTime);

        wrapper.orderByDesc(GroupRedEnvelope::getId);
        return wrapper;
    }
}
