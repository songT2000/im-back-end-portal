package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdGoogleParam;
import com.im.common.param.SysNoticeAddParam;
import com.im.common.param.SysNoticeEditParam;
import com.im.common.param.SysNoticePageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SysNoticeService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.SysNoticeAdminVO;
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
 * 前台用户controller
 *
 * @author max.stark
 */
@RestController
@Api(tags = "系统公告相关接口")
public class SysNoticeController extends BaseController {
    private SysNoticeService sysNoticeService;
    private I18nLanguageCache i18nLanguageCache;

    @Autowired
    public void setSysNoticeService(SysNoticeService sysNoticeService) {
        this.sysNoticeService = sysNoticeService;
    }

    @Autowired
    public void setI18nLanguageCache(I18nLanguageCache i18nLanguageCache) {
        this.i18nLanguageCache = i18nLanguageCache;
    }

    @RequestMapping(value = ApiUrl.SYS_NOTICE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    @ApiOperationSupport(order = 1)
    public RestResponse<PageVO<SysNoticeAdminVO>> sysNoticePage(@RequestBody @Valid SysNoticePageParam param) {
        PageVO<SysNoticeAdminVO> page = sysNoticeService.pageVO(param, e -> new SysNoticeAdminVO(e, i18nLanguageCache));
        return ok(page);
    }

    @RequestMapping(value = ApiUrl.SYS_NOTICE_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_NOTICE_ADD)
    @ApiOperation("新增")
    @ApiOperationSupport(order = 2)
    public RestResponse sysNoticeAdd(HttpServletRequest request, @RequestBody @Valid SysNoticeAddParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysNoticeService.addForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.SYS_NOTICE_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_NOTICE_EDIT)
    @ApiOperation("编辑")
    @ApiOperationSupport(order = 3)
    public RestResponse sysNoticeEdit(HttpServletRequest request, @RequestBody @Valid SysNoticeEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysNoticeService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.SYS_NOTICE_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_NOTICE_DELETE)
    @ApiOperation("删除")
    @ApiOperationSupport(order = 4)
    public RestResponse sysNoticeDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysNoticeService.deleteForAdmin(sessionUser, param);
    }
}
