package com.im.callback.service.handler;

import cn.hutool.core.collection.ListUtil;
import com.im.callback.entity.TiCbFriendAddBeforeRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.PortalUserProfile;
import com.im.common.entity.enums.AddSourceTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.entity.tim.TimFriendAwait;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserProfileService;
import com.im.common.service.TimFriendAwaitService;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendAddParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendAddResult;
import com.im.common.util.api.im.tencent.service.rest.TiFriendService;
import com.im.common.util.user.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 添加好友之前的回掉处理
 * <br>用于构建好友申请的数据
 */
@Slf4j
@Component
public class TiCbFriendAddBeforeHandler implements TiCallbackHandler<TiCbFriendAddBeforeRequest> {

    private TimFriendAwaitService timFriendAwaitService;
    private PortalUserCache portalUserCache;
    private PortalUserProfileService portalUserProfileService;
    private TiFriendService tiFriendService;

    @Autowired
    public void setTimFriendService(TimFriendAwaitService timFriendAwaitService) {
        this.timFriendAwaitService = timFriendAwaitService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Autowired
    public void setPortalUserProfileService(PortalUserProfileService portalUserProfileService) {
        this.portalUserProfileService = portalUserProfileService;
    }

    @Autowired
    public void setTiFriendService(TiFriendService tiFriendService) {
        this.tiFriendService = tiFriendService;
    }

    @Override
    public TiBaseResult handle(TiCbFriendAddBeforeRequest request, Map<String, Object> context) {
        log.info("接收到一个添加好友之前的事件:[{}]", request.toString());
        List<TimFriendAwait> list = new ArrayList<>();
        for (TiCbFriendAddBeforeRequest.FriendItemDTO dto : request.getFriendItem()) {
            Long userId = portalUserCache.getIdByUsernameFromLocal(request.getFromAccount());
            Long initiatorUserId = portalUserCache.getIdByUsernameFromLocal(request.getRequesterAccount());
            Long friendUserId = portalUserCache.getIdByUsernameFromLocal(dto.getToAccount());
            list.add(new TimFriendAwait(userId, friendUserId, initiatorUserId, dto.getAddWording(), dto.getRemark(), dto.getGroupName(), dto.getAddSource()));
        }
        timFriendAwaitService.saveBatch(list);
        //判断是否管理员添加好友，管理员添加好友不需要对方同意，直接可以添加
        for (TiCbFriendAddBeforeRequest.FriendItemDTO dto : request.getFriendItem()) {
            managerAddFriend(request.getFromAccount(), dto.getToAccount(), dto.getGroupName(), dto.getAddSource(), dto.getRemark());
        }
        return TiBaseResult.success();
    }

    /**
     * 处理管理员添加好友逻辑
     *
     * @param fromAccount 发起人账号
     * @param toAccount   接收人账号
     */
    private void managerAddFriend(String fromAccount, String toAccount, String groupName, AddSourceTypeEnum addSourceType, String remark) {
        if (isPortalInternalUser(fromAccount)) {
            //管理员添加好友，直接同意
            TiFriendAddParam.AddFriendItemDTO itemDTO = new TiFriendAddParam.AddFriendItemDTO();
            itemDTO.setToAccount(toAccount);
            if (StrUtil.isNotBlank(groupName)) {
                itemDTO.setGroupName(groupName);
            }
            itemDTO.setRemark(remark);
            itemDTO.setAddSource(addSourceType);
            TiFriendAddParam tiFriendAddParam = new TiFriendAddParam(fromAccount, ListUtil.of(itemDTO));
            RestResponse<TiFriendAddResult> restResponse = tiFriendService.addFriend(tiFriendAddParam);
            if (!restResponse.isOkRsp()) {
                log.error("管理员直接添加好友操作失败，返回消息：{}", restResponse.getMessage());
            }
        }
    }

    private Boolean isPortalInternalUser(String account) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(account, PortalTypeEnum.PORTAL);
        PortalUserProfile profile = portalUserProfileService.getById(userId);
        return profile != null ? profile.getInternalUser() : false;
    }
}
