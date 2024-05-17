package com.im.common.vo;

import com.im.common.entity.AdminUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台管理用户VO
 *
 * @author Barry
 * @date 2019-11-15
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminUserVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(AdminUser.class, AdminUserVO.class, false);

    public AdminUserVO(AdminUser adminUser) {
        BEAN_COPIER.copy(adminUser, this, null);
    }

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("上次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("上次登录IP")
    private String lastLoginIp;

    @ApiModelProperty("上次登录地址")
    private String lastLoginArea;

    @ApiModelProperty("登录密码输入错误次数")
    private Integer pwdErrorTimes;

    @ApiModelProperty("是否绑定谷歌")
    private Boolean googleBound;

    @ApiModelProperty("是否在线")
    private Boolean online;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否启用")
    private Boolean enabled;

    @ApiModelProperty("是否已删除")
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("角色列表")
    private List<AdminRoleVO> roles;

    @ApiModelProperty("是否有管理员角色")
    private Boolean hasAdminRole;
}
