package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改公告
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class SysNoticeEditParam {
    @NotNull
    @ApiModelProperty(value = "ID", required = true, position = 1)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "公告标题", required = true, position = 2)
    private String title;

    @NotBlank
    @ApiModelProperty(value = "公告简介", required = true, position = 3)
    private String simpleContent;

    @NotBlank
    @ApiModelProperty(value = "公告内容", required = true, position = 4)
    private String content;

    @NotNull
    @ApiModelProperty(value = "排序", required = true, position = 5)
    private Integer sort;

    @NotNull
    @ApiModelProperty(value = "是否显示", required = true, position = 6)
    private Boolean showing;

    @NotNull
    @ApiModelProperty(value = "是否置顶", required = true, position = 7)
    private Boolean top;

    @NotBlank
    @ApiModelProperty(value = "国际化编码", required = true, position = 8)
    private String languageCode;

    /**
     * 如果用户绑定了谷歌，并且系统开启了谷歌，则需要输入验证码
     */
    @ApiModelProperty(value = "谷歌验证码，6位数字，通过/api/{portal/agent/admin}/user/has-bound-google来获取用户是否绑定了谷歌", position = 9)
    private Integer googleCode;
}
