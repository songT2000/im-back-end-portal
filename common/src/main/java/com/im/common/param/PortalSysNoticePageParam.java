package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.SysNotice;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 公告分页
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalSysNoticePageParam extends AbstractPageParam<SysNotice> {
    @NotBlank
    @ApiModelProperty(value = "语言编码", required = true, position = 1)
    private String languageCode;

    /**
     * 获取mybatis查询wrapper，子类实现该方法，返回null则不查询，返回其它则查询
     *
     * @param wrapperParam 封装wrapper时需要的参数
     * @return 返回null则不查询，返回其它则查询
     */
    @Override
    public Wrapper<SysNotice> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(languageCode), SysNotice::getLanguageCode, languageCode);
        wrapper.eq(SysNotice::getShowing, true);

        wrapper.orderByDesc(SysNotice::getTop);
        wrapper.orderByAsc(SysNotice::getSort);
        wrapper.orderByDesc(SysNotice::getUpdateTime);
        return wrapper;
    }
}
