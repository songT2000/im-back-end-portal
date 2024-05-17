package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.nospeaking.TiNoSpeakingSetParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.nospeaking.TiNoSpeakingResult;

/**
 * 禁言管理
 */
public interface TiNoSpeakingService {
    /**
     * 设置用户禁言
     *
     * @param param 禁言参数
     */
    RestResponse<TiBaseResult> set(TiNoSpeakingSetParam param);

    /**
     * 查询禁言状态
     *
     * @param account 需要查询的用户
     */
    RestResponse<TiNoSpeakingResult> query(String account);
}
