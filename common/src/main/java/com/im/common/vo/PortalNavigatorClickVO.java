package com.im.common.vo;

import com.im.common.entity.PortalUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 前台导航点击数据VO
 */
@Data
@ApiModel
public class PortalNavigatorClickVO {

    @ApiModelProperty(value = "名称", position = 1)
    private String username;

    @ApiModelProperty(value = "昵称", position = 2)
    private String nickname;

    @ApiModelProperty(value = "头像", position = 3)
    private String avatar;

    @ApiModelProperty(value = "点击时间", position = 4)
    private LocalDateTime createTime;

}
