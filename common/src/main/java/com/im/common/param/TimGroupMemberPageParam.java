package com.im.common.param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.im.common.entity.tim.TimGroupMember;
import com.im.common.util.mybatis.page.AbstractPageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 查询群组成员信息分页参数
 */
@Data
@NoArgsConstructor
@ApiModel
public class TimGroupMemberPageParam extends AbstractPageParam<TimGroupMember> {

    @NotBlank
    @ApiModelProperty(value = "群ID", position = 1)
    private String groupId;

    @ApiModelProperty(value = "成员账号或昵称", position = 2)
    private String username;


    @Override
    public Wrapper<TimGroupMember> toQueryWrapper(Object wrapperParam) {
        return null;
    }
}
