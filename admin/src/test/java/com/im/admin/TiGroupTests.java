// package com.im.admin;
//
// import cn.hutool.core.collection.ListUtil;
// import com.alibaba.fastjson.JSON;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.im.AdminApplication;
// import com.im.common.cache.impl.PortalUserCache;
// import com.im.common.entity.enums.GroupMemberRoleEnum;
// import com.im.common.entity.tim.TimGroupMember;
// import com.im.common.response.RestResponse;
// import com.im.common.service.TimGroupMemberService;
// import com.im.common.service.TimGroupService;
// import com.im.common.service.TimMessageGroupService;
// import com.im.common.util.CollectionUtil;
// import com.im.common.util.LocalDateTimeUtil;
// import com.im.common.util.api.im.tencent.entity.param.group.TiGroupAddMemberParam;
// import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMemberImportParam;
// import com.im.common.util.api.im.tencent.entity.param.group.TiGroupMessageQueryParam;
// import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMemberImportResult;
// import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageHistoryResult;
// import com.im.common.util.api.im.tencent.service.rest.TiGroupService;
// import lombok.extern.slf4j.Slf4j;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.junit4.SpringRunner;
//
// import javax.annotation.Resource;
// import java.util.List;
// import java.util.stream.Collectors;
//
// /**
//  * 群组测试
//  */
// @Slf4j
// @RunWith(SpringRunner.class)
// @ActiveProfiles("mozzie")
// @SpringBootTest(classes={AdminApplication.class})// 指定启动类
// public class TiGroupTests {
//
//     @Resource
//     private TiGroupService tiGroupService;
//
//     @Test
//     public void testHistoryMessage(){
//
//         RestResponse<TiGroupMessageHistoryResult> restResponse = tiGroupService.getGroupMessageHistory(new TiGroupMessageQueryParam("995b907b-d81c-4225-ae9e-519f719a2f90"));
//         System.out.println(JSON.toJSONString(restResponse.getData()));
//
//
//     }
//
//
//
// }
