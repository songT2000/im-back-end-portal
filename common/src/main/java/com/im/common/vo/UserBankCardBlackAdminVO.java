package com.im.common.vo;

import com.im.common.cache.impl.AdminUserCache;
import com.im.common.entity.UserBankCardBlack;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 用户卡黑名单管理后台VO
 *
 * @author Barry
 * @date 2020-05-27
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBankCardBlackAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserBankCardBlack.class, UserBankCardBlackAdminVO.class, false);

    public UserBankCardBlackAdminVO(UserBankCardBlack card, AdminUserCache adminUserCache) {
        BEAN_COPIER.copy(card, this, null);
        this.createAdminUsername = adminUserCache.getUsernameByIdFromLocal(card.getCreateAdminId());
        if (card.getUpdateAdminId() != null) {
            this.updateAdminUsername = adminUserCache.getUsernameByIdFromLocal(card.getUpdateAdminId());
        } else {
            this.updateTime = null;
        }
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "卡姓名", position = 2)
    private String cardName;

    @ApiModelProperty(value = "卡号", position = 3)
    private String cardNum;

    @ApiModelProperty(value = "备注，列稍微长一点", position = 4)
    private String remark;

    @ApiModelProperty(value = "创建人，[列名显示创建人，实际值createAdminUsername/createTime]", position = 5)
    private String createAdminUsername;

    @ApiModelProperty(value = "创建时间，[列名显示创建人，实际值createAdminUsername/createTime]", position = 6)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人，[列名显示修改人，实际值updateAdminUsername/updateTime，有可能为空]", position = 7)
    private String updateAdminUsername;

    @ApiModelProperty(value = "修改时间，[列名显示修改人，实际值updateAdminUsername/updateTime，有可能为空]", position = 8)
    private LocalDateTime updateTime;
}
