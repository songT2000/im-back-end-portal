package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserGroup;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户组分页
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupPageAdminParam extends AbstractPageParam<UserGroup> {
    @ApiModelProperty(value = "组名", position = 1)
    private String name;

    @Override
    public Wrapper<UserGroup> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserGroup> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotBlank(name), UserGroup::getName, name);

        wrapper.orderByDesc(UserGroup::getId);
        return wrapper;
    }
}
