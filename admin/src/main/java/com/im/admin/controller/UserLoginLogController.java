package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.param.UserLoginLogPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserLoginLogService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.UserLoginLogAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 后台登录日志Controller
 *
 * @author Barry
 * @date 2019-11-08
 */
@RestController
@Api(tags = "登录日志相关接口")
public class UserLoginLogController extends BaseController {
    private UserLoginLogService userLoginLogService;

    @Autowired
    public void setUserLoginLogService(UserLoginLogService userLoginLogService) {
        this.userLoginLogService = userLoginLogService;
    }

    @RequestMapping(value = ApiUrl.ADMIN_LOGIN_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("后台登录日志分页")
    public RestResponse<PageVO<UserLoginLogAdminVO>> adminLoginLogPage(@RequestBody @Valid UserLoginLogPageAdminParam param) {
        param.setPortalType(PortalTypeEnum.ADMIN);
        PageVO<UserLoginLogAdminVO> pageVO = userLoginLogService.pageVO(param, UserLoginLogAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.PORTAL_LOGIN_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("前台登录日志分页")
    public RestResponse<PageVO<UserLoginLogAdminVO>> portalLoginLogPage(@RequestBody @Valid UserLoginLogPageAdminParam param) {
        param.setPortalType(PortalTypeEnum.PORTAL);
        PageVO<UserLoginLogAdminVO> pageVO = userLoginLogService.pageVO(param, UserLoginLogAdminVO::new);
        return ok(pageVO);
    }
}
