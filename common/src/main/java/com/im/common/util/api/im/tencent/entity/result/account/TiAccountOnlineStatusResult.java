package com.im.common.util.api.im.tencent.entity.result.account;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiAccountOnlineStatusResult extends TiBaseResult {

    /**
     * 返回的用户在线状态结构化信息
     */
    @JSONField(name = "QueryResult")
    private List<QueryResultDTO> queryResult;
    /**
     * 状态查询失败的帐号列表，在此列表中的目标帐号，状态查询失败或目标帐号不存在。若状态全部查询成功，则 ErrorList 为空
     */
    @JSONField(name = "ErrorList")
    private List<ErrorListDTO> errorList;

    /**
     * 返回的用户在线状态结构化信息
     */
    @NoArgsConstructor
    @Data
    public static class QueryResultDTO {
        /**
         * 返回的用户的 UserID
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 返回的用户状态，目前支持的状态有：
         * 前台运行状态（Online）：客户端登录后和即时通信 IM 后台有长连接
         * 后台运行状态（PushOnline）：iOS 和 Android 进程被 kill 或因网络问题掉线，进入 PushOnline 状态，此时仍然可以接收消息的离线推送。客户端切到后台，但是进程未被手机操作系统 kill 掉时，此时状态仍是 Online
         * 未登录状态（Offline）：客户端主动退出登录或者客户端自上一次登录起7天之内未登录过
         * 如果用户是多终端登录，则只要有一个终端的状态是 Online ，该字段值就是 Online
         */
        @JSONField(name = "Status")
        private String status;
        /**
         * 详细的登录平台信息
         */
        @JSONField(name = "Detail")
        private List<DetailDTO> detail;

        /**
         * 详细的登录平台信息
         */
        @NoArgsConstructor
        @Data
        public static class DetailDTO {
            /**
             * 登录的平台类型。可能的返回值有："iPhone", "Android", "Web", "PC", "iPad", "Mac"。
             */
            @JSONField(name = "Platform")
            private String platform;
            /**
             * 该登录平台的状态
             */
            @JSONField(name = "Status")
            private String status;
        }
    }

    @NoArgsConstructor
    @Data
    public static class ErrorListDTO {
        /**
         * 状态查询失败的目标帐号
         */
        @JSONField(name = "To_Account")
        private String toAccount;
        /**
         * 状态查询失败的错误码，若目标帐号的错误码为70107，表示该帐号不存在
         */
        @JSONField(name = "ErrorCode")
        private Integer errorCode;
    }
}
