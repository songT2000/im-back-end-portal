package com.im.admin.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.lock.annotation.Lock4j;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.ApiWithdrawConfigCache;
import com.im.common.cache.impl.BankCache;
import com.im.common.cache.impl.BankCardWithdrawConfigCache;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.WithdrawConfigBO;
import com.im.common.entity.PortalUser;
import com.im.common.entity.WithdrawOrder;
import com.im.common.entity.enums.UserOperationLogTypeEnum;
import com.im.common.param.*;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserService;
import com.im.common.service.WithdrawOrderLogService;
import com.im.common.service.WithdrawOrderService;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.RandomUtil;
import com.im.common.util.ResponseUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.aop.log.UserOperationLog;
import com.im.common.util.ip.RequestIp;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.AdminSessionUser;
import com.im.common.vo.PortalUserWithdrawAdminVO;
import com.im.common.vo.WithdrawOrderAdminVO;
import com.im.common.vo.WithdrawOrderLogVO;
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
 * 提现订单Controller
 *
 * @author Barry
 * @date 2021-09-07
 */
@RestController
@Api(tags = "提现订单相关接口")
public class WithdrawOrderController extends BaseController {
    private WithdrawOrderService withdrawOrderService;
    private SysConfigCache sysConfigCache;
    private BankCardWithdrawConfigCache bankCardWithdrawConfigCache;
    private BankCache bankCache;
    private ApiWithdrawConfigCache apiWithdrawConfigCache;
    private WithdrawOrderLogService withdrawOrderLogService;
    private PortalUserService portalUserService;

    @Autowired
    public void setWithdrawOrderService(WithdrawOrderService withdrawOrderService) {
        this.withdrawOrderService = withdrawOrderService;
    }

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setBankCardWithdrawConfigCache(BankCardWithdrawConfigCache bankCardWithdrawConfigCache) {
        this.bankCardWithdrawConfigCache = bankCardWithdrawConfigCache;
    }

    @Autowired
    public void setBankCache(BankCache bankCache) {
        this.bankCache = bankCache;
    }

    @Autowired
    public void setApiWithdrawConfigCache(ApiWithdrawConfigCache apiWithdrawConfigCache) {
        this.apiWithdrawConfigCache = apiWithdrawConfigCache;
    }

    @Autowired
    public void setWithdrawOrderLogService(WithdrawOrderLogService withdrawOrderLogService) {
        this.withdrawOrderLogService = withdrawOrderLogService;
    }

    @Autowired
    public void setPortalUserService(PortalUserService portalUserService) {
        this.portalUserService = portalUserService;
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_LOG_PAGE, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.WITHDRAW_ORDER_PAGE)
    @ApiOperation("日志分页")
    @ApiOperationSupport(order = 1)
    public RestResponse<PageVO<WithdrawOrderLogVO>> withdrawOrderLogPage(@RequestBody @Valid WithdrawOrderLogPageParam param) {
        PageVO<WithdrawOrderLogVO> pageVO = withdrawOrderLogService.pageVO(param, WithdrawOrderLogVO::new);
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    @ApiOperationSupport(order = 2)
    public RestResponse<PageVO<WithdrawOrderAdminVO>> WithdrawOrderPage(HttpServletRequest request,
                                                                        @RequestBody @Valid WithdrawOrderPageAdminParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();

        PageVO<WithdrawOrderAdminVO> pageVO = withdrawOrderService.pageVO(param,
                e -> new WithdrawOrderAdminVO(e, bankCardWithdrawConfigCache, bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_APPROVE_LOCK_UNLOCK, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.WITHDRAW_ORDER_APPROVE)
    @UserOperationLog(type = UserOperationLogTypeEnum.WITHDRAW_ORDER_APPROVE_LOCK_UNLOCK)
    @ApiOperation(value = "审核锁定/解锁", notes = "锁定解锁成功失败后要更新数据状态，返回的数据格式和分页一模一样")
    @ApiOperationSupport(order = 3)
    @Lock4j(keys = "#param.id")
    public RestResponse<WithdrawOrderAdminVO> withdrawOrderApproveLockUnlock(HttpServletRequest request,
                                                                             @RequestBody @Valid IdLockParam param) {

        AdminSessionUser sessionUser = getSessionUser(request);
        return withdrawOrderService.approveLockUnlockForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_APPROVE, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.WITHDRAW_ORDER_APPROVE)
    @ApiOperation(value = "审核", notes = "锁定解锁成功失败后要更新数据状态，返回的数据格式和分页一模一样")
    @ApiOperationSupport(order = 4)
    @Lock4j(keys = "#param.id")
    public RestResponse<WithdrawOrderAdminVO> withdrawOrderApprove(HttpServletRequest request,
                                                                   @RequestBody @Valid IdApproveParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return withdrawOrderService.approveForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_PAY_LOCK_UNLOCK, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.WITHDRAW_ORDER_PAY)
    @UserOperationLog(type = UserOperationLogTypeEnum.WITHDRAW_ORDER_PAY_LOCK_UNLOCK)
    @ApiOperation(value = "打款锁定/解锁", notes = "操作成功或失败后要更新数据状态，返回的数据格式和分页一模一样")
    @ApiOperationSupport(order = 5)
    @Lock4j(keys = "#param.id")
    public RestResponse<WithdrawOrderAdminVO> withdrawOrderPayLockUnlock(HttpServletRequest request,
                                                                         @RequestBody @Valid IdLockParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return withdrawOrderService.payLockUnlockForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_PAY, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.WITHDRAW_ORDER_PAY)
    @ApiOperation(value = "打款", notes = "操作成功或失败后要更新数据状态")
    @ApiOperationSupport(order = 6)
    @Lock4j(keys = "#param.id")
    public RestResponse<WithdrawOrderAdminVO> withdrawOrderPay(HttpServletRequest request,
                                                               @RequestBody @Valid WithdrawOrderPayParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return withdrawOrderService.payForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_PAY_FINISH, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.WITHDRAW_ORDER_PAY)
    @UserOperationLog(type = UserOperationLogTypeEnum.WITHDRAW_ORDER_PAY_FINISH)
    @ApiOperation(value = "到账", notes = "操作成功或失败后要更新数据状态")
    @ApiOperationSupport(order = 7)
    @Lock4j(keys = "#param.id")
    public RestResponse<WithdrawOrderAdminVO> withdrawOrderPayFinish(HttpServletRequest request,
                                                                     @RequestBody @Valid IdFinishParam param) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return withdrawOrderService.payFinishForAdmin(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_ADMIN_ADD, method = RequestMethod.POST)
    @CheckPermission
    @UserOperationLog(type = UserOperationLogTypeEnum.WITHDRAW_ORDER_ADMIN_ADD)
    @ApiOperation("人工提现")
    @ApiOperationSupport(order = 8)
    @Lock4j(keys = "#param.username")
    public RestResponse withdrawOrderAdminAdd(HttpServletRequest request,
                                              @RequestBody @Valid WithdrawOrderAdminAddParam param,
                                              @RequestIp String requestIp) {
        AdminSessionUser sessionUser = getSessionUser(request);
        return withdrawOrderService.adminAddForAdmin(sessionUser, param, requestIp);
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_ADMIN_ADD_GET_USER_INFO, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.WITHDRAW_ORDER_ADMIN_ADD)
    @ApiOperation("人工提现获取用户信息")
    @ApiOperationSupport(order = 9)
    public RestResponse<PortalUserWithdrawAdminVO> withdrawOrderAdminAddGetUserInfo(@RequestBody @Valid UsernameParam param) {
        // 获取用户信息
        PortalUser user = portalUserService.getByUsername(param.getUsername());
        if (user == null) {
            return RestResponse.failed(ResponseCode.USER_NOT_FOUND, param.getUsername());
        }

        // 获取上次提现记录
        WithdrawOrder lastWithdraw = withdrawOrderService.getLastOrder(param.getUsername());

        return ok(new PortalUserWithdrawAdminVO(user, lastWithdraw));
    }

    @RequestMapping(value = ApiUrl.WITHDRAW_ORDER_EXPORT, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("导出")
    @ApiOperationSupport(order = 10)
    public void withdrawOrderExport(HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody @Valid WithdrawOrderPageAdminParam param) throws IOException {
        final int maxBetweenDays = 15;
        long betweenDays = DateTimeUtil.betweenDays(param.getStartDateTime(), param.getEndDateTime());
        if (betweenDays > maxBetweenDays) {
            ResponseUtil.printJson(response, RestResponse.failed(ResponseCode.SYS_EXPORT_DATETIME_RANGE_EXCEEDED, maxBetweenDays));
            return;
        }

        AdminSessionUser sessionUser = getSessionUser(request);

        WithdrawConfigBO withdrawConfig = sysConfigCache.getWithdrawConfigFromRedis();

        List<WithdrawOrderAdminVO> listVO = withdrawOrderService.listVO(param.toQueryWrapper(null),
                e -> new WithdrawOrderAdminVO(e, bankCardWithdrawConfigCache, bankCache, apiWithdrawConfigCache, sessionUser, withdrawConfig));

        // 导出
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = StrUtil.format("withdraw-order-{}", RandomUtil.randomToken() + ".xlsx");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("FileName", fileName);
        response.setHeader("Access-Control-Expose-Headers", "FileName");

        EasyExcel
                .write(response.getOutputStream(), WithdrawOrderAdminVO.class)
                .sheet()
                .doWrite(listVO);
    }
}
