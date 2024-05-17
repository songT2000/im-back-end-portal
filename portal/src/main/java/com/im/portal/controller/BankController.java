package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.BankCache;
import com.im.common.entity.Bank;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.BankCommonVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * 银行Controller
 *
 * @author Barry
 * @date 2019/10/12
 */
@RestController
@Api(tags = "银行相关接口")
@ApiSupport(order = 3)
public class BankController extends BaseController {
    private BankCache bankCache;

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @RequestMapping(value = ApiUrl.BANK_SIMPLE_WITHDRAW_LIST, method = RequestMethod.POST)
    @ApiOperation("所有提现银行列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<BankCommonVO>> bankSimpleWithdrawList() {
        List<Bank> list = bankCache.listFromRedis();
        list = CollectionUtil.filterList(list, e -> Boolean.TRUE.equals(e.getWithdrawEnabled()));
        list = CollectionUtil.sort(list, Comparator.comparingInt(Bank::getSort));
        List<BankCommonVO> bankCommonVOS = CollectionUtil.toList(list, BankCommonVO::new);
        return ok(bankCommonVOS);
    }
}
