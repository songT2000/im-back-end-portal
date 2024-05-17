package com.im.common.util.api.im.tencent.service.rest.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.im.common.entity.enums.AddSourceTypeEnum;
import com.im.common.entity.enums.TiFriendItemEnum;
import com.im.common.response.RestResponse;
import com.im.common.util.EnumUtil;
import com.im.common.util.api.im.tencent.entity.TiCustomItem;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistAddParam;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistDeleteParam;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistQueryParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendAddParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendDeleteParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendImportParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendQueryParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistAddResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistDeleteResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistQueryResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistResult;
import com.im.common.util.api.im.tencent.entity.result.friend.*;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.FriendApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiBlacklistService;
import com.im.common.util.api.im.tencent.service.rest.TiFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TiBlacklistServiceImpl implements TiBlacklistService {

    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse<TiBlacklistAddResult> addBlackList(TiBlacklistAddParam param) {
        return requestExecutor.execute(FriendApiEnum.black_list_add.getUrl(), param, TiBlacklistAddResult.class);
    }

    @Override
    public RestResponse<TiBlacklistDeleteResult> deleteBlacklist(TiBlacklistDeleteParam param) {
        return requestExecutor.execute(FriendApiEnum.black_list_delete.getUrl(), param, TiBlacklistDeleteResult.class);
    }

    @Override
    public RestResponse<List<TiBlacklistResult>> getAllBlacklist(String fromAccount) {
        List<TiBlacklistResult> results = new ArrayList<>();

        int startIndex=0;
        long currentSequence = 0L;
        do {
            TiBlacklistQueryParam param = new TiBlacklistQueryParam(fromAccount,startIndex,currentSequence);
            RestResponse<TiBlacklistQueryResult> restResponse = requestExecutor.execute(FriendApiEnum.black_list_get.getUrl(), param, TiBlacklistQueryResult.class);
            if(!restResponse.isOkRsp()){
                break;
            }
            TiBlacklistQueryResult data = restResponse.getData();
            List<TiBlacklistQueryResult.BlackListItemDTO> userDataItems = data.getBlackListItem();
            if(CollUtil.isEmpty(userDataItems)){
                break;
            }
            List<TiBlacklistResult> list = userDataItems.stream().map(p ->
                    new TiBlacklistResult(fromAccount, p.getToAccount(), LocalDateTimeUtil.of(p.getAddBlackTimeStamp() * 1000)))
                    .collect(Collectors.toList());
            results.addAll(list);
            startIndex = data.getStartIndex();
            currentSequence = data.getCurrentSequence();
        }while (startIndex!=0);

        return RestResponse.ok(results);
    }
}
