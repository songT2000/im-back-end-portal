package com.im.common.util.api.im.tencent.service.rest;

import com.im.common.response.RestResponse;
import com.im.common.util.api.im.tencent.entity.param.operation.TiDownloadMsgParam;
import com.im.common.util.api.im.tencent.entity.result.operation.TiAppOperationResult;
import com.im.common.util.api.im.tencent.entity.result.operation.TiDownloadMsgResult;

/**
 * 运营接口
 */
public interface TiAppOperationService {

    /**
     * 通过该接口拉取最近30天的运营数据
     */
    RestResponse<TiAppOperationResult> queryOperation();

    /**
     * 请求下载消息
     * <br>App 管理员可以通过该接口获取 App 中最近7天中某天某小时的所有单发或群组消息记录的下载地址。
     *
     * @param param 请求参数
     */
    RestResponse<TiDownloadMsgResult> download(TiDownloadMsgParam param);
}
