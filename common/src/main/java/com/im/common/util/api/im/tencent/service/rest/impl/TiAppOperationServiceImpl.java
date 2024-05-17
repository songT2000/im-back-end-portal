package com.im.common.util.api.im.tencent.service.rest.impl;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.operation.TiDownloadMsgParam;
import com.im.common.util.api.im.tencent.entity.result.operation.TiAppOperationResult;
import com.im.common.util.api.im.tencent.entity.result.operation.TiDownloadMsgResult;
import com.im.common.util.api.im.tencent.request.RequestExecutor;
import com.im.common.util.api.im.tencent.request.api.OperationApiEnum;
import com.im.common.util.api.im.tencent.service.rest.TiAppOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TiAppOperationServiceImpl implements TiAppOperationService {
    private RequestExecutor requestExecutor;

    @Autowired
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public RestResponse<TiAppOperationResult> queryOperation() {
        return requestExecutor.execute(OperationApiEnum.getappinfo.getUrl(), null, TiAppOperationResult.class);
    }

    @Override
    public RestResponse<TiDownloadMsgResult> download(TiDownloadMsgParam param) {
        return requestExecutor.execute(OperationApiEnum.get_history.getUrl(), param, TiDownloadMsgResult.class);
    }
}
