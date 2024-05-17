package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.AppVersion;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.AppVersionAddParam;
import com.im.common.param.AppVersionSearchParam;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.service.AppVersionService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AppVersionVO;
import com.im.common.vo.BankAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

/**
 * 应用版本管理Controller
 */
@RestController
@Api(tags = "应用版本管理相关接口")
public class AppVersionController extends BaseController {
    private AppVersionService appVersionService;

    @Autowired
    public void setAppVersionService(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @RequestMapping(value = ApiUrl.APP_VERSION_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("应用版本列表")
    public RestResponse<List<AppVersion>> list(@RequestBody AppVersionSearchParam param) {
        List<AppVersion> list = appVersionService.lambdaQuery().eq(param.getAppType()!=null,AppVersion::getAppType,param.getAppType()).list();
        list = CollectionUtil.sort(list, Comparator.comparingInt(AppVersion::getVersionCode).reversed());
        List<AppVersionVO> appVersionVOS = CollectionUtil.toList(list, AppVersionVO::new);
        return ok(appVersionVOS);
    }

    @RequestMapping(value = ApiUrl.APP_VERSION_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type=UserOperationLogTypeEnum.APP_VERSION_DELETE)
    @ApiOperation("删除应用版本")
    public RestResponse delete(@RequestBody @Valid IdParam param) {
        return appVersionService.delete(param.getId());
    }

    @RequestMapping(value = ApiUrl.APP_VERSION_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.APP_VERSION_ADD)
    @ApiOperation("新增应用版本")
    public RestResponse add(@RequestBody @Valid AppVersionAddParam param) {
        return appVersionService.add(param);
    }
}
