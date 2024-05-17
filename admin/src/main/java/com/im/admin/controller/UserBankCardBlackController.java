package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.AdminUserCache;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.UserBankCardBlackAddAdminParam;
import com.im.common.param.UserBankCardBlackEditAdminParam;
import com.im.common.param.UserBankCardBlackPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBankCardBlackService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.UserBankCardBlackAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户卡黑名单Controller
 *
 * @author Barry
 * @date 2019/10/12
 */
@RestController
@Api(tags = "用户卡黑名单相关接口")
public class UserBankCardBlackController extends BaseController {
    private UserBankCardBlackService userBankCardBlackService;
    private AdminUserCache adminUserCache;

    @Autowired
    public void setUserBankCardBlackService(UserBankCardBlackService userBankCardBlackService) {
        this.userBankCardBlackService = userBankCardBlackService;
    }

    @Autowired
    public void setAdminUserCache(AdminUserCache adminUserCache) {
        this.adminUserCache = adminUserCache;
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_BLACK_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<UserBankCardBlackAdminVO>> userBankCardBlackPage(@RequestBody @Valid UserBankCardBlackPageAdminParam param) {
        PageVO<UserBankCardBlackAdminVO> pageVO = userBankCardBlackService.pageVO(param, card -> new UserBankCardBlackAdminVO(card, adminUserCache));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_BLACK_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_BLACK_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse userBankCardBlackAdd(HttpServletRequest request, @RequestBody @Valid UserBankCardBlackAddAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return userBankCardBlackService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_BLACK_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_BLACK_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse userBankCardBlackEdit(HttpServletRequest request, @RequestBody @Valid UserBankCardBlackEditAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return userBankCardBlackService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_BLACK_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_BLACK_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse userBankCardBlackDelete(@RequestBody @Valid IdParam param) {
        return userBankCardBlackService.deleteForAdmin(param);
    }
}
