package com.im.common.util.api.im.tencent.service.rest.impl;

import cn.hutool.core.map.MapUtil;
import com.im.common.response.RestResponse;
import com.im.common.util.CollectionUtil;
import com.im.common.util.api.im.tencent.entity.result.account.TiAccountCheckResult;
import com.im.common.util.api.im.tencent.entity.result.account.TiAccountOnlineStatusResult;
import com.im.common.util.api.im.tencent.entity.result.account.TiMultiAccountImportResult;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.AccountApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TiAccountServiceImpl implements TiAccountService {
    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse accountImport(String identifier) {
        Map<String, String> param = new HashMap<>();
        param.put("Identifier", identifier);

        return requestExecutor.execute(AccountApiEnum.account_import.getUrl(), param);
    }

    @Override
    public RestResponse accountImport(String identifier, String nick, String faceUrl) {
        Map<String, String> param = new HashMap<>();
        param.put("Identifier", identifier);
        param.put("Nick", nick);
        param.put("FaceUrl", faceUrl);

        return requestExecutor.execute(AccountApiEnum.account_import.getUrl(), param);
    }

    @Override
    public RestResponse<Map<String, Boolean>> multiAccountImport(Collection<String> usernames) {
        Map<String, Collection<String>> param = new HashMap<>();
        param.put("Accounts", usernames);

        RestResponse<TiMultiAccountImportResult> rsp = requestExecutor.execute(AccountApiEnum.multi_account_import.getUrl(),
                param, TiMultiAccountImportResult.class);
        if (!rsp.isOkRsp()) {
            return RestResponse.buildClearData(rsp);
        }
        TiMultiAccountImportResult result = rsp.getData();
        List<String> failAccounts = result.getFailAccounts();

        Map<String, Boolean> data = new HashMap<>();
        for (String username : usernames) {
            data.put(username, !failAccounts.contains(username));
        }

        return RestResponse.ok(data);
    }

    @Override
    public RestResponse<Map<String, Boolean>> accountCheck(Collection<String> usernames) {
        Map<String, List<Map<String, String>>> param = new HashMap<>();

        List<Map<String, String>> items = new ArrayList<>();
        for (String account : usernames) {
            items.add(MapUtil.of("UserID", account));
        }
        param.put("CheckItem", items);
        RestResponse<TiAccountCheckResult> rsp = requestExecutor.execute(AccountApiEnum.account_check.getUrl(),
                param, TiAccountCheckResult.class);
        if (!rsp.isOkRsp()) {
            return RestResponse.buildClearData(rsp);
        }
        TiAccountCheckResult result = rsp.getData();
        List<TiAccountCheckResult.ResultItemDTO> resultItem = result.getResultItem();

        Map<String, Boolean> data = new HashMap<>();
        for (String username : usernames) {
            TiAccountCheckResult.ResultItemDTO item = CollectionUtil.findFirst(resultItem, e -> username.equalsIgnoreCase(e.getUserID()));
            data.put(username, item != null && "Imported".equalsIgnoreCase(item.getAccountStatus()));
        }

        return RestResponse.ok(data);
    }

    @Override
    public RestResponse kick(String username) {
        Map<String, String> param = new HashMap<>();
        param.put("UserID", username);

        return requestExecutor.execute(AccountApiEnum.kick.getUrl(), param);
    }

    @Override
    public RestResponse<Map<String, Boolean>> queryOnlineStatus(List<String> usernames) {
        Map<String, Object> param = new HashMap<>();

        // param.put("IsNeedDetail", 1);
        param.put("To_Account", usernames);
        RestResponse<TiAccountOnlineStatusResult> rsp = requestExecutor.execute(AccountApiEnum.query_online_status.getUrl(),
                param, TiAccountOnlineStatusResult.class);

        if (!rsp.isOkRsp()) {
            return RestResponse.buildClearData(rsp);
        }
        TiAccountOnlineStatusResult result = rsp.getData();
        List<TiAccountOnlineStatusResult.QueryResultDTO> queryResult = result.getQueryResult();

        Map<String, Boolean> data = new HashMap<>();
        for (String username : usernames) {
            TiAccountOnlineStatusResult.QueryResultDTO qr = CollectionUtil.findFirst(queryResult, e -> username.equalsIgnoreCase(e.getToAccount()));
            data.put(username, qr != null && "Online".equalsIgnoreCase(qr.getStatus()));
        }

        return RestResponse.ok(data);
    }
}
