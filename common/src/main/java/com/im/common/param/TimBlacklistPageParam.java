package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimBlacklist;
import com.im.common.entity.tim.TimFriend;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 查询用户黑名单信息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimBlacklistPageParam extends AbstractPageParam<TimBlacklist> {

    @NotNull
    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;


    @ApiModelProperty(value = "黑名单用户账号", position = 2)
    private String blacklistUsername;


    @Override
    public Wrapper<TimBlacklist> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<TimBlacklist> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(TimBlacklist::getUserId,userId);

        if (StrUtil.isNotBlank(blacklistUsername)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(blacklistUsername, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(TimBlacklist::getBlacklistUserId, id);
        }

        wrapper.orderByDesc(CollectionUtil.newArrayList(TimBlacklist::getCreateTime));

        return wrapper;
    }
}
