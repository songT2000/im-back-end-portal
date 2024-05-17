package com.im.common.vo;

import com.im.common.entity.AdminRole;
import com.im.common.entity.AdminUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.redis.RedisSessionUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>后台用户SessionUser</p>
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
@NoArgsConstructor
public class AdminSessionUser extends RedisSessionUser implements Serializable {
    private static final long serialVersionUID = 477936995132559660L;

    private List<AdminRole> roles;

    public AdminSessionUser(AdminUser user, String token, List<AdminRole> roles) {
        this.id = user.getId();
        this.portalType = PortalTypeEnum.ADMIN;
        this.username = user.getUsername();
        this.token = token;
        this.roles = roles;
    }
}
