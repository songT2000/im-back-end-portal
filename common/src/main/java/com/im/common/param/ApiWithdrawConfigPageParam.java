package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分页银行卡充值配置列表
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class ApiWithdrawConfigPageParam extends AbstractPageParam<ApiWithdrawConfig> {
    @ApiModelProperty(value = "名称", position = 1)
    private String name;

    @ApiModelProperty(value = "是否启用", position = 2)
    private Boolean enabled;

    @ApiModelProperty(value = "是否查询删除数据，只有当true时条件才有效", position = 3)
    private Boolean deleted;

    @Override
    public Wrapper<ApiWithdrawConfig> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<ApiWithdrawConfig> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(name), ApiWithdrawConfig::getName, name);
        wrapper.eq(enabled != null, ApiWithdrawConfig::getEnabled, enabled);
        wrapper.eq(ApiWithdrawConfig::getDeleted, Boolean.TRUE.equals(deleted));

        wrapper.orderByAsc(ApiWithdrawConfig::getSort);

        return wrapper;
    }
}
