package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PortalNavigator;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台导航
 *
 * @author Barry
 * @date 2022-03-25
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalNavigatorPageAdminParam extends AbstractPageParam<PortalNavigator> {
    @ApiModelProperty(value = "名称", position = 1)
    private String name;

    @ApiModelProperty(value = "是否启用", position = 2)
    private Boolean enabled;

    @Override
    public Wrapper<PortalNavigator> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<PortalNavigator> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotBlank(name), PortalNavigator::getName, name);
        wrapper.eq(null != enabled, PortalNavigator::getEnabled, enabled);

        wrapper.orderByAsc(PortalNavigator::getSort);

        return wrapper;
    }
}
