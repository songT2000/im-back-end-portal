package com.im.callback.service;

import com.im.callback.entity.TiCallbackRequest;
import com.im.callback.entity.TiCbGroupMessageSendRequest;
import com.im.callback.entity.TiCbMessageSendRequest;
import com.im.callback.service.check.MessageDuplicateChecker;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;

import java.util.*;

@Data
public class TiCallbackRouterRule {

  private final TiCallbackRouter routerBuilder;

  private boolean async = true;

  private TiCallbackCommandEnum tiCallbackCommand;

  private String event;

  private boolean reEnter = false;

  private List<TiCallbackHandler> handlers = new ArrayList<>();

  private List<TiCallbackInterceptor> interceptors = new ArrayList<>();

  private MessageDuplicateChecker messageDuplicateChecker;

  public TiCallbackRouterRule(TiCallbackRouter routerBuilder) {
    this.routerBuilder = routerBuilder;
  }

  /**
   * 设置是否异步执行，默认是true
   */
  public TiCallbackRouterRule async(boolean async) {
    this.async = async;
    return this;
  }

  /**
   * 如果event等于某值
   */
  public TiCallbackRouterRule event(TiCallbackCommandEnum tiCallbackCommand) {
    this.event = tiCallbackCommand.getCommand();
    this.tiCallbackCommand = tiCallbackCommand;
    return this;
  }

  /**
   * 设置回掉消息拦截器
   */
  public TiCallbackRouterRule interceptor(TiCallbackInterceptor interceptor) {
    return interceptor(interceptor, (TiCallbackInterceptor[]) null);
  }

  /**
   * 设置回掉消息拦截器
   */
  public TiCallbackRouterRule interceptor(TiCallbackInterceptor interceptor, TiCallbackInterceptor... otherInterceptors) {
    this.interceptors.add(interceptor);
    if (otherInterceptors != null && otherInterceptors.length > 0) {
      Collections.addAll(this.interceptors, otherInterceptors);
    }
    return this;
  }

  /**
   * 设置回掉消息处理器
   */
  public TiCallbackRouterRule handler(TiCallbackHandler handler) {
    return handler(handler, (TiCallbackHandler[]) null);
  }

  /**
   * 设置消息重复检查
   */
  public TiCallbackRouterRule duplicateChecker(MessageDuplicateChecker messageDuplicateChecker){
    this.messageDuplicateChecker = messageDuplicateChecker;
    return this;
  }

  /**
   * 设置回掉消息处理器
   */
  public TiCallbackRouterRule handler(TiCallbackHandler handler, TiCallbackHandler... otherHandlers) {
    this.handlers.add(handler);
    if (otherHandlers != null && otherHandlers.length > 0) {
      this.handlers.addAll(Arrays.asList(otherHandlers));
    }
    return this;
  }

  /**
   * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则
   */
  public TiCallbackRouter end() {
    this.routerBuilder.getRules().add(this);
    return this.routerBuilder;
  }

  /**
   * 规则结束，但是消息还会进入其他规则
   */
  public TiCallbackRouter next() {
    this.reEnter = true;
    return end();
  }

  /**
   * 将腾讯IM自定义的事件修正为不区分大小写,
   */
  protected boolean test(String event) {
    return event.equalsIgnoreCase(this.event);
  }

  /**
   * 处理回掉推送过来的消息
   *
   * @param request   回掉消息
   * @return true 代表继续执行别的router，false 代表停止执行别的router
   */
  @SuppressWarnings("rawtypes")
  protected TiBaseResult service(TiCallbackRequest request, Map<String, Object> context) {

    if (context == null) {
      context = new HashMap<>();
    }
    if(isMsgDuplicated(request)){
      //如果是重复消息，直接返回
      return null;
    }

    // 如果拦截器不通过
    for (TiCallbackInterceptor<TiCallbackRequest> interceptor : this.interceptors) {
      if (!interceptor.intercept(request, context)) {
        return null;
      }
    }

    // 交给handler处理
    TiBaseResult res = null;
    for (TiCallbackHandler handler : this.handlers) {
      // 返回最后handler的结果
      if (handler == null) {
        continue;
      }
      res = handler.handle(request, context);
    }
    return res;

  }

  private boolean isMsgDuplicated(TiCallbackRequest request) {
    if(this.messageDuplicateChecker == null){
      return false;
    }

    return this.messageDuplicateChecker.isDuplicate(request);

  }
}
