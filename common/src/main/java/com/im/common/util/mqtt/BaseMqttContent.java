package com.im.common.util.mqtt;

import cn.hutool.core.util.ZipUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT消息内容
 *
 * @author Barry
 * @date 2021-03-02
 */
@Data
@NoArgsConstructor
public abstract class BaseMqttContent {
    /**
     * 发送的消息
     */
    protected String message;

    /**
     * 压缩过的消息
     */
    protected byte[] messageBytes;

    public BaseMqttContent(String message) {
        this.message = message;
        // 压缩消息，客户端要unGzip一下，测试是数据从5.4K下降到1.7K
        this.messageBytes = ZipUtil.gzip(message, "UTF-8");

    }
}
