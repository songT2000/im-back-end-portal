package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.ApiRechargeConfig;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.ApiRechargeConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.ApiRechargeConfigAdminSimpleVO;
import com.im.common.vo.ApiRechargeConfigAdminVO;
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
 * 第三方充值配置
 *
 * @author max.stark
 */
@RestController
@Api(tags = "三方充值配置相关接口")
public class ApiRechargeConfigController extends BaseController {
    private ApiRechargeConfigService apiRechargeConfigService;

    @Autowired
    public void setApiRechargeConfigService(ApiRechargeConfigService apiRechargeConfigService) {
        this.apiRechargeConfigService = apiRechargeConfigService;
    }

    @RequestMapping(value = ApiUrl.API_RECHARGE_CONFIG_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("简单列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<ApiRechargeConfigAdminSimpleVO>> apiRechargeConfigSimpleList() {
        List<ApiRechargeConfig> list = apiRechargeConfigService
                .lambdaQuery()
                .eq(ApiRechargeConfig::getDeleted, false)
                .orderByAsc(ApiRechargeConfig::getSort)
                .list();
        List<ApiRechargeConfigAdminSimpleVO> voList = CollectionUtil.toList(list, ApiRechargeConfigAdminSimpleVO::new);
        return ok(voList);
    }

    @RequestMapping(value = ApiUrl.API_RECHARGE_CONFIG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<ApiRechargeConfigAdminVO>> apiRechargeConfigPage(@RequestBody @Valid ApiRechargeConfigPageParam param) {
        PageVO<ApiRechargeConfigAdminVO> pageVO = apiRechargeConfigService.pageVO(param, e -> new ApiRechargeConfigAdminVO(e));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.API_RECHARGE_CONFIG_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_RECHARGE_CONFIG_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse apiRechargeConfigAdd(HttpServletRequest request, @RequestBody @Valid ApiRechargeConfigAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return apiRechargeConfigService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.API_RECHARGE_CONFIG_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_RECHARGE_CONFIG_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse apiRechargeConfigEdit(HttpServletRequest request, @RequestBody @Valid ApiRechargeConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return apiRechargeConfigService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.API_RECHARGE_CONFIG_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_RECHARGE_CONFIG_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse apiRechargeConfigDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return apiRechargeConfigService.deleteForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.API_RECHARGE_CONFIG_ENABLE_DISABLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_RECHARGE_CONFIG_ENABLE_DISABLE)
    @CheckPermission(url = ApiUrl.API_RECHARGE_CONFIG_EDIT)
    @ApiOperation("启/禁")
    public RestResponse apiRechargeConfigEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return apiRechargeConfigService.enableDisableForAdmin(param);
    }
}
