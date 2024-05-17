package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.entity.tim.TimGroup;
import com.im.common.util.CollectionUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 查询好友信息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimFriendPageParam extends AbstractPageParam<TimFriend> {

    @NotNull
    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;

    @ApiModelProperty(value = "好友账号", position = 2)
    private String friendUsername;


    @Override
    public Wrapper<TimFriend> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<TimFriend> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(TimFriend::getUserId,userId);
        if (StrUtil.isNotBlank(friendUsername)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(friendUsername, PortalTypeEnum.PORTAL);
            if (id == null) {
                // 没有查询到用户，不再查询
                return null;
            }

            wrapper.eq(TimFriend::getFriendUserId, id);
        }

        wrapper.orderByDesc(CollectionUtil.newArrayList(TimFriend::getCreateTime));

        return wrapper;
    }
}
