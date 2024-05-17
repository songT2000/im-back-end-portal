package com.im.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.lock.annotation.Lock4j;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.ApiRechargeConfigCache;
import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.BankCardRechargeConfigCache;
import com.im.common.entity.PortalUser;
import com.im.common.entity.RechargeOrder;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserService;
import com.im.common.service.RechargeOrderLogService;
import com.im.common.service.RechargeOrderService;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.PortalUserRechargeAdminVO;
import com.im.common.vo.RechargeOrderAdminVO;
import com.im.common.vo.RechargeOrderLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 充值订单Controller
 *
 * @author Barry
 * @date 2021-09-07
 */
@RestController
@Api(tags = "充值订单相关接口")
public class RechargeOrderController extends BaseController {
    private RechargeOrderService rechargeOrderService;
    private BankCardRechargeConfigCache bankCardRechargeConfigCache;
    private ApiRechargeConfigCache apiRechargeConfigCache;
    private BankCache bankCache;
    private RechargeOrderLogService rechargeOrderLogService;
    private PortalUserService portalUserService;

    @Autowired
    public void setRechargeOrderService(RechargeOrderService rechargeOrderService) {
        this.rechargeOrderService = rechargeOrderService;
    }

    @Autowired
    public void setBankCardRechargeConfigCache(BankCardRechargeConfigCache bankCardRechargeConfigCache) {
        this.bankCardRechargeConfigCache = bankCardRechargeConfigCache;
    }

    @Autowired
    public void setApiRechargeConfigCache(ApiRechargeConfigCache apiRechargeConfigCache) {
        this.apiRechargeConfigCache = apiRechargeConfigCache;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setRechargeOrderLogService(RechargeOrderLogService rechargeOrderLogService) {
        this.rechargeOrderLogService = rechargeOrderLogService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.RECHARGE_ORDER_PAGE)
    @ApiOperation("日志分页")
    @ApiOperationSupport(order = 1)
    public RestResponse<PageVO<RechargeOrderLogVO>> rechargeOrderLogPage(@RequestBody @Valid RechargeOrderLogPageParam param) {
        PageVO<RechargeOrderLogVO> pageVO = rechargeOrderLogService.pageVO(param, RechargeOrderLogVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    @ApiOperationSupport(order = 2)
    public RestResponse<PageVO<RechargeOrderAdminVO>> rechargeOrderPage(@RequestBody @Valid RechargeOrderPageAdminParam param) {
        PageVO<RechargeOrderAdminVO> pageVO = rechargeOrderService
                .pageVO(param, e -> new RechargeOrderAdminVO(e, bankCardRechargeConfigCache, apiRechargeConfigCache, bankCache));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_PATCH, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.RECHARGE_ORDER_PATCH)
    @ApiOperation("补单")
    @ApiOperationSupport(order = 3)
    @Lock4j(keys = "#param.id")
    public RestResponse rechargeOrderPatch(HttpServletRequest request, @RequestBody @Valid RechargeOrderPatchAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return rechargeOrderService.patchForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_ADMIN_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.RECHARGE_ORDER_ADMIN_ADD)
    @ApiOperation("人工充值")
    @ApiOperationSupport(order = 4)
    @Lock4j(keys = "#param.username")
    public RestResponse rechargeOrderAdminAdd(HttpServletRequest request,
                                              @RequestBody @Valid RechargeOrderAdminAddParam param,
                                              @RequestIp String requestIp) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return rechargeOrderService.adminAddForAdmin(sessionUser, param, requestIp);
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_ADMIN_ADD_GET_USER_INFO, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.RECHARGE_ORDER_ADMIN_ADD)
    @ApiOperation("人工充值获取用户信息")
    @ApiOperationSupport(order = 5)
    public RestResponse<PortalUserRechargeAdminVO> rechargeOrderAdminAddGetUserInfo(@RequestBody @Valid UsernameParam param) {
        // 获取用户信息
        PortalUser user = portalUserService.getByUsername(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND, param.getUsername());
        }

        // 获取上次充值记录
        RechargeOrder lastRecharge = rechargeOrderService.getLastOrder(param.getUsername());

        return ok(new PortalUserRechargeAdminVO(user, lastRecharge));
    }

    @RequestMapping(value = ApiUrl.RECHARGE_ORDER_EXPORT, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("导出")
    @ApiOperationSupport(order = 6)
    public void rechargeOrderExport(@RequestBody @Valid RechargeOrderPageAdminParam param, HttpServletResponse response) throws IOException {
        // 最多导出7天的记录
        final int maxBetweenDays = 15;
        long betweenDays = DateTimeUtil.betweenDays(param.getStartDateTime(), param.getEndDateTime());
        if (betweenDays > maxBetweenDays) {
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.SYS_EXPORT_DATETIME_RANGE_EXCEEDED, maxBetweenDays));
            return;
        }

        List<RechargeOrderAdminVO> listVO = rechargeOrderService
                .listVO(param.toQueryWrapper(null), e -> new RechargeOrderAdminVO(e, bankCardRechargeConfigCache, apiRechargeConfigCache, bankCache));

        // 导出
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = StrUtil.format("recharge-order-{}", RandomUtil.randomToken() + ".xlsx");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("FileName", fileName);
        response.setHeader("Access-Control-Expose-Headers", "FileName");

        EasyExcel
                .write(response.getOutputStream(), RechargeOrderAdminVO.class)
                .sheet()
                .doWrite(listVO);

    }
}
