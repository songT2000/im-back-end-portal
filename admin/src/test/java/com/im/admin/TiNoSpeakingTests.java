// package com.im.admin;
//
// import cn.hutool.core.date.DateUtil;
// import cn.hutool.core.date.LocalDateTimeUtil;
// import com.im.AdminApplication;
// import com.im.common.response.RestResponse;
// import com.im.common.service.TimMessageC2cService;
// import com.im.common.util.api.im.tencent.entity.param.nospeaking.TiNoSpeakingSet;
// import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageResult;
// import com.im.common.util.api.im.tencent.service.rest.TiNoSpeakingService;
// import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.junit4.SpringRunner;
//
// import javax.annotation.Resource;
// import java.util.Date;
// import java.util.List;
//
// /**
//  * 禁言测试
//  */
// @RunWith(SpringRunner.class)
// @ActiveProfiles("mozzie")
// @SpringBootTest(classes={AdminApplication.class})// 指定启动类
// public class TiNoSpeakingTests {
//
//     @Resource
//     private TiNoSpeakingService tiNoSpeakingService;
//     @Resource
//     private TiSingleChatService tiSingleChatService;
//     @Resource
//     private TimMessageC2cService timMessageC2cService;
//
//     @Test
//     public void set() {
//         TiNoSpeakingSet param = new TiNoSpeakingSet();
//         param.setSetAccount("user2");
// //        param.setC2CMsgNospeakingTime(0L);
// //        param.setGroupMsgNospeakingTime(0L);
//         param.setC2CMsgNospeakingTime(TiNoSpeakingSet.forever);
//         param.setGroupMsgNospeakingTime(TiNoSpeakingSet.forever);
//
//         tiNoSpeakingService.set(param);
//
//     }
//
//     @Test
//     public void get() {
//         RestResponse<List<TiSingleMessageResult>> restResponse = tiSingleChatService.queryHistory("user0", "user2", LocalDateTimeUtil.of(DateUtil.offsetDay(new Date(), -7)), LocalDateTimeUtil.of(new Date()));
//         assert restResponse.getData().size()>0;
//     }
//
//     @Test
//     public void sync(){
//         timMessageC2cService.sync();
//     }
//
// }
