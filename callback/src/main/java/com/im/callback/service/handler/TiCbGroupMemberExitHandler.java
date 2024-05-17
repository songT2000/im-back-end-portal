package com.im.callback.service.handler;

import com.im.callback.entity.TiCbMemberExitRequest;
import com.im.callback.service.TiCallbackHandler;
import com.im.callback.util.PortalUserAppOperationLogConvert;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.service.PortalUserAppOperationLogService;
import com.im.common.service.TimGroupMemberService;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 群成员离开之后回调处理
 */
@Slf4j
@Component
public class TiCbGroupMemberExitHandler implements TiCallbackHandler<TiCbMemberExitRequest> {

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
    public TiBaseResult handle(TiCbMemberExitRequest request, Map<String, Object> context) {
        log.info("接收到一个群成员离开之后的事件:[{}]", request.toString());
        List<TiCbMemberExitRequest.MemberListDTO> memberList = request.getMemberList();
        List<Long> members = new ArrayList<>();
        for (TiCbMemberExitRequest.MemberListDTO dto : memberList) {
            members.add(portalUserCache.getIdByUsernameFromLocal(dto.getMemberAccount()));
        }
        timGroupMemberService.exit(request.getGroupId(), members);

        //记录日志
        portalUserAppOperationLogService.saveBatch(PortalUserAppOperationLogConvert.convert(request));
        return TiBaseResult.success();
    }
}
