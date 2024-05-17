package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.param.UserOperationLogPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserOperationLogService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.UserOperationLogAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 登录日志Controller
 *
 * @author Barry
 * @date 2019-11-08
 */
@RestController
@Api(tags = "操作日志相关接口")
public class UserOperationLogController extends BaseController {
    private UserOperationLogService userOperationLogService;

    @Autowired
    public void setUserActionLogService(UserOperationLogService userOperationLogService) {
        this.userOperationLogService = userOperationLogService;
    }

    @RequestMapping(value = ApiUrl.ADMIN_OPERATION_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("后台操作日志分页")
    public RestResponse<PageVO<UserOperationLogAdminVO>> adminOperationLogPage(@RequestBody @Valid UserOperationLogPageAdminParam param) {
        param.setPortalType(PortalTypeEnum.ADMIN);
        PageVO<UserOperationLogAdminVO> pageVO = userOperationLogService.pageVO(param, UserOperationLogAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.PORTAL_OPERATION_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("前台操作日志分页")
    public RestResponse<PageVO<UserOperationLogAdminVO>> portalOperationLogPage(@RequestBody @Valid UserOperationLogPageAdminParam param) {
        param.setPortalType(PortalTypeEnum.PORTAL);
        PageVO<UserOperationLogAdminVO> pageVO = userOperationLogService.pageVO(param, UserOperationLogAdminVO::new);
        return ok(pageVO);
    }
}
