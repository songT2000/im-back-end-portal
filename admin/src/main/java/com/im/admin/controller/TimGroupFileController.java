package com.im.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimGroupFile;
import com.im.common.param.IdParam;
import com.im.common.param.TimGroupFileAddParam;
import com.im.common.param.TimGroupIdParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupFileService;
import com.im.common.util.NumberUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.vo.TimGroupFileVO;
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
 * 群文件相关接口
 */
@RestController
@Api(tags = "群文件相关接口")
public class TimGroupFileController extends BaseController{

    private TimGroupFileService timGroupFileService;

    @Autowired
    public void setTimGroupFileService(TimGroupFileService timGroupFileService) {
        this.timGroupFileService = timGroupFileService;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_FILE_LIST, method = RequestMethod.POST)
    @ApiOperation("获取群文件列表")
    public RestResponse getGroupInfo(@RequestBody @Valid TimGroupIdParam param) {
        LambdaQueryWrapper<TimGroupFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimGroupFile::getGroupId,param.getGroupId())
                .orderByDesc(TimGroupFile::getCreateTime);
        List<TimGroupFileVO> list = timGroupFileService.listVO(queryWrapper, TimGroupFileVO::new);
        return RestResponse.ok(list);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_FILE_DELETE, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_FILE_DELETE)
    @ApiOperation("删除群文件")
    @CheckPermission
    public RestResponse editNotification(@RequestBody @Valid IdParam param) {
        timGroupFileService.removeById(param.getId());
        return RestResponse.OK;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_FILE_ADD, method = RequestMethod.POST)
    @UserOperationLog(type = UserOperationLogTypeEnum.TIM_GROUP_FILE_ADD)
    @ApiOperation("保存群文件")
    @CheckPermission
    public RestResponse editIntroduction(@RequestBody @Valid TimGroupFileAddParam param) {
        TimGroupFile item = new TimGroupFile();
        item.setGroupId(param.getGroupId());
        item.setFileName(param.getFileName());
        item.setUrl(param.getUrl());
        item.setSize(NumberUtil.div(param.getSize(),1024,2));
        item.setPostfix(param.getPostfix());
        timGroupFileService.save(item);
        return RestResponse.OK;
    }

}
