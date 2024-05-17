package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.vo.*;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.SysConfigEditParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SysConfigService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.EnumUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.SysConfigListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统配置Controller
 *
 * @author Barry
 * @date 2019/11/28
 */
@RestController
@Api(tags = "系统配置相关接口")
public class SysConfigController extends BaseController {
    private SysConfigCache sysConfigCache;
    private SysConfigService sysConfigService;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setSysConfigService(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @RequestMapping(value = ApiUrl.SYS_CONFIG_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("获取系统配置")
    public RestResponse<List<SysConfigVO>> sysConfigSimpleList() {
        List<SysConfigVO> list = new ArrayList<>();

        // 全局配置
        {
            GlobalConfigCommonVO globalConfig = new GlobalConfigCommonVO(sysConfigCache.getGlobalConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.GLOBAL, globalConfig));
        }

        // 后台配置
        {
            AdminConfigAdminVO adminConfig = new AdminConfigAdminVO(sysConfigCache.getAdminConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.ADMIN, adminConfig));
        }

        // 提现配置
        {
            WithdrawConfigAdminVO withdrawConfig = new WithdrawConfigAdminVO(sysConfigCache.getWithdrawConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.WITHDRAW, withdrawConfig));
        }

        // 注册配置
        {
            RegisterConfigAdminVO registerConfig = new RegisterConfigAdminVO(sysConfigCache.getRegisterConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.REGISTER, registerConfig));
        }

        return ok(list);
    }

    @RequestMapping(value = ApiUrl.SYS_CONFIG_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("系统配置列表-普通")
    public RestResponse<List<SysConfigListVO>> sysConfigList() {
        List<SysConfig> sysConfigs = sysConfigService.listEditable(false);
        return ok(CollectionUtil.toList(sysConfigs, SysConfigListVO::new));
    }

    @RequestMapping(value = ApiUrl.SYS_CONFIG_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CONFIG_EDIT)
    @ApiOperation("编辑系统配置-普通")
    public RestResponse edit(HttpServletRequest request, @RequestBody @Valid SysConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysConfigService.edit(sessionUser, param, false);
    }

    @RequestMapping(value = ApiUrl.SYS_CONFIG_ADVANCE_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("系统配置列表-高级")
    public RestResponse<List<SysConfigListVO>> sysConfigAdvanceList() {
        List<SysConfig> sysConfigs = sysConfigService.listEditable(true);
        return ok(CollectionUtil.toList(sysConfigs, SysConfigListVO::new));
    }

    @RequestMapping(value = ApiUrl.SYS_CONFIG_ADVANCE_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.SYS_CONFIG_ADVANCE_EDIT)
    @ApiOperation("编辑系统配置-高级")
    public RestResponse editAdvance(HttpServletRequest request, @RequestBody @Valid SysConfigEditParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return sysConfigService.edit(sessionUser, param, true);
    }

    @RequestMapping(value = ApiUrl.SYS_DICT_LIST, method = RequestMethod.POST)
    @ApiOperation("获取数据字典")
    public RestResponse<Map<String, Map<String, String>>> sysDictList() {
        return ok(EnumUtil.getCurrentLanguageEnumMap());
    }
}
