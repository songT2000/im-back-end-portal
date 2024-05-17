package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.PortalAreaBlackWhiteAddAdminParam;
import com.im.common.param.PortalAreaBlackWhiteEditAdminParam;
import com.im.common.param.PortalAreaBlackWhitePageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalAreaBlackWhiteService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.PortalAreaBlackWhiteAdminVO;
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
 * 用户区域黑名单Controller
 *
 * @author Barry
 * @date 2021-02-27
 */
@RestController
@Api(tags = "前台用户区域黑白名单相关接口")
public class PortalAreaBlackWhiteController extends BaseController {
    private PortalAreaBlackWhiteService portalAreaBlackWhiteService;

    @Autowired
    public void setPortalAreaBlackWhiteService(PortalAreaBlackWhiteService portalAreaBlackWhiteService) {
        this.portalAreaBlackWhiteService = portalAreaBlackWhiteService;
    }

    @RequestMapping(value = ApiUrl.PORTAL_AREA_BLACK_WHITE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<PortalAreaBlackWhiteAdminVO>> portalAreaBlackWhitePage(@RequestBody @Valid PortalAreaBlackWhitePageAdminParam param) {
        PageVO<PortalAreaBlackWhiteAdminVO> pageVO = portalAreaBlackWhiteService.pageVO(param, PortalAreaBlackWhiteAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.PORTAL_AREA_BLACK_WHITE_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_AREA_BLACK_WHITE_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse portalAreaBlackWhiteAdd(HttpServletRequest request, @RequestBody @Valid PortalAreaBlackWhiteAddAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalAreaBlackWhiteService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_AREA_BLACK_WHITE_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_AREA_BLACK_WHITE_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse portalAreaBlackWhiteEdit(HttpServletRequest request, @RequestBody @Valid PortalAreaBlackWhiteEditAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return portalAreaBlackWhiteService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PORTAL_AREA_BLACK_WHITE_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.PORTAL_AREA_BLACK_WHITE_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse portalAreaBlackWhiteDelete(@RequestBody @Valid IdParam param) {
        return portalAreaBlackWhiteService.deleteForAdmin(param);
    }
}
