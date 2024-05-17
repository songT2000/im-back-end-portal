// package com.im.common.util;
//
// import java.math.BigDecimal;
//
// /**
//  * 计算利润工具类
//  *
//  * @author Barry
//  * @date 2020-07-12
//  */
// public final class ProfitUtil {
//     /**
//      * 计算收入
//      *
//      * @param data 数据
//      * @return
//      */
//     public static BigDecimal calculateIncome(ReportPayWayVO data) {
//         // 收入
//         BigDecimal income = NumberUtil.add(data.getSysFeeAmountProfit(), data.getMerchantProfit());
//
//         return income;
//     }
//
//     /**
//      * 计算支出
//      *
//      * @param data 数据
//      * @return
//      */
//     public static BigDecimal calculateSpend(ReportPayWayVO data) {
//         // 支出
//         BigDecimal spend = NumberUtil.add(data.getSysFeeAmountSpend(),
//                 data.getPayWayProfit(),
//                 data.getSupplierProfit(),
//                 data.getAgentProfit());
//
//         return spend;
//     }
//
//     /**
//      * 计算支出
//      *
//      * @param data 数据
//      * @return
//      */
//     public static BigDecimal calculateProfit(ReportPayWayVO data) {
//         // 收入
//         BigDecimal income = calculateIncome(data);
//
//         // 支出
//         BigDecimal spend = calculateSpend(data);
//
//         return NumberUtil.sub(income, spend);
//     }
//
//     /**
//      * 计算利润
//      *
//      * @param data 数据
//      * @return
//      */
//     public static BigDecimal calculateProfit(ReportPayWayCompositeDataAdminVO data) {
//         // 收入
//         BigDecimal income = NumberUtil.add(data.getSysFeeAmountProfit(), data.getMerchantProfit());
//
//         // 支出
//         BigDecimal spend = NumberUtil.add(data.getSysFeeAmountSpend(),
//                 data.getPayWayProfit(),
//                 data.getSupplierProfit(),
//                 data.getAgentProfit());
//
//         // // 支出，如果后面有多个通道，那么其实提现手续费属于平台开支，不应该计算到单个支付渠道上面
//         // BigDecimal spend = NumberUtil.add(data.getFeeAmountSpend(),
//         //         data.getPayWayProfit(),
//         //         data.getSupplierProfit(),
//         //         data.getAgentProfit(),
//         //         data.getMerchantWithdrawFeeAmountSpend(),
//         //         data.getSupplierDepositWithdrawFeeAmountSpend(),
//         //         data.getSupplierCommWithdrawFeeAmountSpend(),
//         //         data.getAgentWithdrawFeeAmountSpend());
//
//         return NumberUtil.sub(income, spend);
//     }
// }
