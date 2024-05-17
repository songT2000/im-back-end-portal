package com.im.common.vo;

import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.UserBalanceSnapshot;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 余额快照，后台显示
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserBalanceSnapshotVO {

    public static final BeanCopier BEAN_COPIER = BeanCopier.create(UserBalanceSnapshot.class, UserBalanceSnapshotVO.class, false);
    public UserBalanceSnapshotVO(UserBalanceSnapshot e, PortalUserCache userCache) {
        BEAN_COPIER.copy(e, this, null);
        this.username = userCache.getUsernameByIdFromLocal(e.getUserId());
    }

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", position = 1)
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", position = 2)
    private String username;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期", position = 5)
    private String date;

    /**
     * 余额
     */
    @ApiModelProperty(value = "余额", position = 6)
    private BigDecimal balance;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", position = 8)
    private LocalDateTime createTime;

}
