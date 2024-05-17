package com.im.common.vo;

import com.im.common.entity.PortalUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台用户VO
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalUserSimpleAdminVO {
    public PortalUserSimpleAdminVO(PortalUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;
}
