package com.im.callback.service.check;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <pre>
 * 默认消息重复检查器.
 * 将每个消息id保存在内存里，每隔5秒清理已经过期的消息id，每个消息id的过期时间是15秒
 * </pre>
 */
public abstract class MessageInMemoryDuplicateChecker implements MessageDuplicateChecker {

  /**
   * 一个消息ID在内存的过期时间：15秒.
   */
  private final Long timeToLive;

  /**
   * 每隔多少周期检查消息ID是否过期：5秒.
   */
  private final Long clearPeriod;

  /**
   * 消息id->消息时间戳的map.
   */
  protected final ConcurrentHashMap<String, Long> msgId2Timestamp = new ConcurrentHashMap<>();

  /**
   * 后台清理线程是否已经开启.
   */
  private final AtomicBoolean backgroundProcessStarted = new AtomicBoolean(false);

  /**
   * 无参构造方法.
   * <pre>
   * 一个消息ID在内存的过期时间：15秒
   * 每隔多少周期检查消息ID是否过期：5秒
   * </pre>
   */
  public MessageInMemoryDuplicateChecker() {
    this.timeToLive = 15 * 1000L;
    this.clearPeriod = 5 * 1000L;
  }

  /**
   * 构造方法.
   *
   * @param timeToLive  一个消息ID在内存的过期时间：毫秒
   * @param clearPeriod 每隔多少周期检查消息ID是否过期：毫秒
   */
  public MessageInMemoryDuplicateChecker(Long timeToLive, Long clearPeriod) {
    this.timeToLive = timeToLive;
    this.clearPeriod = clearPeriod;
  }

  protected void checkBackgroundProcessStarted() {
    if (this.backgroundProcessStarted.getAndSet(true)) {
      return;
    }
    Thread t = new Thread(() -> {
      try {
        while (true) {
          Thread.sleep(MessageInMemoryDuplicateChecker.this.clearPeriod);
          Long now = System.currentTimeMillis();
          MessageInMemoryDuplicateChecker.this.msgId2Timestamp.entrySet().removeIf(entry -> now - entry.getValue() > MessageInMemoryDuplicateChecker.this.timeToLive);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });
    t.setDaemon(true);
    t.start();
  }

}
