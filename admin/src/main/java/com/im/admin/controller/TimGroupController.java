package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.TimGroupEditParam;
import com.im.common.param.TimGroupIdParam;
import com.im.common.param.TimGroupPageParam;
import com.im.common.param.TimGroupShutUpParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupService;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimGroupSimpleAdminVO;
import com.im.common.vo.TimGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 群组相关接口
 */
@RestController
@Api(tags = "群组相关接口")
public class TimGroupController extends BaseController {

    private TimGroupService timGroupService;

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("群组简单列表")
    public RestResponse<List<TimGroupSimpleAdminVO>> timGroupSimpleList() {
        List<TimGroupSimpleAdminVO> list = timGroupService.listVO(TimGroupSimpleAdminVO::new);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("群组分页")
    public RestResponse<PageVO<TimGroupVO>> groupPage(@RequestBody @Valid TimGroupPageParam param) {
        PageVO<TimGroupVO> pageVO = timGroupService.pageVO(param, TimGroupVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_SHUT_UP, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_SHUT_UP)
    @ApiOperation("群组全员禁言/解除禁言")
    public RestResponse shutUp(@RequestBody @Valid TimGroupShutUpParam param) {
        return timGroupService.shutUpAllMember(param.getGroupId(), param.getEnable());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_DESTROY, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_DESTROY)
    @ApiOperation("解散群组")
    public RestResponse destroy(@RequestBody @Valid TimGroupIdParam param) {
        return timGroupService.destroyForAdmin(param.getGroupId());
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_EDIT)
    @ApiOperation("修改群组信息")
    public RestResponse edit(@RequestBody @Valid TimGroupEditParam param) {
        return timGroupService.edit(param);
    }

}
