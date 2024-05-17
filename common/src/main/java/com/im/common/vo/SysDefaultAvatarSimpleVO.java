package com.im.common.vo;

import com.im.common.entity.SysDefaultAvatar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 系统默认头像VO
 *
 * @author Barry
 * @date 2022-04-07
 */
@Data
@ApiModel
public class SysDefaultAvatarSimpleVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(SysDefaultAvatar.class, SysDefaultAvatarSimpleVO.class, false);

    public SysDefaultAvatarSimpleVO(SysDefaultAvatar e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "URL", position = 2)
    private String url;
}
