// package com.im.common.util.report;
//
// import com.baomidou.mybatisplus.core.toolkit.IdWorker;
// import com.im.common.constant.CommonConstant;
// import com.im.common.util.CollectionUtil;
// import com.im.common.util.NumberUtil;
// import com.im.common.vo.ReportContractAdminVO;
// import com.im.common.vo.ReportTeamBasePortalVO;
// import com.im.common.vo.ReportUserBasePortalVO;
// import com.im.common.vo.ReportVcBaseAdminVO;
//
// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.List;
//
// /**
//  * 报表工具类
//  *
//  * @author Barry
//  * @date 2020-11-15
//  */
// public class ReportUtil {
//     /**
//      * 创建用户极速交易报表，不存在会新创建，然后加到reportList里面去
//      *
//      * @param reportList
//      * @param vcId
//      * @param userId
//      * @param date
//      * @return
//      */
//     public static UserTimingTradeReport createOrFindUserTimingTradeReport(List<UserTimingTradeReport> reportList,
//                                                                           long vcId, long userId, RealTypeEnum realType, String date) {
//         UserTimingTradeReport report = CollectionUtil.findFirst(reportList, e -> vcId == e.getVcId() && userId == e.getUserId() && date.equals(e.getDate()));
//         if (report == null) {
//             report = createUserTimingTradeReport(vcId, userId, realType, date);
//             reportList.add(report);
//         }
//         return report;
//     }
//
//     /**
//      * 创建用户极速交易报表
//      *
//      * @param userId
//      * @param date
//      * @return
//      */
//     public static UserTimingTradeReport createUserTimingTradeReport(long vcId, long userId, RealTypeEnum realType, String date) {
//         UserTimingTradeReport report = new UserTimingTradeReport();
//         report.setId(IdWorker.getId());
//         report.setVcId(vcId);
//         report.setUserId(userId);
//         report.setRealType(realType);
//         report.setDate(date);
//         report.setBetAmount(BigDecimal.ZERO);
//         report.setBetCount(CommonConstant.INT_0);
//         report.setPrizeAmount(BigDecimal.ZERO);
//         report.setServiceCharge(BigDecimal.ZERO);
//         report.setUserPointAmount(BigDecimal.ZERO);
//         report.setAgentPointAmount(BigDecimal.ZERO);
//         report.setDrawRevokeAmount(BigDecimal.ZERO);
//         report.setDrawRevokeCount(CommonConstant.INT_0);
//         report.setCreateTime(LocalDateTime.now());
//         report.setUpdateTime(report.getCreateTime());
//         return report;
//     }
//
//     /**
//      * 创建用户合约交易报表，不存在会新创建，然后加到reportList里面去
//      *
//      * @param reportList
//      * @param vcId
//      * @param userId
//      * @param date
//      * @return
//      */
//     public static UserContractTradeReport createOrFindUserContractTradeReport(List<UserContractTradeReport> reportList,
//                                                                               long vcId, long userId, RealTypeEnum realType, String date) {
//         UserContractTradeReport report = CollectionUtil.findFirst(reportList, e -> vcId == e.getVcId() && userId == e.getUserId() && date.equals(e.getDate()));
//         if (report == null) {
//             report = createUserContractTradeReport(vcId, userId, realType, date);
//             reportList.add(report);
//         }
//         return report;
//     }
//
//     /**
//      * 创建用户合约交易报表
//      *
//      * @param userId
//      * @param date
//      * @return
//      */
//     public static UserContractTradeReport createUserContractTradeReport(long vcId, long userId, RealTypeEnum realType, String date) {
//         UserContractTradeReport report = new UserContractTradeReport();
//         report.setId(IdWorker.getId());
//         report.setVcId(vcId);
//         report.setUserId(userId);
//         report.setRealType(realType);
//         report.setDate(date);
//         report.setBetAmount(BigDecimal.ZERO);
//         report.setBetCount(CommonConstant.INT_0);
//         report.setWinLossAmount(BigDecimal.ZERO);
//         report.setServiceCharge(BigDecimal.ZERO);
//         report.setOvernightCharge(BigDecimal.ZERO);
//         report.setUserPointAmount(BigDecimal.ZERO);
//         report.setAgentPointAmount(BigDecimal.ZERO);
//         report.setCreateTime(LocalDateTime.now());
//         report.setUpdateTime(report.getCreateTime());
//         return report;
//     }
//
//     /**
//      * 创建用户钱包报表对象
//      *
//      * @param userId
//      * @param realType
//      * @param date
//      * @return
//      */
//     public static UserWalletReport createUserWalletReport(long vcId, long userId, RealTypeEnum realType, String date) {
//         UserWalletReport report = new UserWalletReport();
//         report.setId(IdWorker.getId());
//         report.setVcId(vcId);
//         report.setUserId(userId);
//         report.setRealType(realType);
//         report.setDate(date);
//         report.setUserRechargeAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setUserRechargeCount(CommonConstant.INT_0);
//         report.setUserRechargeServiceCharge(CommonConstant.BIG_DECIMAL_0);
//         report.setAdminRechargeAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setAdminRechargeCount(CommonConstant.INT_0);
//         report.setAdminRechargeGiveAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setAdminRechargeGiveCount(CommonConstant.INT_0);
//         report.setUserWithdrawAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setUserWithdrawCount(CommonConstant.INT_0);
//         report.setUserWithdrawServiceCharge(CommonConstant.BIG_DECIMAL_0);
//         report.setAdminWithdrawAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setAdminWithdrawCount(CommonConstant.INT_0);
//         report.setExchangeAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setExchangeCount(CommonConstant.INT_0);
//         report.setExchangeServiceCharge(CommonConstant.BIG_DECIMAL_0);
//         report.setFirstRechargeAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setFirstWithdrawAmount(CommonConstant.BIG_DECIMAL_0);
//         report.setCreateTime(LocalDateTime.now());
//         report.setUpdateTime(report.getCreateTime());
//         return report;
//     }
//
//     /**
//      * 创建用户报表对象
//      *
//      * @param userId
//      * @param realType
//      * @param date
//      * @return
//      */
//     public static UserReport createUserReport(long userId, RealTypeEnum realType, String date) {
//         UserReport report = new UserReport();
//         report.setId(IdWorker.getId());
//         report.setUserId(userId);
//         report.setRealType(realType);
//         report.setDate(date);
//         report.setRegisterCount(CommonConstant.INT_0);
//         report.setLoginCount(CommonConstant.INT_0);
//         report.setCreateTime(LocalDateTime.now());
//         report.setUpdateTime(report.getCreateTime());
//         return report;
//     }
//
//     public static BigDecimal calcProfitForAdmin(ReportVcBaseAdminVO vo) {
//         if (vo == null) {
//             return BigDecimal.ZERO;
//         }
//
//         // 平台采取负盈利的方式，负盈利就是平台赚了（用户亏了），正盈利就是平台亏了（用户赚了）
//
//         // 盈利 = 平台支出 - 用户支出
//         // 盈利 = (奖金 + 返点) - (下单 + 各种手续费(极速交易手续费 + 充值手续费 + 提现手续费 + 兑换手续费))
//
//         // 平台支出
//         BigDecimal platformOutgo = NumberUtil.add(vo.getTimingTradePrizeAmount(), vo.getTimingTradePointAmount());
//
//         // 用户支出
//         BigDecimal userOutgo = NumberUtil.add(vo.getTimingTradeBetAmount(), vo.getTimingTradeServiceCharge(),
//                 vo.getRechargeServiceCharge(), vo.getWithdrawServiceCharge(), vo.getExchangeServiceCharge());
//
//         if (NumberUtil.isEqualsZeroOrNull(platformOutgo) && NumberUtil.isEqualsZeroOrNull(userOutgo)) {
//             return BigDecimal.ZERO;
//         }
//
//         return NumberUtil.sub(platformOutgo, userOutgo);
//     }
//
//     public static BigDecimal calcProfitForAdmin(ReportContractAdminVO vo) {
//         if (vo == null) {
//             return BigDecimal.ZERO;
//         }
//
//         // 输赢金额本身就是盈亏，输赢 - 手续费 - 过夜费
//
//         return NumberUtil.sub(vo.getContractTradeWinLossAmount(), vo.getContractTradeServiceCharge(), vo.getContractTradeOvernightCharge());
//     }
//
//     public static BigDecimal calcProfitForPortal(ReportUserBasePortalVO vo) {
//         if (vo == null) {
//             return BigDecimal.ZERO;
//         }
//
//         // 平台采取负盈利的方式，负盈利就是平台赚了（用户亏了），正盈利就是平台亏了（用户赚了）
//
//         // 盈利 = 平台支出 - 用户支出
//         // 盈利 = (奖金 + 返点) - (下单 + 各种手续费(极速交易手续费 + 充值手续费 + 提现手续费 + 兑换手续费))
//
//         // 平台支出
//         BigDecimal platformOutgo = NumberUtil.add(vo.getTimingTradePrizeAmount(), vo.getTimingTradePointAmount());
//
//         // 用户支出
//         BigDecimal userOutgo = NumberUtil.add(vo.getTimingTradeBetAmount(), vo.getTimingTradeServiceCharge(),
//                 vo.getRechargeServiceCharge(), vo.getWithdrawServiceCharge(), vo.getExchangeServiceCharge());
//
//         if (NumberUtil.isEqualsZeroOrNull(platformOutgo) && NumberUtil.isEqualsZeroOrNull(userOutgo)) {
//             return BigDecimal.ZERO;
//         }
//
//         return NumberUtil.sub(platformOutgo, userOutgo);
//     }
//
//     public static BigDecimal calcProfitForPortal(ReportTeamBasePortalVO vo) {
//         if (vo == null) {
//             return BigDecimal.ZERO;
//         }
//
//         // 平台采取负盈利的方式，负盈利就是平台赚了（用户亏了），正盈利就是平台亏了（用户赚了）
//
//         // 盈利 = 平台支出 - 用户支出
//         // 盈利 = (奖金 + 返点) - (下单 + 各种手续费(极速交易手续费 + 充值手续费 + 提现手续费 + 兑换手续费))
//
//         // 平台支出
//         BigDecimal platformOutgo = NumberUtil.add(vo.getTimingTradePrizeAmount(), vo.getTimingTradePointAmount());
//
//         // 用户支出
//         BigDecimal userOutgo = NumberUtil.add(vo.getTimingTradeBetAmount(), vo.getTimingTradeServiceCharge(),
//                 vo.getRechargeServiceCharge(), vo.getWithdrawServiceCharge(), vo.getExchangeServiceCharge());
//
//         if (NumberUtil.isEqualsZeroOrNull(platformOutgo) && NumberUtil.isEqualsZeroOrNull(userOutgo)) {
//             return BigDecimal.ZERO;
//         }
//
//         return NumberUtil.sub(platformOutgo, userOutgo);
//     }
// }
