package com.im.common.service;

import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.BankCardRechargeConfigAdminVO;

/**
 * 银行卡充值配置 服务类
 *
 * @author max
 */
public interface BankCardRechargeConfigService extends MyBatisPlusService<BankCardRechargeConfig> {
    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, BankCardRechargeConfigAddParam param);

    /**
     * 编辑
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, BankCardRechargeConfigEditParam param);

    /**
     * 后台分页查询
     *
     * @param param
     * @return
     */
    PageVO<BankCardRechargeConfigAdminVO> pageVOForAdmin(BankCardRechargeConfigPageParam param);

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
