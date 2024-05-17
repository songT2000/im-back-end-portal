package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.ApiWithdrawConfig;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.ApiWithdrawConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.ApiWithdrawConfigAdminSimpleVO;
import com.im.common.vo.ApiWithdrawConfigAdminVO;
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
 * API代付配置Controller
 *
 * @author Barry
 * @date 2021-09-07
 */
@RestController
@Api(tags = "API代付配置相关接口")
public class ApiWithdrawConfigController extends BaseController {
    private ApiWithdrawConfigService apiWithdrawConfigService;

    @Autowired
    public void setApiWithdrawConfigService(ApiWithdrawConfigService apiWithdrawConfigService) {
        this.apiWithdrawConfigService = apiWithdrawConfigService;
    }

    @RequestMapping(value = ApiUrl.API_WITHDRAW_CONFIG_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("简单列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<ApiWithdrawConfigAdminSimpleVO>> apiWithdrawConfigSimpleList() {
        List<ApiWithdrawConfig> list = apiWithdrawConfigService
                .lambdaQuery()
                .eq(ApiWithdrawConfig::getDeleted, false)
                .orderByAsc(ApiWithdrawConfig::getSort)
                .list();
        List<ApiWithdrawConfigAdminSimpleVO> voList = CollectionUtil.toList(list, ApiWithdrawConfigAdminSimpleVO::new);
        return ok(voList);
    }

    @RequestMapping(value = ApiUrl.API_WITHDRAW_CONFIG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<ApiWithdrawConfigAdminVO>> apiWithdrawConfigPage(@RequestBody @Valid ApiWithdrawConfigPageParam param) {
        PageVO<ApiWithdrawConfigAdminVO> pageVO = apiWithdrawConfigService.pageVO(param, e -> new ApiWithdrawConfigAdminVO(e));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.API_WITHDRAW_CONFIG_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_WITHDRAW_CONFIG_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse apiWithdrawConfigAdd(HttpServletRequest request, @RequestBody @Valid ApiWithdrawConfigAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return apiWithdrawConfigService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.API_WITHDRAW_CONFIG_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_WITHDRAW_CONFIG_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse apiWithdrawConfigEdit(HttpServletRequest request, @RequestBody @Valid ApiWithdrawConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return apiWithdrawConfigService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.API_WITHDRAW_CONFIG_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_WITHDRAW_CONFIG_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse apiWithdrawConfigDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return apiWithdrawConfigService.deleteForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.API_WITHDRAW_CONFIG_ENABLE_DISABLE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.API_WITHDRAW_CONFIG_ENABLE_DISABLE)
    @CheckPermission(url = ApiUrl.API_WITHDRAW_CONFIG_EDIT)
    @ApiOperation("启/禁")
    public RestResponse apiWithdrawConfigEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return apiWithdrawConfigService.enableDisableForAdmin(param);
    }
}
