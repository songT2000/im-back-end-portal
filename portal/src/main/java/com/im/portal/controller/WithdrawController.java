package com.im.portal.controller;

import com.baomidou.lock.annotation.Lock4j;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.param.WithdrawOrderPagePortalParam;
import com.im.common.param.WithdrawRequestPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.service.WithdrawOrderService;
import com.im.common.service.WithdrawService;
import com.im.common.service.impl.WithdrawServiceImpl;
import com.im.common.util.RequestUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.util.api.pay.base.withdraw.WithdrawConfigGroupPortalVO;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.WithdrawOrderPortalVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 提现Controller
 *
 * @author Barry
 * @date 2021-09-01
 */
@RestController
@Api(tags = "提现相关接口")
@ApiSupport(order = 14)
public class WithdrawController extends BaseController {
    private WithdrawService withdrawService;
    private WithdrawOrderService WithdrawOrderService;

    @Autowired
    public void setWithdrawService(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @Autowired
    public void setWithdrawOrderService(WithdrawOrderService WithdrawOrderService) {
        this.WithdrawOrderService = WithdrawOrderService;
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_CONFIG_LIST, method = RequestMethod.POST)
    @ApiOperation("提现配置列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<WithdrawConfigGroupPortalVO>> withdrawConfigList(HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        List<WithdrawConfigGroupPortalVO> list = withdrawService.listConfigForPortal(sessionUser);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_REQUEST, method = RequestMethod.POST)
    @ApiOperation(value = "提现请求", notes = "成功后提示用户：您的提现请求提交成功，请等待工作人员为您处理！")
    @ApiOperationSupport(order = 2)
    public RestResponse withdrawRequest(HttpServletRequest request,
                                        @RequestBody @Valid WithdrawRequestPortalParam param,
                                        @RequestIp String requestIp) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return withdrawService.requestForPortal(sessionUser, param, requestIp);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_PAGE, method = RequestMethod.POST)
    @ApiOperation(value = "我的提现订单")
    @ApiOperationSupport(order = 3)
    public RestResponse<PageVO<WithdrawOrderPortalVO>> WithdrawOrderPage(HttpServletRequest request,
                                                                         @RequestBody @Valid WithdrawOrderPagePortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        PageVO<WithdrawOrderPortalVO> page = WithdrawOrderService.pageVO(param, sessionUser, e -> new WithdrawOrderPortalVO(e));
        return ok(page);
    }

    @RequestMapping(value = WithdrawServiceImpl.COMMON_CALLBACK_PATH, method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
    @ApiOperation(value = "API代付回调", hidden = true)
    @Lock4j(keys = "#orderNum")
    public void withdrawCommonCallbackPath(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @PathVariable("orderNum") String orderNum,
                                           @RequestIp String requestIp) {
        Map<String, String> requestParams = RequestUtil.getAllRequestParams(request);

        RestResponse<String> rsp = withdrawService.callbackByApi(requestParams, orderNum, requestIp);
        if (!rsp.isOkRsp()) {
            ResponseUtil.printJson(response, rsp.toMap(false, false));
        } else {
            ResponseUtil.printText(response, rsp.getData());
        }
    }
}
