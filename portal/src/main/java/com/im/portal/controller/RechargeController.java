package com.im.portal.controller;

import com.baomidou.lock.annotation.Lock4j;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.param.RechargeOrderPagePortalParam;
import com.im.common.param.RechargeRequestPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.service.RechargeOrderService;
import com.im.common.service.RechargeService;
import com.im.common.service.impl.RechargeServiceImpl;
import com.im.common.util.RequestUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.util.api.pay.base.recharge.RechargeConfigGroupPortalVO;
import com.im.common.util.api.pay.base.recharge.RechargeRequestResponseVO;
import com.im.common.util.api.pay.base.recharge.RechargeResponseBankCard;
import com.im.common.util.api.pay.base.recharge.RechargeResponseForm;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.RechargeOrderPortalVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 充值Controller
 *
 * @author Barry
 * @date 2021-09-01
 */
@RestController
@Api(tags = "充值相关接口")
@ApiSupport(order = 13)
public class RechargeController extends BaseController {
    private RechargeService rechargeService;
    private RechargeOrderService rechargeOrderService;

    @Autowired
    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @Autowired
    public void setRechargeOrderService(RechargeOrderService rechargeOrderService) {
        this.rechargeOrderService = rechargeOrderService;
    }

    @RequestMapping(value = ApiUrl.RECHARGE_CONFIG_LIST, method = RequestMethod.POST)
    @ApiOperation("充值配置列表")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<RechargeConfigGroupPortalVO>> rechargeConfigList(HttpServletRequest request) {
        PortalSessionUser sessionUser = getSessionUser(request);
        List<RechargeConfigGroupPortalVO> list = rechargeService.listConfigForPortal(sessionUser);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.RECHARGE_REQUEST, method = RequestMethod.POST)
    @ApiOperation(value = "充值请求")
    @ApiOperationSupport(order = 2)
    public RestResponse<RechargeRequestResponseVO> rechargeRequest(HttpServletRequest request,
                                                                   @RequestBody @Valid RechargeRequestPortalParam param,
                                                                   @RequestIp String requestIp) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return rechargeService.requestForPortal(sessionUser, param, requestIp);
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_PAGE, method = RequestMethod.POST)
    @ApiOperation(value = "我的充值订单")
    @ApiOperationSupport(order = 3)
    public RestResponse<PageVO<RechargeOrderPortalVO>> rechargeOrderPage(HttpServletRequest request,
                                                                         @RequestBody @Valid RechargeOrderPagePortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        PageVO<RechargeOrderPortalVO> page = rechargeOrderService.pageVO(param, sessionUser, e -> new RechargeOrderPortalVO(e));
        return ok(page);
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method10", method = RequestMethod.POST)
    @ApiOperation(value = "FORM表单[RechargeResponseForm]", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 4)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public RechargeResponseForm rechargeResponseForm() {
        return null;
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method11", method = RequestMethod.POST)
    @ApiOperation(value = "银行卡[RechargeResponseBankCard]", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 5)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public RechargeResponseBankCard rechargeResponseBankCard() {
        return null;
    }

    @RequestMapping(value = RechargeServiceImpl.COMMON_CALLBACK_PATH, method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
    @ApiOperation(value = "三方充值回调", hidden = true)
    @Lock4j(keys = "#orderNum")
    public void rechargeCommonCallbackPath(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @PathVariable("orderNum") String orderNum,
                                           @RequestIp String requestIp) {
        Map<String, String> requestParams = RequestUtil.getAllRequestParams(request);

        RestResponse<String> rsp = rechargeService.callbackByApi(requestParams, orderNum, requestIp);
        if (!rsp.isOkRsp()) {
            ResponseUtil.printJson(response, rsp.toMap(false, false));
        } else {
            ResponseUtil.printText(response, rsp.getData());
        }
    }
}
