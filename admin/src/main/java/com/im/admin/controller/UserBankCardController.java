package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdParam;
import com.im.common.param.UserBankCardAddAdminParam;
import com.im.common.param.UserBankCardPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBankCardService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.UserBankCardAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户银行卡相关接口
 *
 * @author max.stark
 */
@RestController
@Api(tags = "用户银行卡相关接口")
public class UserBankCardController extends BaseController {
    private UserBankCardService userBankCardService;
    private BankCache bankCache;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setUserBankCardService(UserBankCardService userBankCardService) {
        this.userBankCardService = userBankCardService;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<UserBankCardAdminVO>> userBankCardPage(@RequestBody @Valid UserBankCardPageAdminParam param) {
        PageVO<UserBankCardAdminVO> pageVO = userBankCardService.pageVO(param, e -> new UserBankCardAdminVO(e, bankCache, portalUserCache));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse userBankCardAdd(@RequestBody @Valid UserBankCardAddAdminParam param) {
        return userBankCardService.addForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse userBankCardDelete(@RequestBody @Valid IdParam param) {
        return userBankCardService.deleteForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_ENABLE_DISABLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_ENABLE_DISABLE)
    @CheckPermission
    @ApiOperation("启/禁")
    public RestResponse userBankCardEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return userBankCardService.enableDisableForAdmin(param);
    }
}
