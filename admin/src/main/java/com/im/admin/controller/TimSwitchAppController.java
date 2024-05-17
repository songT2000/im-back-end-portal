package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.ImConfigBO;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.KeyParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SwitchAppService;
import com.im.common.util.aop.log.UserOperationLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "腾讯IM导入数据（切换账号）相关接口")
public class TimSwitchAppController extends BaseController{

    private SwitchAppService switchAppService;
    private SysConfigCache sysConfigCache;

    @Autowired
    public void setSwitchAppService(SwitchAppService switchAppService) {
        this.switchAppService = switchAppService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @RequestMapping(value = ApiUrl.TIM_SWITCH_APP_ID_GET, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("获取腾讯IM的appId信息")
    public RestResponse getSdkId() {
        ImConfigBO config = sysConfigCache.getImConfigFromLocal();
        return RestResponse.ok(config.getTecentImSdkAppid());
    }

    @RequestMapping(value = ApiUrl.TIM_SWITCH_KICK_ALL, method = RequestMethod.POST)
    @ApiOperation("下线所有账号")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_SWITCH_KICK_ALL)
    public RestResponse kickOutAll() {
        switchAppService.kickOutAll();
        return RestResponse.OK;
    }

    @RequestMapping(value = ApiUrl.TIM_SWITCH_IMPORT_ACCOUNT, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("导入已有的账号")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_SWITCH_IMPORT_ACCOUNT)
    public RestResponse importAccount() {
        switchAppService.importAccount();
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT.getVal());
    }

    @RequestMapping(value = ApiUrl.TIM_SET_ACCOUNT_PORTRAIT, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("设置账号信息")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_SET_ACCOUNT_PORTRAIT)
    public RestResponse setAccountPortrait() {
        switchAppService.setAccountPortrait();
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_ACCOUNT_PORTRAIT.getVal());
    }

    @RequestMapping(value = ApiUrl.TIM_IMPORT_FRIENDSHIP, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("导入用户好友数据")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_IMPORT_FRIENDSHIP)
    public RestResponse importFriendData() {
        switchAppService.importFriendData();
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_FRIEND.getVal());
    }

    @RequestMapping(value = ApiUrl.TIM_IMPORT_C2C_MESSAGE, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("导入单聊消息")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_IMPORT_C2C_MESSAGE)
    public RestResponse importC2cMessage() {
        switchAppService.importC2cMessage();
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_C2C_MESSAGE.getVal());
    }

    @RequestMapping(value = ApiUrl.TIM_IMPORT_GROUP, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("导入群组")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_IMPORT_GROUP)
    public RestResponse importGroup() {
        switchAppService.importGroup();
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP.getVal());
    }


    @RequestMapping(value = ApiUrl.TIM_IMPORT_GROUP_MEMBER, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("导入群组成员")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_IMPORT_GROUP_MEMBER)
    public RestResponse importGroupMember() {
        switchAppService.importGroupMember();
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MEMBER.getVal());
    }

    @RequestMapping(value = ApiUrl.TIM_IMPORT_GROUP_MESSAGE, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.TIM_SWITCH_APP_ID_GET)
    @ApiOperation("导入群组消息")
    @UserOperationLog(type=UserOperationLogTypeEnum.TIM_IMPORT_GROUP_MESSAGE)
    public RestResponse importGroupMessage() {
        int maxDay = 7;//最大存储消息时长（旗舰版是30天，体验版是7天）
        switchAppService.importGroupMessage(maxDay);
        return RestResponse.ok(RedisKeyEnum.SWITCH_APP_IMPORT_GROUP_MESSAGE.getVal());
    }

    @RequestMapping(value = ApiUrl.TIM_SWITCH_PROCESS, method = RequestMethod.POST)
    @ApiOperation("获取导入进度")
    public RestResponse getProcess(@Validated @RequestBody KeyParam param) {
        return switchAppService.getProcess(param.getKey());
    }
}
