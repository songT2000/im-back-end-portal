package com.im.common.param;

import com.im.common.entity.enums.TrendsReviewTypeEnum;
import com.im.common.entity.enums.TrendsTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 朋友圈 点赞添加参数
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsReviewLikeAddParam {

    @NotBlank
    @ApiModelProperty(value = "朋友圈ID", required = true)
    private Long momentsId;

    @ApiModelProperty(value = "发布的IP地址")
    private String ip;

}
