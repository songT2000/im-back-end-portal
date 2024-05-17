package com.im.callback.service;

import com.im.callback.entity.TiCallbackRequest;

import java.util.Map;

/**
 * 回掉函数拦截器，可以用来做验证
 *
 * @author mozzie
 */
public interface TiCallbackInterceptor <T extends TiCallbackRequest>{

  /**
   * 拦截回掉函数消息
   *
   * @param requestBody    推送的业务数据
   * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
   * @return true代表OK，false代表不OK
   */
  boolean intercept(T requestBody,Map<String, Object> context);

}
