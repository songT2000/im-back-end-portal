package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.AppAutoReplyConfig;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自动回复分页
 */
@Data
@NoArgsConstructor
@ApiModel
public class AppAutoReplyConfigPageParam extends AbstractPageParam<AppAutoReplyConfig> {

    @ApiModelProperty(value = "自动回复内容", position = 1)
    private String content;

    @Override
    public Wrapper<AppAutoReplyConfig> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<AppAutoReplyConfig> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(content), AppAutoReplyConfig::getContent, content);

        wrapper.orderByDesc(CollectionUtil.newArrayList(AppAutoReplyConfig::getCreateTime, AppAutoReplyConfig::getId));

        return wrapper;
    }
}
