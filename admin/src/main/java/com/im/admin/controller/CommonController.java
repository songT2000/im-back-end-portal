package com.im.admin.controller;

import com.im.admin.controller.url.ApiUrl;
import com.im.common.param.UsernamePortalTypeParam;
import com.im.common.response.RestResponse;
import com.im.common.util.user.UserUtil;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.TodoAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 通用Controller
 *
 * @author Barry
 * @date 2019/10/12
 */
@RestController
@Api(tags = "通用接口")
public class CommonController extends BaseController {
    @RequestMapping(value = ApiUrl.USER_USERNAME_EXIST, method = RequestMethod.POST)
    @ApiOperation("判断任意类型用户名是否存在")
    public RestResponse<Boolean> userUsernameExist(@RequestBody @Valid UsernamePortalTypeParam param) {
        Long userId = UserUtil.getUserIdByUsernameFromLocal(param.getUsername(), param.getPortalType());
        return ok(userId != null);
    }

    @RequestMapping(value = ApiUrl.TODO, method = RequestMethod.POST)
    @ApiOperation(value = "待办事项", notes = "针对每一项都需要有相应语音提示")
    public RestResponse<TodoAdminVO> todo(HttpServletRequest request) {
        AdminSessionUser sessionUser = getSessionUser(request);

        TodoAdminVO data = new TodoAdminVO();

        // // 供应商银行卡流水未匹配
        // int unMatchCount = supplierBankCardBillService.countUnMatch();
        // data.setSupplierBankCardBillUnMatchCount(unMatchCount);

        return ok(data);
    }
}
