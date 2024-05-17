package com.im.common.util.api.im.tencent.request;


import com.im.common.response.RestResponse;

/**
 * http请求执行器.
 */
public interface RequestExecutor {
    /**
     * 执行http请求（同步请求）
     *
     * @param uri   uri
     * @param param 参数
     * @return 响应结果
     */
    RestResponse execute(String uri, Object param);

    /**
     * 执行http请求（同步请求）
     *
     * @param uri   uri
     * @param param 参数
     * @param clazz
     * @param <T>
     * @return 响应结果
     */
    <T> RestResponse<T> execute(String uri, Object param, Class<T> clazz);
}
