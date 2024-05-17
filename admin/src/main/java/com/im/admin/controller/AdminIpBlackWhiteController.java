package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.AdminIpBlackWhiteAddAdminParam;
import com.im.common.param.AdminIpBlackWhiteEditAdminParam;
import com.im.common.param.AdminIpBlackWhitePageAdminParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminIpBlackWhiteService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminIpBlackWhiteAdminVO;
import com.im.common.vo.AdminSessionUser;
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
 * 后台IP黑白名单Controller
 *
 * @author Barry
 * @date 2021-06-24
 */
@RestController
@Api(tags = "后台黑白名单IP相关接口")
public class AdminIpBlackWhiteController extends BaseController {
    private AdminIpBlackWhiteService adminIpBlackWhiteService;

    @Autowired
    public void setAdminIpWhiteService(AdminIpBlackWhiteService adminIpBlackWhiteService) {
        this.adminIpBlackWhiteService = adminIpBlackWhiteService;
    }

    @RequestMapping(value = ApiUrl.ADMIN_IP_BLACK_WHITE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<AdminIpBlackWhiteAdminVO>> adminIpBlackWhitePage(@RequestBody @Valid AdminIpBlackWhitePageAdminParam param) {
        PageVO<AdminIpBlackWhiteAdminVO> pageVO = adminIpBlackWhiteService.pageVO(param, AdminIpBlackWhiteAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.ADMIN_IP_BLACK_WHITE_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_IP_BLACK_WHITE_ADD)
    @CheckPermission
    @ApiOperation("新增")
    public RestResponse adminIpBlackWhiteAdd(HttpServletRequest request, @RequestBody @Valid AdminIpBlackWhiteAddAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminIpBlackWhiteService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_IP_BLACK_WHITE_EDIT, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_IP_BLACK_WHITE_EDIT)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse adminIpBlackWhiteEdit(HttpServletRequest request, @RequestBody @Valid AdminIpBlackWhiteEditAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminIpBlackWhiteService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_IP_BLACK_WHITE_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.ADMIN_IP_BLACK_WHITE_DELETE)
    @CheckPermission
    @ApiOperation("删除")
    public RestResponse adminIpBlackWhiteDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return adminIpBlackWhiteService.deleteForAdmin(sessionUser, param);
    }
}
