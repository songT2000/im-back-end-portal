package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdEnableDisableGoogleParam;
import com.im.common.param.IdGoogleParam;
import com.im.common.param.SysBackupConfigEditParam;
import com.im.common.param.SysBackupConfigPageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SysBackupConfigService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.SysBackupConfigAdminVO;
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
 * 数据备份配置 Controller
 *
 * @author Barry
 * @date 2020-01-04
 */
@RestController
@Api(tags = "数据备份相关接口")
public class SysBackupConfigController extends BaseController {
    private SysBackupConfigService sysBackupConfigService;

    @Autowired
    public void setSysBackupConfigService(SysBackupConfigService sysBackupConfigService) {
        this.sysBackupConfigService = sysBackupConfigService;
    }

    @RequestMapping(value = ApiUrl.SYS_BACKUP_CONFIG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    @ApiOperationSupport(order = 1)
    public RestResponse<PageVO<SysBackupConfigAdminVO>> sysBackupConfigPage(@RequestBody @Valid SysBackupConfigPageParam param) {
        PageVO<SysBackupConfigAdminVO> page = sysBackupConfigService.pageVO(param, null, SysBackupConfigAdminVO::new);
        return RestResponse.ok(page);
    }

    @RequestMapping(value = ApiUrl.SYS_BACKUP_CONFIG_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_BACKUP_CONFIG_EDIT)
    @ApiOperation("编辑")
    @ApiOperationSupport(order = 2)
    public RestResponse sysBackupConfigEdit(HttpServletRequest request, @RequestBody @Valid SysBackupConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysBackupConfigService.editForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.SYS_BACKUP_CONFIG_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_BACKUP_CONFIG_ENABLE_DISABLE)
    @ApiOperation("启用/禁用")
    @ApiOperationSupport(order = 3)
    public RestResponse sysBackupConfigEnableDisable(HttpServletRequest request, @RequestBody @Valid IdEnableDisableGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysBackupConfigService.enableDisableForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.SYS_BACKUP_CONFIG_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_BACKUP_CONFIG_DELETE)
    @ApiOperation("删除")
    @ApiOperationSupport(order = 4)
    public RestResponse sysBackupConfigDelete(HttpServletRequest request, @RequestBody @Valid IdGoogleParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysBackupConfigService.deleteForAdmin(sessionUser, param);
    }
}

