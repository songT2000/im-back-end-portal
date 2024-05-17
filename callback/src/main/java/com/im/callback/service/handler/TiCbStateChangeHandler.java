package com.im.callback.service.handler;

import cn.hutool.core.collection.CollUtil;
import com.im.callback.entity.TiCbStateChangeRequest;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.ActionEnum;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.entity.tim.TimUserLoginLog;
import com.im.common.service.TimUserDeviceStateService;
import com.im.common.service.TimUserLoginLogService;
import com.im.common.util.EnumUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.callback.service.TiCallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户状态变更回掉处理
 */
@Slf4j
@Component
public class TiCbStateChangeHandler implements TiCallbackHandler<TiCbStateChangeRequest> {

    private TimUserDeviceStateService timUserDeviceStateService;
    private TimUserLoginLogService timUserLoginLogService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setTimUserDeviceStateService(TimUserDeviceStateService timUserDeviceStateService) {
        this.timUserDeviceStateService = timUserDeviceStateService;
    }

    @Autowired
    public void setTimUserLoginLogService(TimUserLoginLogService timUserLoginLogService) {
        this.timUserLoginLogService = timUserLoginLogService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public TiBaseResult handle(TiCbStateChangeRequest request, Map<String, Object> context) {
        log.info("接收到一个状态变更事件:[{}]", request.toString());
        TimUserDeviceState deviceState = new TimUserDeviceState();
        Long userId = portalUserCache.getIdByUsernameFromLocal(request.getInfo().getToAccount());
        if(userId == null){
            log.warn("未找到用户[{}]，直接返回，请核实用户账号",request.getInfo().getToAccount());
            //没找到用户，直接返回
            return TiBaseResult.success();
        }
        deviceState.setUserId(userId);
        deviceState.setClientIp(request.getClientIp());
        deviceState.setAction(EnumUtil.valueOfIEnum(ActionEnum.class,request.getInfo().getAction()));
        deviceState.setReason(request.getInfo().getReason());
        if(CollUtil.isNotEmpty(request.getKickedDevice())){
            deviceState.setKickedDevice(request.getKickedDevice().stream().map(TiCbStateChangeRequest.KickedDeviceDTO::getPlatform).collect(Collectors.joining(StrUtil.COMMA)));
        }
        deviceState.setDeviceType(request.getOptPlatform());
        timUserDeviceStateService.change(deviceState);

        //如果是login，记录到登陆日期表，用于后续关系链和单聊记录同步使用
        if("Login".equals(request.getInfo().getAction())){
            TimUserLoginLog loginLog = new TimUserLoginLog();
            loginLog.setUserId(userId);
            loginLog.setClientIp(request.getClientIp());
            loginLog.setDeviceType(request.getOptPlatform());
            loginLog.setLoginDate(LocalDate.now());
            timUserLoginLogService.put(loginLog);
        }

        return TiBaseResult.success();
    }

}
