package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.entity.GroupRedEnvelopeReceive;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 群红包领取记录
 *
 * @author Barry
 * @date 2020-05-26
 */
@Data
@NoArgsConstructor
@ApiModel
public class GroupRedEnvelopeReceivePageAdminParam extends AbstractPageParam<GroupRedEnvelopeReceive> {
    @NotNull
    @ApiModelProperty(value = "红包ID", required = true, position = 1)
    private Long envelopeId;

    @Override
    public Wrapper<GroupRedEnvelopeReceive> toQueryWrapper(Object wrapperParam) {
        LambdaQueryWrapper<GroupRedEnvelopeReceive> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(GroupRedEnvelopeReceive::getEnvelopeId, envelopeId);

        wrapper.orderByDesc(GroupRedEnvelopeReceive::getId);
        return wrapper;
    }
}
