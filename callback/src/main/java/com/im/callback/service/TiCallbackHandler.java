package com.im.callback.service;

import com.im.callback.entity.TiCallbackRequest;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;

import java.util.Map;

/**
 * 处理腾讯IM回掉函数的处理器接口.
 *
 * @author mozzie
 */
public interface TiCallbackHandler<T extends TiCallbackRequest>{

  /**
   * 处理腾讯IM回掉函数消息.
   *
   * @param requestBody    推送的业务数据
   * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
   * @return 处理结果
   */
  TiBaseResult handle(T requestBody, Map<String, Object> context);

}
