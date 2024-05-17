package com.im.callback.service.handler;

import com.im.callback.entity.TiCbMemberJoinRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.callback.util.PortalUserAppOperationLogConvert;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.service.TimGroupMemberService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新成员入群之后回调处理
 */
@Slf4j
@Component
public class TiCbGroupMemberJoinHandler implements TiCallbackHandler<TiCbMemberJoinRequest> {

    private TimGroupMemberService timGroupMemberService;
    private PortalUserCache portalUserCache;
    private PortalUserAppOperationLogService portalUserAppOperationLogService;

    @Autowired
    public void setPortalUserAppOperationLogService(PortalUserAppOperationLogService portalUserAppOperationLogService) {
        this.portalUserAppOperationLogService = portalUserAppOperationLogService;
    }

    @Autowired
    public void setTimGroupMemberService(TimGroupMemberService timGroupMemberService) {
        this.timGroupMemberService = timGroupMemberService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public TiBaseResult handle(TiCbMemberJoinRequest request, Map<String, Object> context) {
        log.info("接收到一个新成员入群之后的事件:[{}]", request.toString());
        List<TiCbMemberJoinRequest.MemberListDTO> memberList = request.getMemberList();
        List<Long> members = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(memberList)) {
            for (TiCbMemberJoinRequest.MemberListDTO dto : memberList) {
                members.add(portalUserCache.getIdByUsernameFromLocal(dto.getMemberAccount()));
            }
            timGroupMemberService.join(request.getGroupId(), members);

            //记录日志
            portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        }

        return TiBaseResult.success();
    }
}
