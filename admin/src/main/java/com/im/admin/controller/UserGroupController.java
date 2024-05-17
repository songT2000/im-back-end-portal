package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.ApiRechargeConfigCache;
import com.im.common.cache.impl.BankCardRechargeConfigCache;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.UserGroupApiRechargeConfigService;
import com.im.common.service.UserGroupBankCardRechargeConfigService;
import com.im.common.service.UserGroupService;
import com.im.common.service.UserGroupUserService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户组Controller
 *
 * @author Barry
 * @date 2021-04-12
 */
@RestController
@Api(tags = "用户组相关接口")
public class UserGroupController extends BaseController {
    private UserGroupService userGroupService;
    private UserGroupUserService userGroupUserService;
    private UserGroupBankCardRechargeConfigService userGroupBankCardRechargeConfigService;
    private UserGroupApiRechargeConfigService userGroupApiRechargeConfigService;
    private BankCardRechargeConfigCache bankCardRechargeConfigCache;
    private ApiRechargeConfigCache apiRechargeConfigCache;

    @Autowired
    public void setUserGroupService(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @Autowired
    public void setUserGroupUserService(UserGroupUserService userGroupUserService) {
        this.userGroupUserService = userGroupUserService;
    }

    @Autowired
    public void setUserGroupBankCardRechargeConfigService(UserGroupBankCardRechargeConfigService userGroupBankCardRechargeConfigService) {
        this.userGroupBankCardRechargeConfigService = userGroupBankCardRechargeConfigService;
    }

    @Autowired
    public void setUserGroupApiRechargeConfigService(UserGroupApiRechargeConfigService userGroupApiRechargeConfigService) {
        this.userGroupApiRechargeConfigService = userGroupApiRechargeConfigService;
    }

    @Autowired
    public void setBankCardRechargeConfigCache(BankCardRechargeConfigCache bankCardRechargeConfigCache) {
        this.bankCardRechargeConfigCache = bankCardRechargeConfigCache;
    }

    @Autowired
    public void setApiRechargeConfigCache(ApiRechargeConfigCache apiRechargeConfigCache) {
        this.apiRechargeConfigCache = apiRechargeConfigCache;
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("简单列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<UserGroupSimpleAdminVO>> userGroupSimpleList() {
        List<UserGroupSimpleAdminVO> list = userGroupService.listVO(UserGroupSimpleAdminVO::new);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    @ApiOperationSupport(order = 2)
    public RestResponse<PageVO<UserGroupAdminVO>> userGroupPage(@RequestBody @Valid UserGroupPageAdminParam param) {
        PageVO<UserGroupAdminVO> pageVO = userGroupService.pageVOForAdmin(param);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_GROUP_ADD)
    @ApiOperation("新增")
    @ApiOperationSupport(order = 3)
    public RestResponse userGroupAdd(@RequestBody @Valid UserGroupAddAdminParam param) {
        return userGroupService.addForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_GROUP_EDIT)
    @ApiOperation("编辑")
    @ApiOperationSupport(order = 4)
    public RestResponse userGroupEdit(@RequestBody @Valid UserGroupEditAdminParam param) {
        return userGroupService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_USER_LIST, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.USER_GROUP_PAGE)
    @ApiOperation("用户组用户列表")
    @ApiOperationSupport(order = 5)
    public RestResponse<List<UserGroupUserAdminVO>> userGroupUserList(@RequestBody @Valid UserGroupUserListAdminParam param) {
        List<UserGroupUserAdminVO> list = userGroupUserService.listVO(param.toQueryWrapper(), UserGroupUserAdminVO::new);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_EDIT_USER, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.USER_GROUP_EDIT)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_GROUP_EDIT_USER)
    @ApiOperation("编辑用户")
    @ApiOperationSupport(order = 6)
    public RestResponse userGroupEditUser(@RequestBody @Valid UserGroupEditUserAdminParam param) {
        return userGroupUserService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_BANK_CARD_RECHARGE_CONFIG_LIST, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.USER_GROUP_PAGE)
    @ApiOperation("银行卡充值配置列表")
    @ApiOperationSupport(order = 7)
    public RestResponse<List<UserGroupBankCardRechargeConfigAdminVO>> userGroupBankCardRechargeConfigList(
            @RequestBody @Valid UserGroupBankCardRechargeConfigListAdminParam param) {
        List<UserGroupBankCardRechargeConfigAdminVO> list = userGroupBankCardRechargeConfigService
                .listVO(param.toQueryWrapper(), e -> new UserGroupBankCardRechargeConfigAdminVO(e, bankCardRechargeConfigCache));
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_EDIT_BANK_CARD_RECHARGE_CONFIG, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.USER_GROUP_EDIT)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_GROUP_EDIT_BANK_CARD_RECHARGE_CONFIG)
    @ApiOperation("编辑银行卡充值配置")
    @ApiOperationSupport(order = 8)
    public RestResponse userGroupEditBankCardRechargeConfig(@RequestBody @Valid UserGroupEditRelationIdAdminParam param) {
        return userGroupBankCardRechargeConfigService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_API_RECHARGE_CONFIG_LIST, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.USER_GROUP_PAGE)
    @ApiOperation("三方充值配置列表")
    @ApiOperationSupport(order = 9)
    public RestResponse<List<UserGroupApiRechargeConfigAdminVO>> userGroupApiRechargeConfigList(
            @RequestBody @Valid UserGroupApiRechargeConfigListAdminParam param) {
        List<UserGroupApiRechargeConfigAdminVO> list = userGroupApiRechargeConfigService
                .listVO(param.toQueryWrapper(), e -> new UserGroupApiRechargeConfigAdminVO(e, apiRechargeConfigCache));
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_EDIT_API_RECHARGE_CONFIG, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.USER_GROUP_EDIT)
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_GROUP_EDIT_API_RECHARGE_CONFIG)
    @ApiOperation("编辑三方充值配置")
    @ApiOperationSupport(order = 10)
    public RestResponse userGroupEditApiRechargeConfig(@RequestBody @Valid UserGroupEditRelationIdAdminParam param) {
        return userGroupApiRechargeConfigService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.USER_GROUP_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_GROUP_DELETE)
    @ApiOperation("删除")
    @ApiOperationSupport(order = 11)
    public RestResponse userGroupDelete(@RequestBody @Valid IdParam param) {
        return userGroupService.deleteForAdmin(param);
    }
}
