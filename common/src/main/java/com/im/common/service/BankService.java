package com.im.common.service;

import com.im.common.entity.Bank;
import com.im.common.param.BankAddAdminParam;
import com.im.common.param.BankEditAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

/**
 * 银行 服务类
 *
 * @author Barry
 * @date 2019-10-22
 */
public interface BankService extends MyBatisPlusService<Bank> {
    /**
     * 新增银行
     *
     * @param param 参数
     * @return
     */
    RestResponse addForAdmin(BankAddAdminParam param);

    /**
     * 编辑银行
     *
     * @param param 参数
     * @return
     */
    RestResponse editForAdmin(BankEditAdminParam param);
}
