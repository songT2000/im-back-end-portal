package com.im.common.vo;

import com.im.common.entity.UserGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 用户组后台VO
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(UserGroup.class, UserGroupAdminVO.class, false);

    public UserGroupAdminVO(UserGroup e) {
        BEAN_COPIER.copy(e, this, null);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "组名", position = 2)
    private String name;

    @ApiModelProperty(value = "备注", position = 3)
    private String remark;

    @ApiModelProperty(value = "用户个数，点击后弹窗[编辑用户列表]", position = 4)
    private Integer userCount;

    @ApiModelProperty(value = "银行卡充值配置个数，点击后弹窗[编辑银行卡充值配置列表]", position = 5)
    private Integer bankCardRechargeConfigCount;

    @ApiModelProperty(value = "三方充值配置个数，点击后弹窗[编辑三方充值配置列表]", position = 6)
    private Integer apiRechargeConfigCount;

    @ApiModelProperty(value = "创建时间", position = 7)
    private LocalDateTime createTime;
}
