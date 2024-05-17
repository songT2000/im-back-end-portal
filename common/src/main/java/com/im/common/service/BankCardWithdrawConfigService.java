package com.im.common.service;

import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.param.BankCardWithdrawConfigAddParam;
import com.im.common.param.BankCardWithdrawConfigEditParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;

/**
 * 银行卡提现配置 服务类
 *
 * @author Barry
 * @date 2021-09-29
 */
public interface BankCardWithdrawConfigService extends MyBatisPlusService<BankCardWithdrawConfig> {
    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, BankCardWithdrawConfigAddParam param);

    /**
     * 编辑
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, BankCardWithdrawConfigEditParam param);

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
