package com.im.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群组成员信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TimGroupMemberSimpleVO {

    @ApiModelProperty("成员ID")
    private Long userId;

    @ApiModelProperty("成员账号")
    private String username;

    @ApiModelProperty("成员身份")
    private String role;


}
