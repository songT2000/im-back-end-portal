// package com.im.scheduler.task;
//
// import cn.hutool.log.Log;
// import cn.hutool.log.LogFactory;
// import com.im.common.cache.impl.SysConfigCache;
// import com.im.common.cache.sysconfig.bo.ReportConfigBO;
// import com.im.common.entity.PortalUser;
// import com.im.common.service.PortalUserService;
// import com.im.common.service.UserBalanceSnapshotService;
// import com.im.common.util.CollectionUtil;
// import com.im.common.util.DateTimeUtil;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import java.util.List;
//
// /**
//  * 用户余额快照
//  *
//  * @author Max
//  * @date 2020-01-02
//  */
// @Component
// public class UserBalanceSnapshotTask {
//     private static final Log LOG = LogFactory.get();
//
//     private static final String TASK_NAME = "用户余额快照任务";
//
//     private UserBalanceSnapshotService userBalanceSnapshotService;
//     private SysConfigCache sysConfigCache;
//     private PortalUserService portalUserService;
//
//     @Scheduled(cron = "0/10 * * * * ?")
//     private void task() {
//         // todo 余额快照，必须在报表时间结束以后再统计
//         ReportConfigBO reportConfigFromRedis = sysConfigCache.getReportConfigFromLocal();
//         String offsetTimeStr = reportConfigFromRedis.getOffsetTime();
//         String nowTimeStr = DateTimeUtil.getNowStr(DateTimeUtil.TimeFormat.TIME_PATTERN_LINE);
//         if (offsetTimeStr.equals(nowTimeStr)) {
//             LOG.info("开始执行[{}]", TASK_NAME);
//             String date = DateTimeUtil.minusDaysFromNowToDateStr(1);
//             start(date);
//             LOG.info("-------完成执行[{}]", TASK_NAME);
//         }
//     }
//
//     private void start(String date) {
//         try {
//             // 前台用户余额快照
//             List<PortalUser> portalUsers = portalUserService.listBalanceSnapshot();
//             if (CollectionUtil.isNotEmpty(portalUsers)) {
//                 for (PortalUser user : portalUsers) {
//                     userBalanceSnapshotService.add(user, date);
//                 }
//             }
//         } catch (Exception e) {
//             LOG.error(e, "执行[{}]失败", TASK_NAME);
//         }
//     }
//
//     @Autowired
//     public void setUserBalanceSnapshotService(UserBalanceSnapshotService userBalanceSnapshotService) {
//         this.userBalanceSnapshotService = userBalanceSnapshotService;
//     }
//
//     @Autowired
//     public void setSysConfigCache(SysConfigCache sysConfigCache) {
//         this.sysConfigCache = sysConfigCache;
//     }
//
//     @Autowired
//     public void setPortalUserService(PortalUserService portalUserService) {
//         this.portalUserService = portalUserService;
//     }
// }
