package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalNavigatorService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalNavigatorAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 前台导航
 *
 * @author Barry
 * @date 2022-03-25
 */
@RestController
@Api(tags = "前台导航相关接口")
public class PortalNavigatorController extends BaseController {
    private PortalNavigatorService portalNavigatorService;

    @Autowired
    public void setPortalNavigatorService(PortalNavigatorService portalNavigatorService) {
        this.portalNavigatorService = portalNavigatorService;
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<PortalNavigatorAdminVO>> portalNavigatorPage(@RequestBody @Valid PortalNavigatorPageAdminParam param) {
        PageVO<PortalNavigatorAdminVO> pageVO = portalNavigatorService.pageVO(param, PortalNavigatorAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_NAVIGATOR_ADD)
    @ApiOperation("新增")
    public RestResponse portalNavigatorAdd(@RequestBody @Valid PortalNavigatorAddAdminParam param) {
        return portalNavigatorService.addForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_NAVIGATOR_EDIT)
    @ApiOperation("编辑")
    public RestResponse portalNavigatorEdit(@RequestBody @Valid PortalNavigatorEditAdminParam param) {
        return portalNavigatorService.editForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.PORTAL_NAVIGATOR_EDIT)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_NAVIGATOR_ENABLE_DISABLE)
    @ApiOperation("启/禁")
    public RestResponse portalNavigatorEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return portalNavigatorService.enableDisableForAdmin(param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_NAVIGATOR_DELETE)
    @ApiOperation("删除")
    public RestResponse portalNavigatorDelete(@RequestBody @Valid IdParam param) {
        return portalNavigatorService.deleteForAdmin(param);
    }
}
