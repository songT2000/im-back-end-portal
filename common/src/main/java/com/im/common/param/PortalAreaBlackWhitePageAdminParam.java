package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.PortalAreaBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户区域黑名单分页参数
 *
 * @author Barry
 * @date 2021-02-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalAreaBlackWhitePageAdminParam extends AbstractPageParam<PortalAreaBlackWhite> {
    @ApiModelProperty(value = "区域", position = 1)
    private String area;

    @ApiModelProperty(value = "黑白名单", position = 2)
    private BlackWhiteTypeEnum blackWhite;

    @Override
    public Wrapper<PortalAreaBlackWhite> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<PortalAreaBlackWhite> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(getArea()), PortalAreaBlackWhite::getArea, area);
        wrapper.eq(blackWhite != null, PortalAreaBlackWhite::getBlackWhite, blackWhite);

        wrapper.orderByDesc(PortalAreaBlackWhite::getId);
        return wrapper;
    }
}
