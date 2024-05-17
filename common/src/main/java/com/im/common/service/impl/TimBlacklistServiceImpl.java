package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.tim.TimBlacklist;
import com.im.common.entity.tim.TimUserLoginLog;
import com.im.common.mapper.TimBlacklistMapper;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.TimBlacklistService;
import com.im.common.service.TimUserLoginLogService;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistAddParam;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistDeleteParam;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistAddResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistDeleteResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistResult;
import com.im.common.util.api.im.tencent.service.rest.TiBlacklistService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimBlacklistServiceImpl extends MyBatisPlusServiceImpl<TimBlacklistMapper, TimBlacklist> implements TimBlacklistService {

    private PortalUserCache portalUserCache;
    private TiBlacklistService tiBlacklistService;
    private TimUserLoginLogService timUserLoginLogService;

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTiBlacklistService(TiBlacklistService tiBlacklistService) {
        this.tiBlacklistService = tiBlacklistService;
    }

    @Autowired
    public void setTimUserLoginLogService(TimUserLoginLogService timUserLoginLogService) {
        this.timUserLoginLogService = timUserLoginLogService;
    }

    @Lock4j
    @Override
    public void bind(List<TimBlacklist> list) {
        //数据一般只有一条
        for (TimBlacklist timBlacklist : list) {
            boolean exists = lambdaQuery().eq(TimBlacklist::getUserId, timBlacklist.getUserId())
                    .eq(TimBlacklist::getBlacklistUserId, timBlacklist.getBlacklistUserId()).exists();
            if(!exists){
                save(timBlacklist);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dismiss(List<TimBlacklist> list) {
        LambdaQueryWrapper<TimBlacklist> queryWrapper = new LambdaQueryWrapper<>();
        for (TimBlacklist timBlacklist : list) {
            queryWrapper.eq(TimBlacklist::getBlacklistUserId,timBlacklist.getBlacklistUserId())
                    .eq(TimBlacklist::getUserId,timBlacklist.getUserId());
            remove(queryWrapper);
        }
    }

    @Override
    public RestResponse addForAdmin(Long userId, Long blacklistUserId) {
        String fromAccount = portalUserCache.getUsernameByIdFromLocal(userId);
        String toAccount = portalUserCache.getUsernameByIdFromLocal(blacklistUserId);
        TiBlacklistAddParam param = new TiBlacklistAddParam(fromAccount, ListUtil.of(toAccount));
        RestResponse<TiBlacklistAddResult> restResponse = tiBlacklistService.addBlackList(param);
        if(!restResponse.isOkRsp()){
            return restResponse;
        }
        TiBlacklistAddResult.ResultItem resultItem = restResponse.getData().getResultItems().get(0);
        if(resultItem.getResultCode()!=0){
            //非0表示添加出错
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE,resultItem.getResultInfo());
        }
        bind(ListUtil.of(new TimBlacklist(userId,blacklistUserId)));
        return RestResponse.OK;
    }

    @Override
    public RestResponse deleteForAdmin(Long userId, Long blacklistUserId) {
        String fromAccount = portalUserCache.getUsernameByIdFromLocal(userId);
        String toAccount = portalUserCache.getUsernameByIdFromLocal(blacklistUserId);
        TiBlacklistDeleteParam param = new TiBlacklistDeleteParam(fromAccount, ListUtil.of(toAccount));
        RestResponse<TiBlacklistDeleteResult> restResponse = tiBlacklistService.deleteBlacklist(param);
        if(!restResponse.isOkRsp()){
            return restResponse;
        }
        dismiss(ListUtil.of(new TimBlacklist(userId,blacklistUserId)));
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        //同步昨天活跃过的用户数据
        List<TimUserLoginLog> list = timUserLoginLogService.getByDate(LocalDate.parse(DateUtil.formatDate(DateUtil.yesterday())));
        if(CollUtil.isNotEmpty(list)){
            for (TimUserLoginLog loginLog : list) {
                sync(loginLog.getUserId());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(Long userId) {
        String account = portalUserCache.getUsernameByIdFromLocal(userId);
        RestResponse<List<TiBlacklistResult>> restResponse = tiBlacklistService.getAllBlacklist(account);
        if(!restResponse.isOkRsp()){
            return;
        }
        List<TiBlacklistResult> data = restResponse.getData();
        if(CollUtil.isEmpty(data)){
            return;
        }
        //删除本地数据
        LambdaQueryWrapper<TimBlacklist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimBlacklist::getUserId,userId);
        remove(queryWrapper);

        List<TimBlacklist> list = data.stream().map(p -> {
            Long blacklistUserId = portalUserCache.getIdByUsernameFromLocal(p.getToAccount());
            TimBlacklist item = new TimBlacklist(userId, blacklistUserId);
            item.setCreateTime(p.getAddTime());
            return item;
        }).collect(Collectors.toList());
        //批量新增
        saveBatch(list);

    }
}
