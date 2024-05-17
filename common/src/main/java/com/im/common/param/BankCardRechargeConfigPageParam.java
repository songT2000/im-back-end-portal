package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页银行卡充值配置列表
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardRechargeConfigPageParam extends AbstractPageParam<BankCardRechargeConfig> {

    @ApiModelProperty(value = "名称", position = 1)
    private String name;

    @ApiModelProperty(value = "是否启用", position = 2)
    private Boolean enabled;

    @ApiModelProperty(value = "是否删除", position = 3)
    private Boolean deleted;

    @Override
    public Wrapper<BankCardRechargeConfig> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<BankCardRechargeConfig> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(name), BankCardRechargeConfig::getName, name);
        wrapper.eq(enabled != null, BankCardRechargeConfig::getEnabled, enabled);
        wrapper.eq(BankCardRechargeConfig::getDeleted, Boolean.TRUE.equals(deleted));


        wrapper.orderByAsc(BankCardRechargeConfig::getSort);

        return wrapper;
    }
}
