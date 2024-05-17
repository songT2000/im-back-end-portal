// package com.im.common.util.api.im.tencent.error;
//
// import cn.hutool.json.JSONUtil;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
//
// import java.io.Serializable;
//
// /**
//  * 腾讯IM返回消息.
//  */
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class TencentImError implements Serializable {
//   private static final long serialVersionUID = 7869786563361406291L;
//
//   /**
//    * 错误码，0为成功，其他为失败，可查询 错误码表(BaseErrorMsgEnum) 得到具体的原因.
//    */
//   private int ErrorCode;
//
//   /**
//    * 失败原因
//    */
//   private String ErrorInfo;
//
//   /**
//    * 请求处理的结果，OK 表示处理成功，FAIL 表示失败，如果为 FAIL，ErrorInfo 带上失败原因
//    */
//   private String ActionStatus;
//
//   public TencentImError(int ErrorCode, String ErrorInfo) {
//     this.ErrorCode = ErrorCode;
//     this.ErrorInfo = ErrorInfo;
//   }
//
//   public static TencentImError fromJson(String json) {
//     return JSONUtil.toBean(json,TencentImError.class);
//   }
//
// }
