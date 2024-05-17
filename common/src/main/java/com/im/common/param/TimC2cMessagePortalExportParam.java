package com.im.common.param;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 前台导出单聊记录参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimC2cMessagePortalExportParam {

    @NotNull
    @ApiModelProperty(value = "开始日期，yyyy-MM-dd", required = true, position = 1)
    protected LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "结束日期，yyyy-MM-dd", required = true, position = 2)
    protected LocalDate endDate;

    @NotNull
    @ApiModelProperty(value = "参与人1的账号")
    private String usernamePart1;

    @NotNull
    @ApiModelProperty(value = "参与人2的账号")
    private String usernamePart2;

    public Wrapper<TimMessageC2c> toQueryWrapper() {
        LambdaQueryWrapper<TimMessageC2c> wrapper = new LambdaQueryWrapper<>();
        Long id1 = UserUtil.getUserIdByUsernameFromLocal(usernamePart1, PortalTypeEnum.PORTAL);
        Long id2 = UserUtil.getUserIdByUsernameFromLocal(usernamePart2, PortalTypeEnum.PORTAL);

        List<Long> userIds = ListUtil.of(id1, id2);

        wrapper.in(TimMessageC2c::getFromUserId, userIds)
                .in(TimMessageC2c::getToUserId, userIds);

        wrapper.ge(startDate != null, TimMessageC2c::getSendTime, startDate);
        wrapper.le(endDate != null, TimMessageC2c::getSendTime, LocalDateTimeUtil.getLocalDateEndOfDay(endDate));

        wrapper.orderByDesc(CollectionUtil.newArrayList(TimMessageC2c::getSendTime));

        return wrapper;
    }
}
