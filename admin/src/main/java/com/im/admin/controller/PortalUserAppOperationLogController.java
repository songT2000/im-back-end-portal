package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.param.PortalUserAppOperationLogPageParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalUserAppOperationLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(tags = "app操作日志相关接口")
public class PortalUserAppOperationLogController extends BaseController {

    private PortalUserAppOperationLogService portalUserAppOperationLogService;

    @Autowired
    public void setPortalUserAppOperationLogService(PortalUserAppOperationLogService portalUserAppOperationLogService) {
        this.portalUserAppOperationLogService = portalUserAppOperationLogService;
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_APP_OPERATION_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("日志分页")
    public RestResponse<PageVO<PortalUserAppOperationLogVO>> adminOperationLogPage(@RequestBody @Valid PortalUserAppOperationLogPageParam param) {
        PageVO<PortalUserAppOperationLogVO> pageVO = portalUserAppOperationLogService.pageVO(param, PortalUserAppOperationLogVO::new);
        return ok(pageVO);
    }
}
