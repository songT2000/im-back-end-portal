package com.im.common.service.impl;

import com.im.common.cache.base.CacheProxy;
import com.im.common.entity.Bank;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.mapper.BankMapper;
import com.im.common.param.BankAddAdminParam;
import com.im.common.param.BankEditAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.BankService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 银行 服务实现类
 *
 * @author Barry
 * @date 2019-10-22
 */
@Service
public class BankServiceImpl
        extends MyBatisPlusServiceImpl<BankMapper, Bank>
        implements BankService {
    private CacheProxy cacheProxy;

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(BankAddAdminParam param) {
        // 检查名称是否重复
        Bank nameOne = lambdaQuery().eq(Bank::getName, param.getName()).one();
        if (nameOne != null) {
            return RestResponse.failed(ResponseCode.BANK_NAME_EXISTED);
        }

        // 检查CODE是否重复
        Bank codeOne = lambdaQuery().eq(Bank::getCode, param.getCode()).one();
        if (codeOne != null) {
            return RestResponse.failed(ResponseCode.BANK_CODE_EXISTED);
        }

        Bank bank = new Bank();
        bank.setName(param.getName());
        bank.setCode(param.getCode());
        bank.setSort(param.getSort());
        bank.setWithdrawEnabled(param.getWithdrawEnabled());

        boolean saved = save(bank);

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK);

        return saved ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(BankEditAdminParam param) {
        // 检查名称是否重复
        Bank nameOne = lambdaQuery().eq(Bank::getName, param.getName()).ne(Bank::getId, param.getId()).one();
        if (nameOne != null) {
            return RestResponse.failed(ResponseCode.BANK_NAME_EXISTED);
        }

        boolean updated = lambdaUpdate()
                .eq(Bank::getId, param.getId())
                .set(Bank::getName, param.getName())
                .set(Bank::getSort, param.getSort())
                .set(Bank::getWithdrawEnabled, param.getWithdrawEnabled())
                .update();

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.BANK);

        return updated ? RestResponse.OK : RestResponse.SYS_DATA_STATUS_ERROR;
    }
}
