package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.AdminUser;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台用户列表
 *
 * @author Barry
 * @date 2019-11-11
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminUserPageParam extends AbstractPageParam<AdminUser> {
    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "是否在线")
    private Boolean online;

    @ApiModelProperty(value = "是否查询删除数据，只有当true时条件才有效")
    private Boolean deleted;

    @Override
    public Wrapper<AdminUser> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(username), AdminUser::getUsername, username);
        wrapper.eq(online != null, AdminUser::getOnline, true);
        wrapper.eq(AdminUser::getDeleted, Boolean.TRUE.equals(deleted));

        // 无所谓排序，不处理

        return wrapper;
    }
}
