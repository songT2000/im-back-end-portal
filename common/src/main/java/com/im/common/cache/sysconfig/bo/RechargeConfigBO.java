package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 充值配置
 *
 * @author Barry
 * @date 2021-01-18
 */
@Data
@NoArgsConstructor
public class RechargeConfigBO extends BaseSysConfigBO {
    /**
     * 是否允许充值，禁用后所有用户都不能提交新的充值订单
     */
    private Boolean enabled;

    /**
     * 充值是否必须先绑定资金密码
     */
    private Boolean fundPwdBindRequired;

    /**
     * 充值是否必须先绑定银行卡
     */
    private Boolean bankCardBindRequired;
}
