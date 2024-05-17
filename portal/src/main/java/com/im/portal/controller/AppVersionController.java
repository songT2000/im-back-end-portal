package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.common.cache.impl.AppVersionCache;
import com.im.common.entity.AppVersion;
import com.im.common.param.AppVersionLatestPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.service.AppVersionService;
import com.im.common.vo.AppVersionVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "app版本管理接口")
public class AppVersionController extends BaseController{

    private AppVersionService appVersionService;

    @Autowired
    public void setAppVersionService(AppVersionService appVersionService) {
        this.appVersionService = appVersionService;
    }

    @RequestMapping(value = ApiUrl.APP_VERSION_LATEST, method = RequestMethod.POST)
    @ApiOperation("获取应用最新版本")
    @ApiOperationSupport(order = 1)
    public RestResponse<AppVersionVO> queryLatest(@RequestBody @Valid AppVersionLatestPortalParam param){
        return appVersionService.queryLatest(param.getAppType());
    }

    @RequestMapping(value = ApiUrl.APP_VERSION_LATEST2, method = RequestMethod.POST)
    @ApiOperation("获取应用最新版本，无需token")
    @ApiOperationSupport(order = 1)
    public RestResponse<AppVersionVO> queryLatest2(@RequestBody @Valid AppVersionLatestPortalParam param){
        return appVersionService.queryLatest(param.getAppType());
    }
}
