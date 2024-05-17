package com.im.admin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.param.PortalUserDeviceInfoPageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserDeviceInfoService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalUserDeviceInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = ApiUrl.DEVICE_INFO_PAGE, method = RequestMethod.POST)
    @ApiOperation("分页")
    @ApiOperationSupport(order = 1)
    public RestResponse page(@RequestBody @Valid PortalUserDeviceInfoPageParam param) {
        PageVO<PortalUserDeviceInfoVO> vo = portalUserDeviceInfoService.pageVO(param, PortalUserDeviceInfoVO::new);
        return RestResponse.ok(vo);
    }

}
