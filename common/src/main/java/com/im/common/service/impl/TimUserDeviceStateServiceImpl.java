package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.ActionEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimUserDeviceState;
import com.im.common.mapper.TimUserDeviceStateMapper;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserService;
import com.im.common.service.TimUserDeviceStateService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.api.im.tencent.service.rest.TiAccountService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import com.im.common.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimUserDeviceStateServiceImpl extends MyBatisPlusServiceImpl<TimUserDeviceStateMapper, TimUserDeviceState>
        implements TimUserDeviceStateService {

    private TiAccountService tiAccountService;
    private PortalUserCache portalUserCache;
    private PortalUserService portalUserService;

    @Autowired
    public void setTiAccountService(TiAccountService tiAccountService) {
        this.tiAccountService = tiAccountService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public TimUserDeviceState getLastLoginDevice(long userId) {
        List<TimUserDeviceState> list = lambdaQuery()
                .eq(TimUserDeviceState::getUserId, userId)
                .eq(TimUserDeviceState::getAction, ActionEnum.Login)
                .orderByDesc(TimUserDeviceState::getId)
                .last("limit 1")
                .list();
        return CollectionUtil.isEmpty(list) ? null : list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void change(TimUserDeviceState timUserDeviceState) {
        //只记录用户某个平台的最新状态，暂不当流水日志处理，没啥用
        //先删除用户同平台之前的记录再创建
        LambdaQueryWrapper<TimUserDeviceState> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(TimUserDeviceState::getUserId, timUserDeviceState.getUserId())
                .eq(TimUserDeviceState::getDeviceType, timUserDeviceState.getDeviceType());
        remove(queryWrapper);

        save(timUserDeviceState);
    }

    @Override
    public void syncStateFromSdk() {
        int current = 1;
        int pageSize = 100;
        LambdaQueryWrapper<TimUserDeviceState> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimUserDeviceState::getAction, ActionEnum.Login);
        while (true) {
            Page<TimUserDeviceState> page = page(new Page<>(current, pageSize), queryWrapper);
            List<TimUserDeviceState> records = page.getRecords();
            if (CollectionUtil.isNotEmpty(records)) {
                List<String> usernames = records.stream().map(p -> UserUtil.getUsernameByIdFromLocal(p.getUserId(), PortalTypeEnum.PORTAL)).collect(Collectors.toList());
                RestResponse<Map<String, Boolean>> response = tiAccountService.queryOnlineStatus(usernames);
                if (response.isOkRsp()) {
                    List<Long> offlineUserIds = new ArrayList<>();
                    Map<String, Boolean> data = response.getData();
                    for (String username : data.keySet()) {
                        if (!data.get(username)) {
                            offlineUserIds.add(UserUtil.getUserIdByUsernameFromLocal(username, PortalTypeEnum.PORTAL));
                        }
                    }
                    if (CollectionUtil.isNotEmpty(offlineUserIds)) {
                        LambdaQueryWrapper<TimUserDeviceState> q = Wrappers.lambdaQuery();
                        q.in(TimUserDeviceState::getUserId, offlineUserIds);
                        remove(q);
                    }
                }
            }
            if (!page.hasNext()) {
                break;
            }
            current++;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse kickOut(Long userId) {
        String account = portalUserCache.getUsernameByIdFromLocal(userId);
        RestResponse restResponse = tiAccountService.kick(account);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        LambdaQueryWrapper<TimUserDeviceState> queryWrapper = Wrappers.lambdaQuery();

        queryWrapper.eq(TimUserDeviceState::getUserId, userId);
        remove(queryWrapper);


        // 移除所有token
        portalUserService.kickOutAllLoginClient(userId, ResponseCode.USER_SESSION_INFO_CHANGED);

        return RestResponse.OK;
    }
}
