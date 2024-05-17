// package com.im.common.util.api.im.tencent.error;
//
// /**
//  * 腾讯IM异常
//  * @author mozzie
//  */
// public class TencentImErrorException extends Exception {
//   private static final long serialVersionUID = -6357149550353160810L;
//
//   private final TencentImError error;
//
//   private static final int DEFAULT_ERROR_CODE = -99;
//
//   public TencentImErrorException(String message) {
//     this(TencentImError.builder().ErrorCode(DEFAULT_ERROR_CODE).ErrorInfo(message).build());
//   }
//
//   public TencentImErrorException(TencentImError tencentImError) {
//     super(ApiErrorMsgEnum.findMsgByCode(tencentImError.getErrorCode()));
//     this.error = TencentImError.builder().ErrorCode(tencentImError.getErrorCode())
//             .ErrorInfo(ApiErrorMsgEnum.findMsgByCode(tencentImError.getErrorCode()))
//             .ActionStatus(tencentImError.getActionStatus())
//             .build();
//   }
//
//   public TencentImErrorException(TencentImError error, Throwable cause) {
//     super(error.toString(), cause);
//     this.error = error;
//   }
//
//   public TencentImErrorException(Throwable cause) {
//     super(cause.getMessage(), cause);
//     this.error = TencentImError.builder().ErrorCode(DEFAULT_ERROR_CODE).ErrorInfo(cause.getMessage()).build();
//   }
//
//   public TencentImError getError() {
//     return this.error;
//   }
// }
