package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.BankCardRechargeConfig;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.BankCardRechargeConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.BankCardRechargeConfigAdminSimpleVO;
import com.im.common.vo.BankCardRechargeConfigAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 银行卡充值配置
 *
 * @author max.stark
 */
@RestController
@Api(tags = "银行卡充值配置相关接口")
public class BankCardRechargeConfigController extends BaseController {
    private BankCardRechargeConfigService bankCardRechargeConfigService;

    @Autowired
    public void setBankCardRechargeConfigService(BankCardRechargeConfigService bankCardRechargeConfigService) {
        this.bankCardRechargeConfigService = bankCardRechargeConfigService;
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_RECHARGE_CONFIG_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("简单列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<BankCardRechargeConfigAdminSimpleVO>> bankCardRechargeConfigSimpleList() {
        List<BankCardRechargeConfig> list = bankCardRechargeConfigService
                .lambdaQuery()
                .eq(BankCardRechargeConfig::getDeleted, false)
                .orderByAsc(BankCardRechargeConfig::getSort)
                .list();
        List<BankCardRechargeConfigAdminSimpleVO> voList = CollectionUtil.toList(list, BankCardRechargeConfigAdminSimpleVO::new);
        return ok(voList);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_RECHARGE_CONFIG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<BankCardRechargeConfigAdminVO>> bankCardRechargeConfigPage(@RequestBody @Valid BankCardRechargeConfigPageParam param) {
        PageVO<BankCardRechargeConfigAdminVO> pageVO = bankCardRechargeConfigService.pageVOForAdmin(param);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_RECHARGE_CONFIG_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_RECHARGE_CONFIG_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse bankCardRechargeConfigAdd(HttpServletRequest request, @RequestBody @Valid BankCardRechargeConfigAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return bankCardRechargeConfigService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_RECHARGE_CONFIG_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_RECHARGE_CONFIG_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse bankCardRechargeConfigEdit(HttpServletRequest request, @RequestBody @Valid BankCardRechargeConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return bankCardRechargeConfigService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_RECHARGE_CONFIG_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_RECHARGE_CONFIG_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse bankCardRechargeConfigDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return bankCardRechargeConfigService.deleteForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_RECHARGE_CONFIG_ENABLE_DISABLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_RECHARGE_CONFIG_ENABLE_DISABLE)
    @CheckPermission(url = ApiUrl.BANK_CARD_RECHARGE_CONFIG_EDIT)
    @ApiOperation("启/禁")
    public RestResponse bankCardRechargeConfigEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return bankCardRechargeConfigService.enableDisableForAdmin(param);
    }
}
