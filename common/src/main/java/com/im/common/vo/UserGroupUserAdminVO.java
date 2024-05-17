package com.im.common.vo;

import com.im.common.entity.UserGroupUser;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 用户组内用户后台VO
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupUserAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(UserGroupUser.class, UserGroupUserAdminVO.class, false);

    public UserGroupUserAdminVO(UserGroupUser e) {
        BEAN_COPIER.copy(e, this, null);

        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
    }

    @ApiModelProperty(value = "ID，删除的时候传这个ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    @ApiModelProperty(value = "加入时间", position = 3)
    private LocalDateTime createTime;
}
