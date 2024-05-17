package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.AppAutoReplyConfigAddParam;
import com.im.common.param.AppAutoReplyConfigPageParam;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.service.AppAutoReplyConfigService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AppAutoReplyConfigVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 自动回复配置接口
 */
@RestController
@Api(tags = "自动回复配置接口")
public class AppAutoReplyConfigController extends BaseController {

    private AppAutoReplyConfigService appAutoReplyConfigService;

    @Autowired
    public void setAppAutoReplyConfigService(AppAutoReplyConfigService appAutoReplyConfigService) {
        this.appAutoReplyConfigService = appAutoReplyConfigService;
    }

    @RequestMapping(value = ApiUrl.APP_AUTO_REPLY_CONFIG_PAGE, method = RequestMethod.POST)
    @ApiOperation("自动回复配置分页")
    @CheckPermission
    public RestResponse<PageVO<AppAutoReplyConfigVo>> page(AppAutoReplyConfigPageParam param) {
        PageVO<AppAutoReplyConfigVo> vo = appAutoReplyConfigService.pageVO(param, AppAutoReplyConfigVo::new);
        return RestResponse.ok(vo);
    }

    @RequestMapping(value = ApiUrl.APP_AUTO_REPLY_CONFIG_ADD, method = RequestMethod.POST)
    @ApiOperation("新增自动回复配置")
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.APP_AUTO_REPLY_CONFIG_ADD)
    public RestResponse add(@RequestBody @Valid AppAutoReplyConfigAddParam param) {

        return appAutoReplyConfigService.add(param);
    }

    @RequestMapping(value = ApiUrl.APP_AUTO_REPLY_CONFIG_DELETE, method = RequestMethod.POST)
    @ApiOperation("删除自动回复配置")
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.APP_AUTO_REPLY_CONFIG_DELETE)
    public RestResponse delete(@RequestBody @Valid IdParam param) {
        return appAutoReplyConfigService.delete(param);
    }
}
