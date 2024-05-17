package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserGroupUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 用户组分页
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupUserListAdminParam {
    @NotNull
    @ApiModelProperty(value = "组ID", required = true, position = 1)
    private Long groupId;

    public Wrapper<UserGroupUser> toQueryWrapper() {
        LambdaQueryWrapper<UserGroupUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroupUser::getGroupId, groupId);

        wrapper.orderByDesc(UserGroupUser::getId);

        return wrapper;
    }
}
