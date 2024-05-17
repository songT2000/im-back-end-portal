package com.im.common.vo;

import com.im.common.cache.impl.ApiRechargeConfigCache;
import com.im.common.entity.UserGroupApiRechargeConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

/**
 * 组三方充值配置后台VO
 *
 * @author Barry
 * @date 2021-04-12
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserGroupApiRechargeConfigAdminVO {
    private static final BeanCopier BEAN_COPIER = BeanCopier.create(UserGroupApiRechargeConfig.class, UserGroupApiRechargeConfigAdminVO.class, false);

    public UserGroupApiRechargeConfigAdminVO(UserGroupApiRechargeConfig e, ApiRechargeConfigCache apiRechargeConfigCache) {
        BEAN_COPIER.copy(e, this, null);

        this.apiRechargeConfigName = apiRechargeConfigCache.getAdminNameByIdFromLocal(e.getApiRechargeConfigId());
    }

    @ApiModelProperty(value = "ID，删除的时候传这个ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "三方充值配置ID", position = 2)
    private Long apiRechargeConfigId;

    @ApiModelProperty(value = "三方充值配置名", position = 3)
    private String apiRechargeConfigName;
}
