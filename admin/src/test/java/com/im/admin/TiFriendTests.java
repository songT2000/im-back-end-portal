//package com.im.admin;// package com.im.admin;
//
//import com.im.AdminApplication;
//import com.im.common.service.TimFriendService;
//import com.im.common.service.TimGroupService;
//import com.im.common.service.TimMessageC2cService;
//import com.im.common.service.TimMessageGroupService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
//  * 好友测试
//  */
// @RunWith(SpringRunner.class)
// @ActiveProfiles("mozzie")
// @SpringBootTest(classes={AdminApplication.class})// 指定启动类
// public class TiFriendTests {
//
//
//    private TimFriendService timFriendService;
//
//    @Autowired
//    public void setTimFriendService(TimFriendService timFriendService) {
//        this.timFriendService = timFriendService;
//    }
//
//    @Test
//     public void testSync(){
//        timFriendService.sync(4L);
//     }
//
// }
