package com.im.common.vo;

import com.im.common.entity.PortalNavigator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 前台导航VO
 *
 * @author Barry
 * @date 2022-03-25
 */
@Data
@ApiModel
public class PortalNavigatorPortalVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalNavigator.class, PortalNavigatorPortalVO.class, false);

    public PortalNavigatorPortalVO(PortalNavigator e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "名称", position = 2)
    private String name;

    @ApiModelProperty(value = "链接", position = 3)
    private String url;
}
