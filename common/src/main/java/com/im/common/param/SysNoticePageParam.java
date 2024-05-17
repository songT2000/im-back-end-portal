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

/**
 * 公告分页
 *
 * @author max.stark
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysNoticePageParam extends AbstractPageParam<SysNotice> {
    @ApiModelProperty(value = "公告标题", position = 1)
    private String title;

    @ApiModelProperty(value = "是否显示", position = 2)
    private Boolean showing;

    @ApiModelProperty(value = "是否置顶", position = 3)
    private Boolean top;

    @ApiModelProperty(value = "语言编码", position = 4)
    private String languageCode;

    @Override
    public Wrapper<SysNotice> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<SysNotice> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotBlank(title), SysNotice::getTitle, title);
        wrapper.eq(showing != null, SysNotice::getShowing, showing);
        wrapper.eq(top != null, SysNotice::getTop, top);
        wrapper.eq(StrUtil.isNotBlank(languageCode), SysNotice::getLanguageCode, languageCode);

        // 默认按照置顶，排序，最后修改时间 进行降序
        wrapper.orderByDesc(SysNotice::getShowing);
        wrapper.orderByDesc(SysNotice::getTop);
        wrapper.orderByAsc(SysNotice::getSort);
        wrapper.orderByDesc(SysNotice::getUpdateTime);

        return wrapper;
    }
}
