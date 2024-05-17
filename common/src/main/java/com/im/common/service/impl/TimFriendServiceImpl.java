package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.AddSourceTypeEnum;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.entity.tim.TimFriendAwait;
import com.im.common.entity.tim.TimUserLoginLog;
import com.im.common.mapper.TimFriendMapper;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.AppAutoReplyService;
import com.im.common.service.TimFriendAwaitService;
import com.im.common.service.TimFriendService;
import com.im.common.service.TimUserLoginLogService;
import com.im.common.util.RandomUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendAddParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendDeleteParam;
import com.im.common.util.api.im.tencent.entity.param.message.TIMTextElem;
import com.im.common.util.api.im.tencent.entity.param.message.TiMessage;
import com.im.common.util.api.im.tencent.entity.param.message.TiMsgBody;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendAddResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendDeleteResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendResult;
import com.im.common.util.api.im.tencent.service.rest.TiFriendService;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.i18n.I18nTranslateUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimFriendServiceImpl extends MyBatisPlusServiceImpl<TimFriendMapper, TimFriend> implements TimFriendService {

    private static final Log LOG = LogFactory.get();

    private TimFriendAwaitService timFriendAwaitService;
    private PortalUserCache portalUserCache;
    private TiFriendService tiFriendService;
    private TimUserLoginLogService timUserLoginLogService;
    private TiSingleChatService tiSingleChatService;
    private AppAutoReplyService appAutoReplyService;

    @Autowired
    public void setTimFriendAwaitService(TimFriendAwaitService timFriendAwaitService) {
        this.timFriendAwaitService = timFriendAwaitService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setTiFriendService(TiFriendService tiFriendService) {
        this.tiFriendService = tiFriendService;
    }

    @Autowired
    public void setTimUserLoginLogService(TimUserLoginLogService timUserLoginLogService) {
        this.timUserLoginLogService = timUserLoginLogService;
    }

    @Autowired
    public void setTiSingleChatService(TiSingleChatService tiSingleChatService) {
        this.tiSingleChatService = tiSingleChatService;
    }

    @Autowired
    public void setAppAutoReplyService(AppAutoReplyService appAutoReplyService) {
        this.appAutoReplyService = appAutoReplyService;
    }

    @Lock4j
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchHandler(List<TimFriend> list) {
        //数据不多，直接循环处理
        for (TimFriend timFriend : list) {
            //判断是否好友关系
            if (isFriend(timFriend.getUserId(), timFriend.getFriendUserId())) {
                continue;
            }
            //查询之前的好友申请信息
            List<TimFriendAwait> friendAwaits = timFriendAwaitService.lambdaQuery().eq(TimFriendAwait::getUserId, timFriend.getFriendUserId())
                    .eq(TimFriendAwait::getFriendUserId, timFriend.getUserId()).orderByDesc(TimFriendAwait::getId).list();
            if (CollUtil.isNotEmpty(friendAwaits)) {
                TimFriendAwait timFriendAwait = friendAwaits.get(0);
                timFriend.setInitiatorUserId(timFriendAwait.getInitiatorUserId());
            }
            //创建好友关系
            save(timFriend);

            //发一条打招呼消息
            {
                TiMsgBody tiMsgBody = new TiMsgBody();
                tiMsgBody.setMsgType(TiMsgTypeEnum.TIMTextElem);
                String message = I18nTranslateUtil.translate("RSP_MSG.ADD_FRIEND_SUCCESS_MESSAGE#I18N");
                tiMsgBody.setMsgContent(new TIMTextElem(message));
                String fromAccount = portalUserCache.getUsernameByIdFromLocal(timFriend.getFriendUserId());
                String toAccount = portalUserCache.getUsernameByIdFromLocal(timFriend.getUserId());
                TiMessage tiMessage = new TiMessage(fromAccount, toAccount, RandomUtil.randomInt(100000), ListUtil.of(tiMsgBody));
                LOG.info("************* send friend first message:{}", JSON.toJSONString(tiMessage));
                tiSingleChatService.sendMessage(tiMessage);
                //自动回复
                {
                    appAutoReplyService.autoReply(fromAccount, toAccount);
                }
            }

            //查询申请好友的数据，交换位置创建好友关系
            if (!isFriend(timFriend.getFriendUserId(), timFriend.getUserId())) {
                if (CollUtil.isNotEmpty(friendAwaits)) {
                    TimFriendAwait timFriendAwait = friendAwaits.get(0);
                    TimFriend friend = new TimFriend(timFriendAwait.getUserId(), timFriendAwait.getFriendUserId(),
                            timFriendAwait.getInitiatorUserId(), timFriendAwait.getAliasName(),
                            timFriendAwait.getGroupName(), timFriendAwait.getAddSource());
                    save(friend);
                } else {
                    //如果没有申请记录，先保存关系，后续需要调用好友rest api接口补充信息
                    TimFriend friend = new TimFriend(timFriend.getFriendUserId(), timFriend.getUserId(),
                            timFriend.getFriendUserId(), StrUtil.EMPTY, StrUtil.EMPTY, AddSourceTypeEnum.Other);
                    save(friend);
                }
            }

            //删除双方的好友申请信息
            timFriendAwaitService.delete(timFriend.getUserId(), timFriend.getFriendUserId());

        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public TimFriend getByUserId(long userId, long friendUserId) {
        return lambdaQuery().eq(TimFriend::getUserId, userId).eq(TimFriend::getFriendUserId, friendUserId).one();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long userId, Long friendUserId) {
        LambdaUpdateWrapper<TimFriend> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TimFriend::getUserId, userId)
                .eq(TimFriend::getFriendUserId, friendUserId);
        remove(updateWrapper);
        LambdaUpdateWrapper<TimFriend> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.eq(TimFriend::getUserId, friendUserId)
                .eq(TimFriend::getFriendUserId, userId);
        remove(updateWrapper2);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse addForAdmin(Long userId, Long friendUserId) {
        String fromAccount = portalUserCache.getUsernameByIdFromLocal(userId);
        String toAccount = portalUserCache.getUsernameByIdFromLocal(friendUserId);
        TiFriendAddParam tiFriendAddParam = new TiFriendAddParam(fromAccount, ListUtil.of(new TiFriendAddParam.AddFriendItemDTO(toAccount, AddSourceTypeEnum.RESTAPI)));
        RestResponse<TiFriendAddResult> restResponse = tiFriendService.addFriend(tiFriendAddParam);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        TiFriendAddResult.ResultItem resultItem = restResponse.getData().getResultItems().get(0);
        if (resultItem.getResultCode() != 0) {
            //非0表示添加出错
            return RestResponse.failed(ResponseCode.SYS_REQUEST_PARAM_ERROR_WITH_MESSAGE, resultItem.getResultInfo());
        }
        batchHandler(ListUtil.of(new TimFriend(userId, friendUserId, AddSourceTypeEnum.RESTAPI)));
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse deleteForAdmin(Long userId, Long friendUserId) {
        String fromAccount = portalUserCache.getUsernameByIdFromLocal(userId);
        String toAccount = portalUserCache.getUsernameByIdFromLocal(friendUserId);
        TiFriendDeleteParam tiFriendDeleteParam = new TiFriendDeleteParam(fromAccount, ListUtil.of(toAccount));
        RestResponse<TiFriendDeleteResult> restResponse = tiFriendService.deleteFriend(tiFriendDeleteParam);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        delete(userId, friendUserId);
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResponse deleteAllForAdmin(Long userId) {
        String fromAccount = portalUserCache.getUsernameByIdFromLocal(userId);
        TiFriendDeleteParam tiFriendDeleteParam = new TiFriendDeleteParam(fromAccount);
        RestResponse<TiBaseResult> restResponse = tiFriendService.deleteAllFriend(tiFriendDeleteParam);
        if (!restResponse.isOkRsp()) {
            return restResponse;
        }
        LambdaUpdateWrapper<TimFriend> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(TimFriend::getUserId, userId);
        remove(updateWrapper);
        LambdaUpdateWrapper<TimFriend> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.eq(TimFriend::getFriendUserId, userId);
        remove(updateWrapper2);
        return RestResponse.OK;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchDelete(List<TimFriend> list) {
        //数据不多，直接循环处理
        for (TimFriend friend : list) {
            delete(friend.getUserId(), friend.getFriendUserId());
        }
    }

    /**
     * 判断userId是否已经和friendUserId建立了好友关系
     */
    private boolean isFriend(Long userId, Long friendUserId) {
        return lambdaQuery()
                .eq(TimFriend::getUserId, userId)
                .eq(TimFriend::getFriendUserId, friendUserId)
                .exists();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        //同步昨天活跃过的用户数据
        List<TimUserLoginLog> list = timUserLoginLogService.getByDate(LocalDate.parse(DateUtil.formatDate(DateUtil.yesterday())));
        if (CollUtil.isNotEmpty(list)) {
            for (TimUserLoginLog loginLog : list) {
                sync(loginLog.getUserId());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync(Long userId) {
        String account = portalUserCache.getUsernameByIdFromLocal(userId);
        RestResponse<List<TiFriendResult>> restResponse = tiFriendService.getAllFriend(account);
        if (!restResponse.isOkRsp()) {
            return;
        }
        //没有好友数据，删除本地的好友数据
        if (CollUtil.isEmpty(restResponse.getData())) {
            LambdaUpdateWrapper<TimFriend> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(TimFriend::getUserId, userId);
            remove(queryWrapper);
        }
        List<TimFriend> serverFriends = restResponse.getData().stream().map(p -> {
            TimFriend timFriend = new TimFriend(userId, portalUserCache.getIdByUsernameFromLocal(p.getFriendAccount()));
            timFriend.setInitiatorUserId(userId);
            timFriend.setAddSource(p.getAddSource());
            timFriend.setGroupName(p.getGroups());
            timFriend.setAliasName(p.getRemark());
            timFriend.setCreateTime(p.getAddTime());
            return timFriend;
        }).collect(Collectors.toList());
        List<TimFriend> localTimFriends = lambdaQuery().eq(TimFriend::getUserId, userId).list();
        //筛选出本地没有的关系链(新增)
        List<TimFriend> needInsertList = serverFriends.stream().filter(p -> !localTimFriends.contains(p)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needInsertList)) {
            saveBatch(needInsertList);
        }

        //筛选出本地有，远程没有的关系链（删除）
        List<TimFriend> needDeleteList = localTimFriends.stream().filter(p -> !serverFriends.contains(p)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needInsertList)) {
            removeByIds(needDeleteList.stream().map(TimFriend::getId).collect(Collectors.toList()));
        }

        //筛选出两边都有的关系链（更新）
        List<TimFriend> needUpdateList = localTimFriends.stream().filter(serverFriends::contains).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(needUpdateList)) {
            for (TimFriend timFriend : needUpdateList) {
                for (TimFriend serverFriend : serverFriends) {
                    if (serverFriend.getFriendUserId().equals(timFriend.getFriendUserId())) {
                        timFriend.setGroupName(serverFriend.getGroupName());
                        timFriend.setAliasName(serverFriend.getAliasName());
                    }
                }
            }
            updateBatchById(needUpdateList);
        }
    }
}
