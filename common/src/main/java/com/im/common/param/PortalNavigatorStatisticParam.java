package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PortalNavigatorClick;
import com.im.common.util.mybatis.page.AbstractDateRangePageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台导航统计
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalNavigatorStatisticParam extends AbstractDateRangePageParam<PortalNavigatorClick> {

    @ApiModelProperty(value = "导航ID", position = 1)
    private Long portalNavigatorId;

    @ApiModelProperty(value = "用户账号", position = 2)
    private String username;

    private Long userId;

    @Override
    public Wrapper<PortalNavigatorClick> toQueryWrapper(Object wrapperParam) {
        return null;
    }
}
