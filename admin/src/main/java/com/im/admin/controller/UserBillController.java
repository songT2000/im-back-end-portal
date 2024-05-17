package com.im.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.UserBillPageAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBillService;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.UserBillAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 用户账变Controller
 *
 * @author Barry
 * @date 2019-11-08
 */
@RestController
@Api(tags = "用户账变相关接口")
public class UserBillController extends BaseController {
    private UserBillService userBillService;

    @Autowired
    public void setUserBillService(UserBillService userBillService) {
        this.userBillService = userBillService;
    }

    @RequestMapping(value = ApiUrl.USER_BILL_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<UserBillAdminVO>> userBillPage(@RequestBody @Valid UserBillPageAdminParam param) {
        PageVO<UserBillAdminVO> pageVO = userBillService.pageVO(param, UserBillAdminVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.USER_BILL_EXPORT, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.USER_BILL_EXPORT)
    @ApiOperation("导出")
    public void userBillExport(HttpServletResponse response,
                               @RequestBody @Valid UserBillPageAdminParam param) throws IOException {
        final int maxBetweenDays = 15;
        long betweenDays = DateTimeUtil.betweenDays(param.getStartDateTime(), param.getEndDateTime());
        if (betweenDays > maxBetweenDays) {
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.SYS_EXPORT_DATETIME_RANGE_EXCEEDED, maxBetweenDays));
            return;
        }

        List<UserBillAdminVO> listVO = userBillService.listVO(param.toQueryWrapper(null), UserBillAdminVO::new);

        // 导出
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = StrUtil.format("user-bill-{}", RandomUtil.randomToken() + ".xlsx");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("FileName", fileName);
        response.setHeader("Access-Control-Expose-Headers", "FileName");

        EasyExcel
                .write(response.getOutputStream(), UserBillAdminVO.class)
                .sheet()
                .doWrite(listVO);
    }
}
