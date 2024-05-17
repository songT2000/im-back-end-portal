package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.PortalIpBlackWhiteAddAdminParam;
import com.im.common.param.PortalIpBlackWhiteEditAdminParam;
import com.im.common.param.PortalIpBlackWhitePageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalIpBlackWhiteService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.PortalIpBlackWhiteAdminVO;
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
 * 用户IP黑白名单Controller
 *
 * @author Max
 * @date 2021/02/21
 */
@RestController
@Api(tags = "前台用户IP黑白名单相关接口")
public class PortalIpBlackWhiteController extends BaseController {
    private PortalIpBlackWhiteService portalIpBlackWhiteService;

    @Autowired
    public void setPortalIpBlackWhiteService(PortalIpBlackWhiteService portalIpBlackWhiteService) {
        this.portalIpBlackWhiteService = portalIpBlackWhiteService;
    }

    @RequestMapping(value = ApiUrl.PORTAL_IP_BLACK_WHITE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<PortalIpBlackWhiteAdminVO>> portalIpBlackWhitePage(@RequestBody @Valid PortalIpBlackWhitePageAdminParam param) {
        PageVO<PortalIpBlackWhiteAdminVO> pageVO = portalIpBlackWhiteService.pageVO(param, PortalIpBlackWhiteAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.PORTAL_IP_BLACK_WHITE_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_IP_BLACK_WHITE_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse portalIpBlackWhiteAdd(HttpServletRequest request, @RequestBody @Valid PortalIpBlackWhiteAddAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalIpBlackWhiteService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_IP_BLACK_WHITE_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_IP_BLACK_WHITE_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse portalIpBlackWhiteEdit(HttpServletRequest request, @RequestBody @Valid PortalIpBlackWhiteEditAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalIpBlackWhiteService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_IP_BLACK_WHITE_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_IP_BLACK_WHITE_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse portalIpBlackWhiteDelete(@RequestBody @Valid IdParam param) {
        return portalIpBlackWhiteService.deleteForAdmin(param);
    }
}
