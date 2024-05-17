package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.IdParam;
import com.im.common.param.TimUserOnlinePageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimUserDeviceStateService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimUserOnlineVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * IM在线用户
 */
@RestController
@Api(tags = "IM在线用户相关接口")
public class TimUserStateController extends BaseController {

    private TimUserDeviceStateService timUserDeviceStateService;

    @Autowired
    public void setTimUserDeviceStateService(TimUserDeviceStateService timUserDeviceStateService) {
        this.timUserDeviceStateService = timUserDeviceStateService;
    }

    @RequestMapping(value = ApiUrl.TIM_USER_ONLINE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("在线用户分页")
    public RestResponse<PageVO<TimUserOnlineVO>> page(@RequestBody @Valid TimUserOnlinePageParam param) {
        PageVO<TimUserOnlineVO> pageVO = timUserDeviceStateService.pageVO(param, TimUserOnlineVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_USER_KICK_OUT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_USER_KICK_OUT)
    @ApiOperation("踢下线")
    public RestResponse kickOut(@RequestBody @Valid IdParam param) {
        return timUserDeviceStateService.kickOut(param.getId());
    }
}
