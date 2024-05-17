package com.im.common.vo;

import com.im.common.entity.UserGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 用户组后台简单VO
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupSimpleAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(UserGroup.class, UserGroupSimpleAdminVO.class, false);

    public UserGroupSimpleAdminVO(UserGroup e) {
        BEAN_COPIER.copy(e, this, null);
    }

    public UserGroupSimpleAdminVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "组名", position = 2)
    private String name;
}
