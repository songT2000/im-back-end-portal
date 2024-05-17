package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserGroupApiRechargeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户组三方充值配置列表
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupApiRechargeConfigListAdminParam {
    @NotNull
    @ApiModelProperty(value = "组ID", required = true, position = 1)
    private Long groupId;

    public Wrapper<UserGroupApiRechargeConfig> toQueryWrapper() {
        LambdaQueryWrapper<UserGroupApiRechargeConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroupApiRechargeConfig::getGroupId, groupId);

        wrapper.orderByDesc(UserGroupApiRechargeConfig::getId);

        return wrapper;
    }
}
