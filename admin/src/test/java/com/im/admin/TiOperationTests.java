// package com.im.admin;
//
// import com.im.AdminApplication;
// import com.im.common.service.TimOperationStatisticService;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.junit4.SpringRunner;
//
// /**
//  * 运营测试
//  */
// @RunWith(SpringRunner.class)
// @ActiveProfiles("mozzie")
// @SpringBootTest(classes={AdminApplication.class})// 指定启动类
// public class TiOperationTests {
//
//     private TimOperationStatisticService timOperationStatisticService;
//
//     @Autowired
//     public void setTimOperationStatisticService(TimOperationStatisticService timOperationStatisticService) {
//         this.timOperationStatisticService = timOperationStatisticService;
//     }
//
//     @Test
//     public void sync(){
//         timOperationStatisticService.sync();
//     }
//
//
// }
