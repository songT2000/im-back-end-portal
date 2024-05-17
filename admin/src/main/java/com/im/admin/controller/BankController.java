package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.Bank;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.BankAddAdminParam;
import com.im.common.param.BankEditAdminParam;
import com.im.common.param.BankPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.BankService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.BankAdminVO;
import com.im.common.vo.BankCommonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
public class BankController extends BaseController {
    private BankService bankService;

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    @RequestMapping(value = ApiUrl.BANK_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("所有银行列表")
    public RestResponse<List<BankCommonVO>> bankSimpleList() {
        List<Bank> banks = bankService.list();
        banks = CollectionUtil.sort(banks, Comparator.comparingInt(Bank::getSort));
        List<BankCommonVO> bankCommonVOS = CollectionUtil.toList(banks, BankCommonVO::new);
        return ok(bankCommonVOS);
    }

    @RequestMapping(value = ApiUrl.BANK_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("系统银行分页")
    public RestResponse<PageVO<BankAdminVO>> bankPage(@RequestBody @Valid BankPageAdminParam param) {
        PageVO<BankAdminVO> pageVO = bankService.pageVO(param, BankAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.BANK_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_ADD)
    @ApiOperation("新增银行")
    public RestResponse bankAdd(@RequestBody @Valid BankAddAdminParam param) {
        return bankService.addForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.BANK_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_EDIT)
    @ApiOperation("编辑银行")
    public RestResponse bankEdit(@RequestBody @Valid BankEditAdminParam param) {
        return bankService.editForAdmin(param);
    }
}
