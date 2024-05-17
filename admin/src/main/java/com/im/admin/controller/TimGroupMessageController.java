package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimGroup;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGroupService;
import com.im.common.service.TimMessageGroupElemRelService;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.TimMessageUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimMessageGroupVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 群组消息相关接口
 */
@RestController
@Api(tags = "群组消息相关接口")
public class TimGroupMessageController extends BaseController{

    private TimMessageGroupService timMessageGroupService;
    private TimMessageGroupElemRelService timMessageGroupElemRelService;
    private TimGroupService timGroupService;

    @Autowired
    public void setTimMessageGroupService(TimMessageGroupService timMessageGroupService) {
        this.timMessageGroupService = timMessageGroupService;
    }

    @Autowired
    public void setTimMessageGroupElemRelService(TimMessageGroupElemRelService timMessageGroupElemRelService) {
        this.timMessageGroupElemRelService = timMessageGroupElemRelService;
    }

    @Autowired
    public void setTimGroupService(TimGroupService timGroupService) {
        this.timGroupService = timGroupService;
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MESSAGE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("群组消息分页")
    public RestResponse<PageVO<TimMessageGroupVO>> page(@RequestBody @Valid TimGroupMessagePageParam param) {
        PageVO<TimMessageGroupVO> pageVO = timMessageGroupService.pageVO(param, TimMessageGroupVO::new);
        List<TimMessageGroupVO> records = pageVO.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<Long> messageIds = records.stream().map(TimMessageGroupVO::getId).collect(Collectors.toList());
            Set<String> groupIds = records.stream().map(TimMessageGroupVO::getGroupId).collect(Collectors.toSet());
            //组装消息内容
            List<TimMessageBody> list = timMessageGroupElemRelService.getByIds(messageIds);
            for (TimMessageGroupVO record : records) {
                List<TimMessageBody> messageBodies = list.stream()
                        .filter(p -> p.getMessageId().equals(record.getId()))
                        .map(TimMessageUtil::customMessageConvert)
                        .collect(Collectors.toList());
                record.setMsgBody(messageBodies);
            }
            //组装群组内容
            List<TimGroup> groups = timGroupService.lambdaQuery().in(TimGroup::getGroupId, groupIds).list();
            for (TimGroup group : groups) {
                for (TimMessageGroupVO record : records) {
                    if(record.getGroupId().equals(group.getGroupId())){
                        record.setGroupName(group.getGroupName());
                    }
                }
            }
        }
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MESSAGE_WITHDRAW, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_GROUP_MESSAGE_WITHDRAW)
    @ApiOperation("撤回群组消息")
    public RestResponse withdraw(@RequestBody @Valid TimMessageGroupWithdrawParam param) {
        return timMessageGroupService.withdraw(param);
    }

    @RequestMapping(value = ApiUrl.TIM_GROUP_MESSAGE_SEND, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_GROUP_MESSAGE_SEND)
    @ApiOperation("发送群组系统通知")
    public RestResponse sendGroupSystemNotification(@RequestBody @Valid TimGroupSystemMessageSendParam param) {
        return timMessageGroupService.sendGroupSystemNotification(param);
    }

}
