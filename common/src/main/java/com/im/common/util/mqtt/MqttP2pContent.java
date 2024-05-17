package com.im.common.util.mqtt;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT P2P消息内容
 *
 * @author Barry
 * @date 2021-03-02
 */
@Data
@NoArgsConstructor
public class MqttP2pContent extends BaseMqttContent {
    /**
     * 要发送到哪个用户
     */
    private String username;

    public MqttP2pContent(String username, String message) {
        super(message);
        this.username = username;
    }
}
