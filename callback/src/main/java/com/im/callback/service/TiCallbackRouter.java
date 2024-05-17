package com.im.callback.service;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.im.callback.entity.TiCallbackRequest;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 腾讯IM回掉路由器，通过代码化的配置，把来自腾讯IM的回掉请求交给handler处理
 */
@Slf4j
public class TiCallbackRouter {
  private static final int DEFAULT_THREAD_POOL_SIZE = 100;
  private final List<TiCallbackRouterRule> rules = new ArrayList<>();

  private ExecutorService executorService;

  public TiCallbackRouter() {
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("TiCallbackRouter-pool-%d").build();
    this.executorService = new ThreadPoolExecutor(DEFAULT_THREAD_POOL_SIZE, DEFAULT_THREAD_POOL_SIZE,
      0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), namedThreadFactory);
  }

  /**
   * 使用自定义的 {@link ExecutorService}.
   */
  public TiCallbackRouter(ExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * 如果使用默认的 {@link ExecutorService}，则系统退出前，应该调用该方法.
   */
  public void shutDownExecutorService() {
    this.executorService.shutdown();
  }


  /**
   * <pre>
   * 设置自定义的 {@link ExecutorService}
   * 如果不调用该方法，默认使用 Executors.newFixedThreadPool(100)
   * </pre>
   */
  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  List<TiCallbackRouterRule> getRules() {
    return this.rules;
  }

  /**
   * 开始一个新的Route规则.
   */
  public TiCallbackRouterRule rule() {
    return new TiCallbackRouterRule(this);
  }


  /**
   * 处理回掉消息.
   */
  public TiBaseResult route(final String event, final TiCallbackRequest request, final Map<String, Object> context) {
    if (isMsgDuplicated(request)) {
      // 如果是重复消息，那么就不做处理
      return TiBaseResult.success();
    }

    final List<TiCallbackRouterRule> matchRules = new ArrayList<>();
    // 收集匹配的规则
    for (final TiCallbackRouterRule rule : this.rules) {
      if (rule.test(event)) {
        matchRules.add(rule);
        if (!rule.isReEnter()) {
          break;
        }
      }
    }

    if (matchRules.isEmpty()) {
      //没有匹配的规格，直接返回成功，不处理该回掉事件
      return TiBaseResult.success();
    }

    TiBaseResult res = null;
    final List<Future<?>> futures = new ArrayList<>();
    for (final TiCallbackRouterRule rule : matchRules) {
      // 返回最后一个非异步的rule的执行结果
      if (rule.isAsync()) {
        futures.add(
          this.executorService.submit(() -> {
            rule.service(request, context);
          })
        );
      } else {
        res = rule.service(request, context);
      }
    }

    if (futures.isEmpty()) {
      return res;
    }

    this.executorService.submit(() -> {
      for (Future<?> future : futures) {
        try {
          future.get();
        } catch (InterruptedException e) {
          log.error("Error happened when wait task finish", e);
          Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
          log.error("Error happened when wait task finish", e);
        }
      }
    });
    return res;
  }

  public TiBaseResult route(final String event,final TiCallbackRequest request) {
    return this.route(event,request, new HashMap<>(2));
  }


  /**
   * 判断消息是否重复，暂未实现
   * @param request   消息体
   */
  private boolean isMsgDuplicated(TiCallbackRequest request) {
    return false;

  }
}
