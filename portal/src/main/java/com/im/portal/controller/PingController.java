package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.I18nLanguageCache;
import com.im.common.entity.I18nLanguage;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.I18nLanguageCommonVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 心跳服务接口
 */
@RestController
@Api(tags = "心跳服务接口")
@ApiSupport(order = 20)
public class PingController extends BaseController {

    @RequestMapping(value = ApiUrl.PING, method = RequestMethod.POST)
    @ApiOperation("心跳接口-POST-无需登录")
    @ApiOperationSupport(order = 1)
    public RestResponse post() {
        return ok();
    }

    @RequestMapping(value = ApiUrl.PING, method = RequestMethod.GET)
    @ApiOperation("心跳接口-GET-无需登录")
    @ApiOperationSupport(order = 2)
    public RestResponse get() {
        return ok();
    }
}
