package com.im.common.vo;

import com.im.common.entity.PortalNavigator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 前台导航统计VO
 */
@Data
@ApiModel
public class PortalNavigatorStatisticVO {

    public PortalNavigatorStatisticVO(PortalNavigator e) {
        this.portalNavigatorId = e.getId();
        this.url = e.getUrl();
        this.name = e.getName();
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long portalNavigatorId;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "链接", position = 3)
    private String url;

    @ApiModelProperty(value = "点击人数", position = 4)
    private Long clickCount;

}
