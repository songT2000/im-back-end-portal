package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistAddParam;
import com.im.common.util.api.im.tencent.entity.param.blacklist.TiBlacklistDeleteParam;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistAddResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistDeleteResult;
import com.im.common.util.api.im.tencent.entity.result.blacklist.TiBlacklistResult;

import java.util.List;

/**
 * 黑名单管理接口
 */
public interface TiBlacklistService {

    /**
     * 分页拉取某个用户的所有黑名单信息
     * @param fromAccount   用户账号
     * @return              黑名单列表信息
     */
    RestResponse<List<TiBlacklistResult>> getAllBlacklist(String fromAccount);

    /**
     * 添加黑名单
     */
    RestResponse<TiBlacklistAddResult> addBlackList(TiBlacklistAddParam param);

    /**
     * 删除黑名单
     */
    RestResponse<TiBlacklistDeleteResult> deleteBlacklist(TiBlacklistDeleteParam param);

}
