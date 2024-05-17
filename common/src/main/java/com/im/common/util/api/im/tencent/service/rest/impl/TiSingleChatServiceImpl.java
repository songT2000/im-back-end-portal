package com.im.common.util.api.im.tencent.service.rest.impl;

import com.im.common.cache.impl.PortalUserCache;
import com.im.common.response.RestResponse;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.api.im.tencent.entity.param.message.*;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleChatMessageHistoryResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleChatUnreadMsgNumResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageSendResult;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.SingleChatApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
import com.im.common.util.i18n.I18nTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TiSingleChatServiceImpl implements TiSingleChatService {

    private RequestExecutor requestExecutor;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @Override
    public RestResponse<TiSingleMessageSendResult> sendMessage(TiMessage tiMessage) {
        //设置offlinePushInfo
        Long userId = portalUserCache.getIdByUsernameFromLocal(tiMessage.getFromAccount());
        String nickname = portalUserCache.getNicknameByIdFromLocal(userId);
        String message = I18nTranslateUtil.translate("RSP_MSG.OFFLINE_PUSH_INFO_MESSAGE#I18N");
        TiOfflinePushInfoParam offlinePushInfo = new TiOfflinePushInfoParam(nickname, message);
        tiMessage.setOfflinePushInfo(offlinePushInfo);
        return requestExecutor.execute(SingleChatApiEnum.send_msg.getUrl(), tiMessage, TiSingleMessageSendResult.class);
    }

    @Override
    public RestResponse<TiSingleMessageSendResult> batchSendMessage(TiBatchMessageParam tiBatchMessageParam) {
        return requestExecutor.execute(SingleChatApiEnum.batch_send_msg.getUrl(), tiBatchMessageParam, TiSingleMessageSendResult.class);
    }

    @Override
    public RestResponse importMessage(TiMessageImportParam tiMessage) {
        return requestExecutor.execute(SingleChatApiEnum.import_msg.getUrl(), tiMessage);
    }

    @Override
    public RestResponse<List<TiSingleMessageResult>> queryHistory(String fromAccount, String toAccount,
                                                                  LocalDateTime startTime, LocalDateTime endTime) {
        List<TiSingleMessageResult> results = new ArrayList<>();
        Long minTime = LocalDateTimeUtil.getTimestampOfDateTime(startTime) / 1000L;
        Long maxTime = LocalDateTimeUtil.getTimestampOfDateTime(endTime) / 1000L;
        TiMessageQueryParam messageQueryParam = new TiMessageQueryParam(fromAccount, toAccount, minTime, maxTime);
        int complete;
        Long lastMsgTime = null;
        String lastMsgKey = null;
        do {
            if (lastMsgKey != null) {
                messageQueryParam.setLastMsgKey(lastMsgKey);
            }
            if (lastMsgTime != null) {
                messageQueryParam.setMaxTime(lastMsgTime);
            }
            RestResponse<TiSingleChatMessageHistoryResult> restResponse = requestExecutor.execute(SingleChatApiEnum.admin_get_roam_msg.getUrl(), messageQueryParam, TiSingleChatMessageHistoryResult.class);
            if (!restResponse.isOkRsp()) {
                break;
            }
            TiSingleChatMessageHistoryResult data = restResponse.getData();
            complete = data.getComplete();
            lastMsgTime = data.getLastMsgTime();
            lastMsgKey = data.getLastMsgKey();
            if (data.getMsgCnt() > 0) {
                results.addAll(data.getMsgList());
            }

        } while (complete == 0);


        return RestResponse.ok(results);
    }


    @Override
    public RestResponse withdraw(TiWithdrawMessageParam tiWithdrawMessageParam) {
        return requestExecutor.execute(SingleChatApiEnum.admin_msg_withdraw.getUrl(), tiWithdrawMessageParam);
    }

    @Override
    public RestResponse setMessageRead(TiSetMessageReadParam tiSetMessageReadParam) {
        return requestExecutor.execute(SingleChatApiEnum.admin_set_msg_read.getUrl(), tiSetMessageReadParam);
    }

    @Override
    public RestResponse<TiSingleChatUnreadMsgNumResult> getUnreadMsgNum(String account) {
        Map<String, Object> param = new HashMap<>();
        param.put("To_Account", account);

        return requestExecutor.execute(SingleChatApiEnum.get_c2c_unread_msg_num.getUrl(),
                param, TiSingleChatUnreadMsgNumResult.class);
    }

    @Override
    public RestResponse modifyC2cMsg(TiModifyC2cMessageParam param) {
        return requestExecutor.execute(SingleChatApiEnum.modify_c2c_msg.getUrl(), param);
    }
}
