package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserGroupBankCardRechargeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户组银行卡充值配置列表
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupBankCardRechargeConfigListAdminParam {
    @NotNull
    @ApiModelProperty(value = "组ID", required = true, position = 1)
    private Long groupId;

    public Wrapper<UserGroupBankCardRechargeConfig> toQueryWrapper() {
        LambdaQueryWrapper<UserGroupBankCardRechargeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroupBankCardRechargeConfig::getGroupId, groupId);

        wrapper.orderByDesc(UserGroupBankCardRechargeConfig::getId);

        return wrapper;
    }
}
