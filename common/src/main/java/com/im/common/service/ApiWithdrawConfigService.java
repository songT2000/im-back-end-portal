package com.im.common.service;

import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.param.ApiWithdrawConfigAddParam;
import com.im.common.param.ApiWithdrawConfigEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * API代付配置 服务类
 *
 * @author Barry
 * @date 2021-09-29
 */
public interface ApiWithdrawConfigService extends MyBatisPlusService<ApiWithdrawConfig> {
    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, ApiWithdrawConfigAddParam param);

    /**
     * 编辑
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, ApiWithdrawConfigEditParam param);


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
