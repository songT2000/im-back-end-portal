package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.TimBlacklistService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimBlacklistVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "黑名单相关接口")
public class TimBlacklistController extends BaseController{

    private TimBlacklistService timBlacklistService;

    @Autowired
    public void setTimBlacklistService(TimBlacklistService timBlacklistService) {
        this.timBlacklistService = timBlacklistService;
    }

    @RequestMapping(value = ApiUrl.TIM_BLACKLIST_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("黑名单分页")
    public RestResponse<PageVO<TimBlacklistVO>> pageVO(@RequestBody @Valid TimBlacklistPageParam param) {
        PageVO<TimBlacklistVO> pageVO = timBlacklistService.pageVO(param, TimBlacklistVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_BLACKLIST_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_BLACKLIST_ADD)
    @ApiOperation("添加黑名单")
    public RestResponse add(@RequestBody @Valid TimBlacklistAddParam param) {
        return timBlacklistService.addForAdmin(param.getUserId(),param.getBlacklistUserId());
    }

    @RequestMapping(value = ApiUrl.TIM_BLACKLIST_DELETE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_BLACKLIST_DELETE)
    @ApiOperation("删除黑名单")
    public RestResponse delete(@RequestBody @Valid TimBlacklistDeleteParam param) {
        return timBlacklistService.deleteForAdmin(param.getUserId(),param.getBlacklistUserId());
    }

}
