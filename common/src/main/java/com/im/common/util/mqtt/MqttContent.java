package com.im.common.util.mqtt;

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
public class MqttContent extends BaseMqttContent {
    /**
     * 要发送到哪个主题，发送普通消息时，topic 必须和接收方订阅的 topic 一致
     * MQTT通配符语法，包含加号单级通配符（+）和井号多级通配符（#）。
     * 例如，
     * 如果为Topic1/+，则客户端可以操作Topic1/xxx的任意Topic
     * 如果为Topic1/#，则客户端可以操作Topic1/xxx/xxx/xxx的任意多级Topic
     */
    private String topic;

    public MqttContent(String topic, String message) {
        super(message);
        this.topic = topic;
    }
}
