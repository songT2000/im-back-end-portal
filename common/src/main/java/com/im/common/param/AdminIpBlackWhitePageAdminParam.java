package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.AdminIpBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台白名单查询参数
 *
 * @author James
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminIpBlackWhitePageAdminParam extends AbstractPageParam<AdminIpBlackWhite> {
    @ApiModelProperty(value = "IP", position = 1)
    private String ip;

    @ApiModelProperty(value = "黑白名单", position = 2)
    private BlackWhiteTypeEnum blackWhite;

    @Override
    public Wrapper<AdminIpBlackWhite> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<AdminIpBlackWhite> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(ip), AdminIpBlackWhite::getIp, ip);
        wrapper.eq(blackWhite != null, AdminIpBlackWhite::getBlackWhite, blackWhite);

        wrapper.orderByDesc(AdminIpBlackWhite::getId);
        return wrapper;
    }
}
