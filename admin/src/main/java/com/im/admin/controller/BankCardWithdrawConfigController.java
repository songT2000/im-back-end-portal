package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.BankCardWithdrawConfig;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.BankCardWithdrawConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.BankCardWithdrawConfigAdminSimpleVO;
import com.im.common.vo.BankCardWithdrawConfigAdminVO;
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
@Api(tags = "银行卡充值配置后台接口")
public class BankCardWithdrawConfigController extends BaseController {
    private BankCardWithdrawConfigService bankCardWithdrawConfigService;

    @Autowired
    public void setBankCardWithdrawConfigService(BankCardWithdrawConfigService bankCardWithdrawConfigService) {
        this.bankCardWithdrawConfigService = bankCardWithdrawConfigService;
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("简单列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<BankCardWithdrawConfigAdminSimpleVO>> bankCardWithdrawConfigSimpleList() {
        List<BankCardWithdrawConfig> list = bankCardWithdrawConfigService
                .lambdaQuery()
                .eq(BankCardWithdrawConfig::getDeleted, false)
                .orderByAsc(BankCardWithdrawConfig::getSort)
                .list();
        List<BankCardWithdrawConfigAdminSimpleVO> voList = CollectionUtil.toList(list, BankCardWithdrawConfigAdminSimpleVO::new);
        return ok(voList);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<BankCardWithdrawConfigAdminVO>> bankCardWithdrawConfigPage(@RequestBody @Valid BankCardWithdrawConfigPageParam param) {
        PageVO<BankCardWithdrawConfigAdminVO> pageVO = bankCardWithdrawConfigService.pageVO(param, e -> new BankCardWithdrawConfigAdminVO(e));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_WITHDRAW_CONFIG_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse bankCardWithdrawConfigAdd(HttpServletRequest request, @RequestBody @Valid BankCardWithdrawConfigAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return bankCardWithdrawConfigService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_WITHDRAW_CONFIG_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse bankCardWithdrawConfigEdit(HttpServletRequest request, @RequestBody @Valid BankCardWithdrawConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return bankCardWithdrawConfigService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_WITHDRAW_CONFIG_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse bankCardWithdrawConfigDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return bankCardWithdrawConfigService.deleteForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_ENABLE_DISABLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.BANK_CARD_WITHDRAW_CONFIG_ENABLE_DISABLE)
    @CheckPermission(url = ApiUrl.BANK_CARD_WITHDRAW_CONFIG_EDIT)
    @ApiOperation("启/禁")
    public RestResponse bankCardWithdrawConfigEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return bankCardWithdrawConfigService.enableDisableForAdmin(param);
    }
}
