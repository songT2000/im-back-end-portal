//package com.im.admin;
//
//import com.im.AdminApplication;
//import com.im.common.response.RestResponse;
//import com.im.common.util.api.im.tencent.entity.param.portrait.TiAccountPortraitParam;
//import com.im.common.util.api.im.tencent.service.rest.TiAccountPortraitService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//
///**
// * 资料管理测试
// */
//@RunWith(SpringRunner.class)
//@ActiveProfiles("mozzie")
//@SpringBootTest(classes = {AdminApplication.class})// 指定启动类
//public class TiPortraitTests {
//
//    @Resource
//    private TiAccountPortraitService tiAccountPortraitService;
//
//    @Test
//    public void sync() {
//        RestResponse<TiAccountPortraitParam> portrait = tiAccountPortraitService.getPortrait("jerry");
//        System.out.println(portrait);
//    }
//
//
//}
