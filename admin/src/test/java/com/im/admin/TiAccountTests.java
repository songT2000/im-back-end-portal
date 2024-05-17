// package com.im.admin;
//
// import com.im.AdminApplication;
// import com.im.common.util.api.im.tencent.entity.result.account.TiAccountCheckResult;
// import com.im.common.util.api.im.tencent.entity.result.account.TiAccountOnlineStatusResult;
// import com.im.common.util.api.im.tencent.error.TencentImErrorException;
// import com.im.common.util.api.im.tencent.service.rest.TiAccountService;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.junit4.SpringRunner;
//
// import javax.annotation.Resource;
// import java.util.ArrayList;
// import java.util.List;
//
// /**
//  * 账号管理测试
//  */
// @RunWith(SpringRunner.class)
// @ActiveProfiles("mozzie")
// @SpringBootTest(classes={AdminApplication.class})// 指定启动类
// public class TiAccountTests {
//
//     @Resource
//     private TiAccountService tiAccountService;
//
//     @Test
//     public void accountImport() throws TencentImErrorException {
//
//         tiAccountService.accountImport("james");
//
//     }
//
//     @Test
//     public void multiImport() throws TencentImErrorException {
//         List<String>  accounts = new ArrayList<>();
//         for (int i = 0; i < 12; i++) {
//             accounts.add("测试账号-"+i);
//         }
//
//         List<String> strings = tiAccountService.multiAccountImport(accounts);
//         assert strings.size() == 0;
//     }
//
//     @Test
//     public void accountCheck() throws TencentImErrorException {
//         List<String>  accounts = new ArrayList<>();
//         for (int i = 0; i < 12; i++) {
//             accounts.add("测试账号-"+i);
//         }
//
//         TiAccountCheckResult tiAccountCheckResult = tiAccountService.accountCheck(accounts);
//         assert tiAccountCheckResult.getErrorCode() == 0;
//     }
//
//     @Test
//     public void kick() throws TencentImErrorException {
//         tiAccountService.kick("user2");
//     }
//
//     @Test
//     public void queryOnlineStatus() throws TencentImErrorException {
//         List<String>  accounts = new ArrayList<>();
//         for (int i = 0; i < 12; i++) {
//             accounts.add("测试账号-"+i);
//         }
//         accounts.add("user2");
//         TiAccountOnlineStatusResult tiAccountOnlineStatusResult = tiAccountService.queryOnlineStatus(accounts);
//         assert tiAccountOnlineStatusResult.getErrorCode() == 0;
//     }
//
// }
