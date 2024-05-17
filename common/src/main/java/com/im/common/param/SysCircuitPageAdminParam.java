package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.SysCircuit;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统测速地址分页
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysCircuitPageAdminParam extends AbstractPageParam<SysCircuit> {
    @ApiModelProperty(value = "系统测速地址", position = 1)
    private String circuitUrl;

    @ApiModelProperty(value = "类型", position = 2)
    private Integer circuitType;

    @ApiModelProperty(value = "是否启用", position = 3)
    private Boolean enabled;

    @Override
    public Wrapper<SysCircuit> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<SysCircuit> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotBlank(circuitUrl), SysCircuit::getCircuitUrl, circuitUrl);
        wrapper.eq(enabled != null, SysCircuit::getEnabled, enabled);
        wrapper.eq(circuitType != null, SysCircuit::getCircuitType, circuitType);

        return wrapper;
    }
}
