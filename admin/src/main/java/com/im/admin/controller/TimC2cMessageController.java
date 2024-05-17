package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.entity.tim.TimMessageBody;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.TimMessageC2cElemRelService;
import com.im.common.service.TimMessageC2cService;
import com.im.common.service.TimMessageGroupElemRelService;
import com.im.common.service.TimMessageGroupService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.TimMessageUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.TimMessageC2cVO;
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
import java.util.stream.Collectors;

/**
 * 单聊消息相关接口
 */
@RestController
@Api(tags = "单聊消息相关接口")
public class TimC2cMessageController extends BaseController{

    private TimMessageC2cService timMessageC2cService;
    private TimMessageC2cElemRelService timMessageC2cElemRelService;

    @Autowired
    public void setTimMessageC2cService(TimMessageC2cService timMessageC2cService) {
        this.timMessageC2cService = timMessageC2cService;
    }

    @Autowired
    public void setTimMessageC2cElemRelService(TimMessageC2cElemRelService timMessageC2cElemRelService) {
        this.timMessageC2cElemRelService = timMessageC2cElemRelService;
    }

    @RequestMapping(value = ApiUrl.TIM_C2C_MESSAGE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("单聊消息分页")
    public RestResponse<PageVO<TimMessageC2cVO>> page(@RequestBody @Valid TimC2cMessagePageParam param) {
        PageVO<TimMessageC2cVO> pageVO = timMessageC2cService.pageVO(param, TimMessageC2cVO::new);
        List<TimMessageC2cVO> records = pageVO.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<Long> messageIds = records.stream().map(TimMessageC2cVO::getId).collect(Collectors.toList());
            List<TimMessageBody> list = timMessageC2cElemRelService.getByIds(messageIds);
            for (TimMessageC2cVO record : records) {
                List<TimMessageBody> messageBodies = list.stream()
                        .filter(p -> p.getMessageId().equals(record.getId()))
                        .map(TimMessageUtil::customMessageConvert)
                        .collect(Collectors.toList());
                record.setMsgBody(messageBodies);
            }
        }
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.TIM_C2C_MESSAGE_WITHDRAW, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_C2C_MESSAGE_WITHDRAW)
    @ApiOperation("撤回单聊消息")
    public RestResponse withdraw(@RequestBody @Valid TimMessageC2cWithdrawParam param) {
        return timMessageC2cService.withdrawForAdmin(param.getFromUserId(),param.getToUserId(),param.getMsgKey());
    }

    @RequestMapping(value = ApiUrl.TIM_C2C_MESSAGE_SEND, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type= UserOperationLogTypeEnum.TIM_C2C_MESSAGE_SEND)
    @ApiOperation("发送单聊文本消息")
    public RestResponse sendGroupSystemNotification(@RequestBody @Valid TimMessageC2cSendParam param) {
        return timMessageC2cService.sendTextMessageForAdmin(param.getToUserId(),param.getContent());
    }

}
