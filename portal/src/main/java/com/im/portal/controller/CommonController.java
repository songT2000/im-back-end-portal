package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.response.RestResponse;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用接口
 *
 * @author Barry
 * @date 2021-02-01
 */
@RestController
@Api(tags = "通用接口")
@ApiSupport(order = 2)
public class CommonController extends BaseController {
    private SysConfigCache sysConfigCache;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @RequestMapping(value = ApiUrl.SPEED_TEST, method = RequestMethod.POST)
    @ApiOperation(value = "测速", notes = "<span style='color: red;font-family:bold;'>返回字段中success===true表示服务器运行正常，其它情况都表示服务器不可用</span>")
    @ApiOperationSupport(order = 1)
    public RestResponse speedTest() {
        return ok();
    }
}
