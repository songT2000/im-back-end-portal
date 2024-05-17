package com.im.callback.service.handler;

import cn.hutool.core.collection.CollUtil;
import com.im.callback.entity.TiCbFriendBeforeResponseRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.enums.AddSourceTypeEnum;
import com.im.common.entity.enums.BaseEnum;
import com.im.common.entity.tim.TimFriend;
import com.im.common.service.TimFriendService;
import com.im.common.util.EnumUtil;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 添加好友回应之前的回掉处理
 * <br>用于构建双方的好友关系
 */
@Slf4j
@Component
public class TiCbFriendResponseBeforeHandler implements TiCallbackHandler<TiCbFriendBeforeResponseRequest> {

    private TimFriendService timFriendService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setTimFriendService(TimFriendService timFriendService) {
        this.timFriendService = timFriendService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public TiBaseResult handle(TiCbFriendBeforeResponseRequest request, Map<String, Object> context) {
        log.info("接收到一个添加好友回应之前的事件:[{}]", request.toString());
        List<TimFriend> list = new ArrayList<>();
        for (TiCbFriendBeforeResponseRequest.ResponseFriendItemDTO dto : request.getResponseFriendItem()) {
            Long userId = portalUserCache.getIdByUsernameFromLocal(request.getFromAccount());
            Long initiatorUserId = portalUserCache.getIdByUsernameFromLocal(request.getRequesterAccount());
            Long friendUserId = portalUserCache.getIdByUsernameFromLocal(dto.getToAccount());
            if("Response_Action_AgreeAndAdd".equals(dto.getResponseAction())){
                AddSourceTypeEnum typeEnum = EnumUtil.valueOfIEnum(AddSourceTypeEnum.class, "AddSource_Type_" + request.getOptPlatform());
                list.add(new TimFriend(userId,friendUserId,initiatorUserId,dto.getRemark(), dto.getTagName(),typeEnum));
            }

        }
        if(CollUtil.isNotEmpty(list)){
            timFriendService.batchHandler(list);
        }

        return TiBaseResult.success();
    }
}
