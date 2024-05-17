package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PortalIpBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户IP黑白名单分页参数
 *
 * @author Max
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalIpBlackWhitePageAdminParam extends AbstractPageParam<PortalIpBlackWhite> {
    @ApiModelProperty(value = "IP", position = 1)
    private String ip;

    @ApiModelProperty(value = "黑白名单", position = 2)
    private BlackWhiteTypeEnum blackWhite;

    @Override
    public Wrapper<PortalIpBlackWhite> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<PortalIpBlackWhite> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(ip), PortalIpBlackWhite::getIp, ip);
        wrapper.eq(blackWhite != null, PortalIpBlackWhite::getBlackWhite, blackWhite);

        wrapper.orderByDesc(PortalIpBlackWhite::getId);
        return wrapper;
    }
}
