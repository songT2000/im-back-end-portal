//package com.im.admin;
//
//import com.im.AdminApplication;
//import com.im.common.response.RestResponse;
//import com.im.common.service.AppAutoReplyService;
//import com.im.common.service.GroupRedEnvelopeService;
//import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageResult;
//import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
//import com.im.common.util.api.im.tencent.service.rest.TiSingleChatService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * 单聊消息测试
// */
//@RunWith(SpringRunner.class)
//@ActiveProfiles("mozzie")
//@SpringBootTest(classes = {AdminApplication.class})// 指定启动类
//public class TiSingleMessageTests {
//
//    @Resource
//    private TiSingleChatService tiSingleChatService;
//    @Resource
//    private TiGroupService tiGroupService;
//    @Resource
//    private GroupRedEnvelopeService groupRedEnvelopeService;
//    @Resource
//    private AppAutoReplyService appAutoReplyService;
//
//    @Test
//    public void sendMsg() {
//        appAutoReplyService.autoReply("lebron", "king");
//    }
//
//    @Test
//    public void queryHistory() {
//        RestResponse<List<TiSingleMessageResult>> response = tiSingleChatService.queryHistory("fri", "suzy",
//                LocalDateTime.parse("2022-06-11T15:31:30"), LocalDateTime.parse("2022-06-11T16:15:30"));
//
//        System.out.println(response.getData());
//    }
//}
