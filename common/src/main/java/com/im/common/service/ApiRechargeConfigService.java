package com.im.common.service;

import com.im.common.entity.ApiRechargeConfig;
import com.im.common.param.ApiRechargeConfigAddParam;
import com.im.common.param.ApiRechargeConfigEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 三方充值配置 服务类
 *
 * @author Barry
 * @date 2021-09-29
 */
public interface ApiRechargeConfigService extends MyBatisPlusService<ApiRechargeConfig> {
    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, ApiRechargeConfigAddParam param);

    /**
     * 编辑
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, ApiRechargeConfigEditParam param);

    /**
     * 软删除
     *
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param);

    /**
     * 启/禁
     *
     * @param param
     * @return
     */
    RestResponse enableDisableForAdmin(IdEnableDisableParam param);
}
