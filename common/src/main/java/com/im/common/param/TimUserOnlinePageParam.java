package com.im.common.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.enums.ActionEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询在线用户分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimUserOnlinePageParam extends AbstractPageParam<TimUserDeviceState> {

    @ApiModelProperty(value = "用户账号")
    private String username;

    @Override
    public Wrapper<TimUserDeviceState> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<TimUserDeviceState> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(username)) {
            Long id = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
            if (id == null) {
                return null;
            }
            wrapper.eq(TimUserDeviceState::getUserId, id);
        }
        // 只查在线用户
        wrapper.eq(TimUserDeviceState::getAction, ActionEnum.Login);
        wrapper.orderByDesc(TimUserDeviceState::getCreateTime);

        return wrapper;
    }
}
