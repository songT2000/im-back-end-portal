package com.im.common.util.api.im.tencent.service.rest.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.im.common.entity.enums.AddSourceTypeEnum;
import com.im.common.entity.enums.TiFriendItemEnum;
import com.im.common.response.RestResponse;
import com.im.common.util.EnumUtil;
import com.im.common.util.api.im.tencent.entity.TiCustomItem;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendAddParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendDeleteParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendQueryParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendImportParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.friend.*;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.FriendApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TiFriendServiceImpl implements TiFriendService {

    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse<TiFriendImportResult> importFriend(TiFriendImportParam param) {
        return requestExecutor.execute(FriendApiEnum.friend_import.getUrl(), param, TiFriendImportResult.class);
    }

    @Override
    public RestResponse<List<TiFriendResult>> getAllFriend(String fromAccount) {
        List<TiFriendResult> results = new ArrayList<>();
        TiFriendQueryParam param = new TiFriendQueryParam(fromAccount,0);
        int completeFlag;
        do {
            RestResponse<TiFriendQueryResult> restResponse = requestExecutor.execute(FriendApiEnum.friend_get.getUrl(), param, TiFriendQueryResult.class);
            if(!restResponse.isOkRsp()){
                break;
            }
            TiFriendQueryResult data = restResponse.getData();
            List<TiFriendQueryResult.UserDataItem> userDataItems = data.getUserDataItems();
            if(CollUtil.isEmpty(userDataItems)){
                break;
            }
            for (TiFriendQueryResult.UserDataItem item : userDataItems) {
                TiFriendResult result = new TiFriendResult();
                result.setFriendAccount(item.getToAccount());
                for (TiCustomItem customItem : item.getValues()) {
                    if(customItem.getTag().equals(TiFriendItemEnum.Group.getVal())){
                        result.setGroups((String) customItem.getValue());
                    }
                    if(customItem.getTag().equals(TiFriendItemEnum.Remark.getVal())){
                        result.setRemark((String) customItem.getValue());
                    }
                    if(customItem.getTag().equals(TiFriendItemEnum.AddSource.getVal())){
                        result.setAddSource(EnumUtil.valueOfIEnum(AddSourceTypeEnum.class,(String) customItem.getValue()));
                    }
                    if(customItem.getTag().equals(TiFriendItemEnum.AddWording.getVal())){
                        result.setAddWording((String) customItem.getValue());
                    }
                    if(customItem.getTag().equals(TiFriendItemEnum.AddTime.getVal())){
                        result.setAddTime(LocalDateTimeUtil.of(((Integer) customItem.getValue())*1000L));
                    }
                }
                results.add(result);
            }
            completeFlag = data.getCompleteFlag();
        }while (completeFlag==0);

        return RestResponse.ok(results);
    }

    @Override
    public RestResponse<TiFriendAddResult> addFriend(TiFriendAddParam param) {
        return requestExecutor.execute(FriendApiEnum.friend_add.getUrl(), param, TiFriendAddResult.class);
    }

    @Override
    public RestResponse<TiFriendDeleteResult> deleteFriend(TiFriendDeleteParam param) {
        return requestExecutor.execute(FriendApiEnum.friend_delete.getUrl(), param, TiFriendDeleteResult.class);
    }

    @Override
    public RestResponse<TiBaseResult> deleteAllFriend(TiFriendDeleteParam param) {
        return requestExecutor.execute(FriendApiEnum.friend_delete_all.getUrl(), param, TiBaseResult.class);
    }
}
