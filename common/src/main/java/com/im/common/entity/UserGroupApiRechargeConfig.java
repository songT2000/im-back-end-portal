package com.im.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.im.common.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 组三方充值配置，关联到组的数据只能组内可见
 *
 * @author Barry
 * @date 2022-03-12
 */
@Data
@NoArgsConstructor
public class UserGroupApiRechargeConfig extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1680308925714688274L;

    public UserGroupApiRechargeConfig(Long groupId, Long apiRechargeConfigId) {
        this.groupId = groupId;
        this.apiRechargeConfigId = apiRechargeConfigId;
    }

    public UserGroupApiRechargeConfig(Long apiRechargeConfigId) {
        this.apiRechargeConfigId = apiRechargeConfigId;
    }

    /**
     * 分布式ID
     **/
    @TableId
    private Long id;

    /**
     * 组ID
     **/
    private Long groupId;

    /**
     * 三方充值配置ID
     **/
    private Long apiRechargeConfigId;
}
