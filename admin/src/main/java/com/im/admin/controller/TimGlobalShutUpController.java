package com.im.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimGlobalShutUp;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.TimBlacklistService;
import com.im.common.service.TimGlobalShutUpService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimBlacklistVO;
import com.im.common.vo.TimGlobalShutUpVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户全局禁言相关接口")
public class TimGlobalShutUpController extends BaseController{

    private TimGlobalShutUpService timGlobalShutUpService;

    @Autowired
    public void setTimGlobalShutUpService(TimGlobalShutUpService timGlobalShutUpService) {
        this.timGlobalShutUpService = timGlobalShutUpService;
    }

    @RequestMapping(value = ApiUrl.TIM_GLOBAL_SHUT_UP_SET, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("设置全局禁言")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_GLOBAL_SHUT_UP_SET)
    public RestResponse set(@RequestBody @Valid TimGlobalShutUpSetParam param) {
        return timGlobalShutUpService.set(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GLOBAL_SHUT_UP_GET, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("获取用户全局禁言配置")
    public RestResponse set(@RequestBody @Valid IdListParam param) {
        List<TimGlobalShutUp> results = timGlobalShutUpService.getByUserIds(param.getIds());
        if(CollUtil.isNotEmpty(results)){
            List<TimGlobalShutUpVO> vos = results.stream().map(TimGlobalShutUpVO::new).collect(Collectors.toList());
            return RestResponse.ok(vos);
        }
        return RestResponse.OK;
    }

}
