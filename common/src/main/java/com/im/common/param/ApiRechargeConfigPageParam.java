package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.enums.RechargeConfigGroupEnum;
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
public class ApiRechargeConfigPageParam extends AbstractPageParam<ApiRechargeConfig> {
    @ApiModelProperty(value = "分组", position = 1)
    private RechargeConfigGroupEnum group;

    @ApiModelProperty(value = "后台名称", position = 2)
    private String adminName;

    @ApiModelProperty(value = "前台名称", position = 3)
    private String portalName;

    @ApiModelProperty(value = "是否启用", position = 5)
    private Boolean enabled;

    @ApiModelProperty(value = "是否查询删除数据，只有当true时条件才有效", position = 6)
    private Boolean deleted;

    @Override
    public Wrapper<ApiRechargeConfig> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<ApiRechargeConfig> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(group != null, ApiRechargeConfig::getGroup, group);
        wrapper.eq(StrUtil.isNotBlank(portalName), ApiRechargeConfig::getPortalName, portalName);
        wrapper.eq(StrUtil.isNotBlank(adminName), ApiRechargeConfig::getAdminName, adminName);
        wrapper.eq(enabled != null, ApiRechargeConfig::getEnabled, enabled);
        wrapper.eq(ApiRechargeConfig::getDeleted, Boolean.TRUE.equals(deleted));

        wrapper.orderByAsc(ApiRechargeConfig::getSort);

        return wrapper;
    }
}
