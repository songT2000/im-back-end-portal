package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.param.UserBillPagePortalParam;
import com.im.common.response.RestResponse;
import com.im.common.service.UserBillService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserBillPortalVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户账变Controller
 *
 * @author Barry
 * @date 2020-08-06
 */
@RestController
@Api(tags = "用户账变相关接口")
@ApiSupport(order = 10)
public class UserBillController extends BaseController {
    private UserBillService userBillService;

    @Autowired
    public void setUserBillService(UserBillService userBillService) {
        this.userBillService = userBillService;
    }

    @RequestMapping(value = ApiUrl.USER_BILL_PAGE, method = RequestMethod.POST)
    @ApiOperation("我的账变分页")
    public RestResponse<PageVO<UserBillPortalVO>> userBillPage(HttpServletRequest request, @RequestBody @Valid UserBillPagePortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        PageVO<UserBillPortalVO> pageVO = userBillService.pageVO(param, sessionUser, UserBillPortalVO::new);
        return ok(pageVO);
    }
}
