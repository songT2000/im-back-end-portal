package com.im.portal.controller;

import com.baomidou.lock.annotation.Lock4j;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.param.GroupRedEnvelopeSendPortalParam;
import com.im.common.param.IdParam;
import com.im.common.param.PersonalRedEnvelopeSendPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.service.GroupRedEnvelopeService;
import com.im.common.service.PersonalRedEnvelopeService;
import com.im.common.util.aop.limit.RequestLimit;
import com.im.common.vo.GroupRedEnvelopePortalVO;
import com.im.common.vo.GroupRedEnvelopeReceivePortalVO;
import com.im.common.vo.PersonalRedEnvelopePortalVO;
import com.im.common.vo.PortalSessionUser;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 红包Controller
 *
 * @author Barry
 * @date 2021-12-20
 */
@RestController
@Api(tags = "红包相关接口")
@ApiSupport(order = 9)
public class RedEnvelopeController extends BaseController {
    private PersonalRedEnvelopeService personalRedEnvelopeService;
    private GroupRedEnvelopeService groupRedEnvelopeService;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setPersonalRedEnvelopeService(PersonalRedEnvelopeService personalRedEnvelopeService) {
        this.personalRedEnvelopeService = personalRedEnvelopeService;
    }

    @Autowired
    public void setGroupRedEnvelopeService(GroupRedEnvelopeService groupRedEnvelopeService) {
        this.groupRedEnvelopeService = groupRedEnvelopeService;
    }

    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @RequestMapping(value = ApiUrl.PERSONAL_RED_ENVELOPE_SEND, method = RequestMethod.POST)
    @ApiOperation(value = "发个人红包", notes = "返回红包ID，已处理IM消息，详见[个人红包消息说明]")
    @RequestLimit
    @ApiOperationSupport(order = 1)
    public RestResponse<Long> personalRedEnvelopeSend(HttpServletRequest request, @RequestBody @Valid PersonalRedEnvelopeSendPortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return personalRedEnvelopeService.sendForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PERSONAL_RED_ENVELOPE_RECEIVE, method = RequestMethod.POST)
    @ApiOperation(value = "领个人红包", notes = "只能是领取人领取，已处理IM消息，详见[个人红包消息说明]")
    @Lock4j(keys = "#param.id")
    @ApiOperationSupport(order = 2)
    public RestResponse personalRedEnvelopeReceive(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return personalRedEnvelopeService.receiveForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PERSONAL_RED_ENVELOPE_REFUND, method = RequestMethod.POST)
    @ApiOperation(value = "退个人红包", notes = "只能是领取人退回，已处理IM消息，详见[个人红包消息说明]")
    @Lock4j(keys = "#param.id")
    @ApiOperationSupport(order = 3)
    public RestResponse personalRedEnvelopeRefund(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return personalRedEnvelopeService.refundForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.PERSONAL_RED_ENVELOPE_GET, method = RequestMethod.POST)
    @ApiOperation(value = "获取个人红包")
    @ApiOperationSupport(order = 4)
    public RestResponse<PersonalRedEnvelopePortalVO> personalRedEnvelopeGet(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        PersonalRedEnvelopePortalVO vo = personalRedEnvelopeService.getForPortal(sessionUser, param);
        return ok(vo);
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method12", method = RequestMethod.POST)
    @ApiOperation(value = "个人红包消息说明", notes = "该接口不可调用，仅说明每个字段的含义<br/>" +
            "<span style='color: red;font-family:bold;font-size: 20px;'>发个人红包/领个人红包/退个人红包/红包过期，只要红包状态发生变化，服务器都会推送消息过来</span><br/>")
    @ApiOperationSupport(order = 5)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public PersonalRedEnvelopePortalVO personalRedEnvelopeImVO() {
        return null;
    }

    @RequestMapping(value = ApiUrl.GROUP_RED_ENVELOPE_SEND, method = RequestMethod.POST)
    @ApiOperation(value = "发群红包", notes = "返回红包ID，已处理IM消息，详见[群红包消息说明]")
    @RequestLimit
    @ApiOperationSupport(order = 6)
    public RestResponse<Long> groupRedEnvelopeSend(HttpServletRequest request, @RequestBody @Valid GroupRedEnvelopeSendPortalParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return groupRedEnvelopeService.sendForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.GROUP_RED_ENVELOPE_RECEIVE, method = RequestMethod.POST)
    @ApiOperation(value = "领群红包", notes = "返回本次领取的金额，已处理IM消息，详见[群红包消息说明]")
    @Lock4j(keys = "#param.id")
    @ApiOperationSupport(order = 7)
    public RestResponse<BigDecimal> groupRedEnvelopeReceive(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        return groupRedEnvelopeService.receiveForPortal(sessionUser, param);
    }

    @RequestMapping(value = ApiUrl.GROUP_RED_ENVELOPE_GET, method = RequestMethod.POST)
    @ApiOperation(value = "获取群红包")
    @ApiOperationSupport(order = 8)
    public RestResponse<GroupRedEnvelopePortalVO> groupRedEnvelopeGet(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        GroupRedEnvelopePortalVO vo = groupRedEnvelopeService.getForPortal(sessionUser, param);
        return ok(vo);
    }

    @RequestMapping(value = ApiUrl.GROUP_RED_ENVELOPE_RECEIVE_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "群红包领取记录", notes = "已按照领取时间进行排序")
    @ApiOperationSupport(order = 9)
    public RestResponse<List<GroupRedEnvelopeReceivePortalVO>> groupRedEnvelopeReceiveList(HttpServletRequest request, @RequestBody @Valid IdParam param) {
        PortalSessionUser sessionUser = getSessionUser(request);
        List<GroupRedEnvelopeReceivePortalVO> list = groupRedEnvelopeService.listReceivedByEnvelopeIdForPortal(sessionUser, param);
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method13", method = RequestMethod.POST)
    @ApiOperation(value = "群红包消息说明", notes = "该接口不可调用，仅说明每个字段的含义<br/>" +
            "<span style='color: red;font-family:bold;font-size: 20px;'>发群红包/领群红包/红包过期，只要红包状态发生变化，服务器都会推送消息过来</span><br/>")
    @ApiOperationSupport(order = 10)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public GroupRedEnvelopePortalVO groupRedEnvelopeImVO() {
        return null;
    }
}
