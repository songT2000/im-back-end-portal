package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PortalUserDeviceInfo;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 设备信息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserDeviceInfoPageParam extends AbstractPageParam<PortalUserDeviceInfo> {

    @NotBlank
    @ApiModelProperty(value = "账号", required = true)
    private String username;


    @Override
    public Wrapper<PortalUserDeviceInfo> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<PortalUserDeviceInfo> wrapper = new LambdaQueryWrapper<>();
        Long userId = UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL);
        wrapper.eq(PortalUserDeviceInfo::getUserId, userId)
                .orderByDesc(PortalUserDeviceInfo::getUpdateTime);
        return wrapper;
    }
}
