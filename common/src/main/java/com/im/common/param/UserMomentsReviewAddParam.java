package com.im.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 朋友圈 点赞添加参数
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsReviewAddParam {

    @NotBlank
    @ApiModelProperty(value = "朋友圈ID", required = true)
    private Long momentsId;

    @NotBlank
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @ApiModelProperty(value = "回复用户ID")
    private Long replyUserId;

    @NotBlank
    @ApiModelProperty(value = "内容", required = true)
    private String content;

    @ApiModelProperty(value = "发布的IP地址")
    private String ip;

}
