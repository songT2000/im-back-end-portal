package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendAddParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendDeleteParam;
import com.im.common.util.api.im.tencent.entity.param.friend.TiFriendImportParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendAddResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendDeleteResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendResult;
import com.im.common.util.api.im.tencent.entity.result.friend.TiFriendImportResult;

import java.util.List;

/**
 * 好友关系链管理接口
 */
public interface TiFriendService {
    /**
     * 导入一个用户的所有好友信息
     *
     * @param param 导入数据
     */
    RestResponse<TiFriendImportResult> importFriend(TiFriendImportParam param);

    /**
     * 分页拉取某个用户的所有好友信息
     * @param fromAccount   用户账号
     * @return              好友列表信息
     */
    RestResponse<List<TiFriendResult>> getAllFriend(String fromAccount);

    /**
     * 添加好友
     */
    RestResponse<TiFriendAddResult> addFriend(TiFriendAddParam param);

    /**
     * 删除好友
     */
    RestResponse<TiFriendDeleteResult> deleteFriend(TiFriendDeleteParam param);

    /**
     * 删除某个用户所有的好友
     */
    RestResponse<TiBaseResult> deleteAllFriend(TiFriendDeleteParam param);
}
