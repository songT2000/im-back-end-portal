package com.im.common.param;

import com.im.common.entity.enums.TrendsTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 朋友圈添加参数
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsAddParam {

    @NotBlank
    @ApiModelProperty(value = "内容", required = true)
    private String content;

    @ApiModelProperty(value = "图片链接地址，最多9个")
    private List<String> imgUrls;

    @ApiModelProperty(value = "视频链接地址，只允许一个")
    private String videoUrls;

    @ApiModelProperty(value = "发布的IP地址")
    private String ip;

    @ApiModelProperty(value = "发布的位置")
    private String address;

    @NotNull
    @ApiModelProperty(value = "朋友圈权限", required = true)
    private TrendsTypeEnum trendsType;

    @ApiModelProperty(value = "提醒谁看，用户ID")
    private List<Long> callUserIds;

    @ApiModelProperty(value = "可见用户id")
    private List<Long> allowUserIds;

    @ApiModelProperty(value = "不可见用id")
    private List<Long> notAllowUserIds;
}
