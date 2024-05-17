package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.Bank;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 银行
 *
 * @author Barry
 * @date 2019-11-20
 */
@Data
@NoArgsConstructor
@ApiModel
public class BankPageAdminParam extends AbstractPageParam<Bank> {
    @ApiModelProperty(value = "名称", position = 1)
    private String name;

    @ApiModelProperty(value = "编码", position = 2)
    private String code;

    @ApiModelProperty(value = "是否启用提现", position = 3)
    private Boolean withdrawEnabled;

    @Override
    public Wrapper<Bank> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<Bank> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotBlank(name), Bank::getName, name);
        wrapper.eq(StrUtil.isNotBlank(code), Bank::getCode, code);
        wrapper.eq(null != withdrawEnabled, Bank::getWithdrawEnabled, withdrawEnabled);

        wrapper.orderByAsc(Bank::getSort);
        wrapper.orderByAsc(Bank::getId);

        return wrapper;
    }
}
