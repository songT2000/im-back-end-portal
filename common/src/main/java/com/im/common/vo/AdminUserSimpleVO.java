package com.im.common.vo;

import com.im.common.entity.AdminUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台管理用户VO
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminUserSimpleVO {
    public AdminUserSimpleVO(AdminUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;
}
