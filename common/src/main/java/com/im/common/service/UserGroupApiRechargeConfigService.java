package com.im.common.service;

import com.im.common.entity.UserGroupApiRechargeConfig;
import com.im.common.param.UserGroupEditRelationIdAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 组三方充值配置 服务类
 *
 * @author Barry
 * @date 2021-04-11
 */
public interface UserGroupApiRechargeConfigService extends MyBatisPlusService<UserGroupApiRechargeConfig> {
    /**
     * 编辑
     *
     * @param param
     * @return
     */
    RestResponse editForAdmin(UserGroupEditRelationIdAdminParam param);
}
