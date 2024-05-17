package com.im.common.vo;

import com.im.common.entity.UserMomentsCallTrends;
import com.im.common.entity.UserMomentsReview;
import com.im.common.entity.UserMomentsTrends;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 动态提醒
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserMomentsTrendsVO {

    @ApiModelProperty("总提醒量")
    private Integer sumCount;

    @ApiModelProperty("朋友圈提醒你看，朋友圈ID")
    List<Long> callTrends;

    @ApiModelProperty("评论提醒数量，K=朋友圈ID，V=回复或者点赞数")
    Map<Long, Integer> reviews;
}
