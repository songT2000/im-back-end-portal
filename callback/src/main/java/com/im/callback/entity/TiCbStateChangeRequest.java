package com.im.callback.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 状态变更的回掉
 */
@NoArgsConstructor
@Data
public class TiCbStateChangeRequest extends TiCallbackRequest {

    private static final long serialVersionUID = 8100852175697042827L;
    /**
     * 触发本次回调的时间戳，单位为毫秒
     */
    @JSONField(name = "EventTime")
    private Long eventTime;
    /**
     * 用户上下线的信息
     */
    @JSONField(name = "Info")
    private InfoDTO info;
    /**
     * 如果本次状态变更为 Login（Register），而且有其他设备被踢下线，才会有此字段。此字段表示其他被踢下线的设备的信息。
     */
    @JSONField(name = "KickedDevice")
    private List<KickedDeviceDTO> kickedDevice;

    @NoArgsConstructor
    @Data
    public static class InfoDTO {
        /**
         * 用户上线或者下线的动作，Login 表示上线（TCP 建立），Logout 表示下线（TCP 断开），Disconnect 表示网络断开（TCP 断开）
         */
        @JSONField(name = "Action")
        private String action;
        /**
         * 用户 UserID
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 用户上下线触发的原因：
         * <li>Login 的原因有 Register：App TCP 连接建立或断网重连
         * <li>Logout 的原因有 Unregister：App 用户注销帐号导致 TCP 断开
         * <li>Disconnect 的原因有 LinkClose：即时通信 IM 检测到 App TCP 连接断开（例如 kill App，客户端发出 TCP 的 FIN 包或 RST 包）；
         * <li>TimeOut：即时通信 IM 检测到 App 心跳包超时，认为 TCP 已断开（例如客户端网络异常断开，未发出 TCP 的 FIN 包或 RST 包，也无法发送心跳包）。心跳超时时间为400秒
         */
        @JSONField(name = "Reason")
        private String reason;
    }

    @NoArgsConstructor
    @Data
    public static class KickedDeviceDTO {
        /**
         * 被踢下线的设备的平台类型，可能的取值有"iOS", "Android", "Web", "Windows", "iPad", "Mac", "Linux"
         */
        @JSONField(name = "Platform")
        private String platform;
    }
}
