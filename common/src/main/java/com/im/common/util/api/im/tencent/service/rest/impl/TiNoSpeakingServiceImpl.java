package com.im.common.util.api.im.tencent.service.rest.impl;

import cn.hutool.core.map.MapUtil;
import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.nospeaking.TiNoSpeakingSetParam;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import com.im.common.util.api.im.tencent.entity.result.nospeaking.TiNoSpeakingResult;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.NoSpeakingApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiNoSpeakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TiNoSpeakingServiceImpl implements TiNoSpeakingService {

    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse<TiBaseResult> set(TiNoSpeakingSetParam param) {
        return requestExecutor.execute(NoSpeakingApiEnum.setnospeaking.getUrl(), param,TiBaseResult.class);
    }

    @Override
    public RestResponse<TiNoSpeakingResult> query(String account) {
        Map<String, String> map = MapUtil.of("Get_Account", account);
        return requestExecutor.execute(NoSpeakingApiEnum.getnospeaking.getUrl(), map, TiNoSpeakingResult.class);
    }
}
