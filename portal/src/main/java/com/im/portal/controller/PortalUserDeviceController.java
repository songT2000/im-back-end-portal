package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.entity.enums.DeviceTypeEnum;
import com.im.common.param.PortalUserDeviceInfoParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserDeviceInfoService;
import com.im.common.vo.PortalSessionUser;
import com.im.portal.controller.url.ApiUrl;
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
 * 设备接口
 */
@RestController
@Api(tags = "设备接口")
@ApiSupport(order = 20)
public class PortalUserDeviceController extends BaseController {

    private PortalUserDeviceInfoService portalUserDeviceInfoService;

    @Autowired
    public void setPortalUserDeviceInfoService(PortalUserDeviceInfoService portalUserDeviceInfoService) {
        this.portalUserDeviceInfoService = portalUserDeviceInfoService;
    }

    @RequestMapping(value = ApiUrl.DEVICE_ANDROID, method = RequestMethod.POST)
    @ApiOperation("android设备信息接口")
    @ApiOperationSupport(order = 1)
    public RestResponse androidDeviceInfo(@RequestBody @Valid PortalUserDeviceInfoParam param, HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return portalUserDeviceInfoService.bind(sessionUser.getId(), DeviceTypeEnum.ANDROID, param);
    }

    @RequestMapping(value = ApiUrl.DEVICE_IOS, method = RequestMethod.POST)
    @ApiOperation("iOS设备信息接口")
    @ApiOperationSupport(order = 2)
    public RestResponse iOSDeviceInfo(@RequestBody @Valid PortalUserDeviceInfoParam param, HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return portalUserDeviceInfoService.bind(sessionUser.getId(), DeviceTypeEnum.IOS, param);
    }
}
