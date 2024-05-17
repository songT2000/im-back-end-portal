package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页银行卡提现配置列表
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankCardWithdrawConfigPageParam extends AbstractPageParam<BankCardWithdrawConfig> {
    @ApiModelProperty(value = "名称", position = 1)
    private String name;

    @ApiModelProperty(value = "是否启用", position = 2)
    private Boolean enabled;

    @ApiModelProperty(value = "是否删除", position = 3)
    private Boolean deleted;

    @Override
    public Wrapper<BankCardWithdrawConfig> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<BankCardWithdrawConfig> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(name), BankCardWithdrawConfig::getName, name);
        wrapper.eq(enabled != null, BankCardWithdrawConfig::getEnabled, enabled);
        wrapper.eq(BankCardWithdrawConfig::getDeleted, Boolean.TRUE.equals(deleted));

        wrapper.orderByAsc(BankCardWithdrawConfig::getSort);
        return wrapper;
    }
}
