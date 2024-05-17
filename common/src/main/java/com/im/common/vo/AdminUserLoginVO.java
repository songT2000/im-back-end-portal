package com.im.common.vo;

import com.im.common.entity.AdminMenu;
import com.im.common.entity.AdminUser;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.util.CollectionUtil;
import com.im.common.util.jwt.JwtUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>后台用户登录时返回给前台的信息</p>
 *
 * @author Barry
 * @date 2019-10-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminUserLoginVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(AdminUser.class, AdminUserLoginVO.class, false);

    public AdminUserLoginVO(AdminSessionUser sessionUser, AdminUser user, List<AdminMenu> adminMenus) {
        BEAN_COPIER.copy(user, this, null);

        this.token = sessionUser.getToken();
        this.loginTime = sessionUser.getLoginTime();
        this.loginIp = sessionUser.getLoginIp();
        this.loginArea = sessionUser.getLoginArea();
        this.loginDeviceId = sessionUser.getLoginDeviceId();
        this.loginDeviceType = sessionUser.getLoginDeviceType();

        this.roleNames = CollectionUtil.toList(sessionUser.getRoles(), role -> role.getName());
        this.menus = CollectionUtil.toList(adminMenus, AdminMenuVO::new);
    }

    @ApiModelProperty(value = "用户名", position = 1)
    private String username;

    @ApiModelProperty(value = "角色名", position = 2)
    private List<String> roleNames;

    @ApiModelProperty(value = "拥有的菜单及权限", position = 3)
    private List<AdminMenuVO> menus;

    @ApiModelProperty(value = "本次登录时间", position = 4)
    private LocalDateTime loginTime;

    @ApiModelProperty(value = "本次登录IP", position = 5)
    private String loginIp;

    @ApiModelProperty(value = "本次登录区域", position = 6)
    private String loginArea;

    @ApiModelProperty(value = "本次登录设备标识", position = 7)
    private String loginDeviceId;

    @ApiModelProperty(value = "本次登录设备类型", position = 8)
    private DeviceTypeEnum loginDeviceType;

    @ApiModelProperty(value = "token，保存在本地，并且每个/auth请求都放到header的" + JwtUtil.AUTH_HEADER + "里", position = 9)
    private String token;
}
