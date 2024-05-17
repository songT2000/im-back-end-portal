package com.im.common.service;

import com.im.common.entity.UserGroupBankCardRechargeConfig;
import com.im.common.param.UserGroupEditRelationIdAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 组银行卡充值配置 服务类
 *
 * @author Barry
 * @date 2021-04-11
 */
public interface UserGroupBankCardRechargeConfigService extends MyBatisPlusService<UserGroupBankCardRechargeConfig> {
    /**
     * 编辑
     *
     * @param param
     * @return
     */
    RestResponse editForAdmin(UserGroupEditRelationIdAdminParam param);
}
