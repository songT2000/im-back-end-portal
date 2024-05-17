//package com.im.admin;
//
//import com.im.AdminApplication;
//import com.im.common.service.SwitchAppService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//
// /**
//  * 账号管理测试
//  */
// @RunWith(SpringRunner.class)
// @ActiveProfiles("mozzie")
// @SpringBootTest(classes={AdminApplication.class})// 指定启动类
// public class SwitchAppTests {
//
//     @Resource
//     private SwitchAppService switchAppService;
//
//     @Test
//     public void accountImport() {
//
//         switchAppService.importAccount();
//
//     }
//
//    @Test
//     public void importFriendData() {
//
//         switchAppService.importFriendData();
//
//     }
//
//     @Test
//     public void importC2cMessage(){
//
//         switchAppService.importC2cMessage();
//
//     }
//
//     @Test
//     public void importGroup(){
//
//         switchAppService.importGroup();
//
//     }
//
//     @Test
//     public void importGroupMember(){
//
//         switchAppService.importGroupMember();
//
//     }
//
//     @Test
//     public void importGroupMessage(){
//
//         switchAppService.importGroupMessage(7);
//
//     }
//
// }
