package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.PortalUserBankCardAddParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBankCardService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserBankCardPortalVO;
import com.im.portal.controller.url.ApiUrl;
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
 * 用户银行卡Controller
 *
 * @author Max
 * @date 2021-02-25
 */
@RestController
@Api(tags = "用户银行卡相关接口")
@ApiSupport(order = 11)
public class UserBankCardController extends BaseController {
    private UserBankCardService userBankCardService;

    @Autowired
    public void setUserBankCardService(UserBankCardService userBankCardService) {
        this.userBankCardService = userBankCardService;
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_LIST, method = RequestMethod.POST)
    @ApiOperation("列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<UserBankCardPortalVO>> userBankCardList(HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        List<UserBankCardPortalVO> list = userBankCardService.listVOForPortal(sessionUser);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_ADD, method = RequestMethod.POST)
    @ApiOperation("新增")
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_ADD)
    @ApiOperationSupport(order = 2)
    public RestResponse userBankCardAdd(HttpServletRequest request, @RequestBody @Valid PortalUserBankCardAddParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userBankCardService.addForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.USER_BANK_CARD_DELETE, method = RequestMethod.POST)
    @ApiOperation("删除")
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BANK_CARD_DELETE)
    @ApiOperationSupport(order = 3)
    public RestResponse userBankCardDelete(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return userBankCardService.deleteForPortal(sessionUser, param);
    }
}
