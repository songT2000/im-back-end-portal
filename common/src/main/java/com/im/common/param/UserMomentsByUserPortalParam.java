package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.UserBill;
import com.im.common.entity.UserMoments;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 前台某个好友朋友圈
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsByUserPortalParam extends AbstractPageParam<UserMoments> {

    @NotBlank
    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    /**
     * 获取mybatis查询wrapper，子类实现该方法，返回null则不查询，返回其它则查询
     *
     * @param wrapperParam 封装wrapper时需要的参数
     * @return 返回null则不查询，返回其它则查询
     */
    @Override
    public Wrapper<UserMoments> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<UserMoments> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(UserMoments::getUserId, id);
            wrapper.orderByDesc(UserMoments::getCreateTime);
        }
        return wrapper;
    }
}
