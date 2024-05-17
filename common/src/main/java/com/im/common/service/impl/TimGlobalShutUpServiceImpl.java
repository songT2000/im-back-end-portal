package com.im.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimGlobalShutUp;
import com.im.common.mapper.TimGlobalShutUpMapper;
import com.im.common.param.TimGlobalShutUpSetParam;
import com.im.common.response.RestResponse;
import com.im.common.service.TimGlobalShutUpService;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.api.im.tencent.entity.param.nospeaking.TiNoSpeakingSetParam;
import com.im.common.util.api.im.tencent.service.rest.TiNoSpeakingService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TimGlobalShutUpServiceImpl extends MyBatisPlusServiceImpl<TimGlobalShutUpMapper, TimGlobalShutUp>
        implements TimGlobalShutUpService {

    private TiNoSpeakingService tiNoSpeakingService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setTiNoSpeakingService(TiNoSpeakingService tiNoSpeakingService) {
        this.tiNoSpeakingService = tiNoSpeakingService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse set(TimGlobalShutUpSetParam param) {
        String toAccount = portalUserCache.getUsernameByIdFromLocal(param.getUserId());
        TiNoSpeakingSetParam p = new TiNoSpeakingSetParam(toAccount,param.getC2cShutupEndTime(),param.getGroupShutupEndTime());
        RestResponse restResponse = tiNoSpeakingService.set(p);
        if(!restResponse.isOkRsp()){
            return restResponse;
        }
        TimGlobalShutUp item = new TimGlobalShutUp();
        item.setUserId(param.getUserId());
        if(param.getC2cShutupEndTime() != 0){
            item.setC2cShutupEndTime(LocalDateTimeUtil.offset(LocalDateTime.now(),param.getC2cShutupEndTime(), ChronoUnit.SECONDS));
        }
        if(param.getGroupShutupEndTime() != 0){
            item.setGroupShutupEndTime(LocalDateTimeUtil.offset(LocalDateTime.now(),param.getGroupShutupEndTime(), ChronoUnit.SECONDS));
        }

        LambdaQueryWrapper<TimGlobalShutUp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimGlobalShutUp::getUserId,param.getUserId());
        remove(queryWrapper);
        save(item);

        return RestResponse.OK;
    }

    @Override
    public TimGlobalShutUp getByUserId(Long userId) {
        return lambdaQuery().eq(TimGlobalShutUp::getUserId,userId).one();
    }

    @Override
    public List<TimGlobalShutUp> getByUserIds(List<Long> userIds) {
        return lambdaQuery().in(TimGlobalShutUp::getUserId,userIds).list();
    }
}
