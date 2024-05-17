package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.SensitiveWord;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 敏感词分页
 */
@Data
@NoArgsConstructor
@ApiModel
public class SensitiveWordPageParam extends AbstractPageParam<SensitiveWord> {

    @ApiModelProperty(value = "自动回复内容", position = 1)
    private String word;

    @Override
    public Wrapper<SensitiveWord> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<SensitiveWord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(StrUtil.isNotBlank(word), SensitiveWord::getWord, word);

        wrapper.orderByDesc(CollectionUtil.newArrayList(SensitiveWord::getCreateTime, SensitiveWord::getId));

        return wrapper;
    }
}
