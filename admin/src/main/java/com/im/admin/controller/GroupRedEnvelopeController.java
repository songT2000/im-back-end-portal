package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.TimGroupCache;
import com.im.common.param.GroupRedEnvelopePageAdminParam;
import com.im.common.param.GroupRedEnvelopeReceivePageAdminParam;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.service.GroupRedEnvelopeService;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.GroupRedEnvelopeAdminVO;
import com.im.common.vo.GroupRedEnvelopeReceiveAdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 群红包Controller
 *
 * @author Barry
 * @date 2021-06-24
 */
@RestController
@Api(tags = "群红包相关接口")
public class GroupRedEnvelopeController extends BaseController {
    private GroupRedEnvelopeService groupRedEnvelopeService;
    private TimGroupCache timGroupCache;

    @Autowired
    public void setGroupRedEnvelopeService(GroupRedEnvelopeService groupRedEnvelopeService) {
        this.groupRedEnvelopeService = groupRedEnvelopeService;
    }

    @Autowired
    public void setTimGroupCache(TimGroupCache timGroupCache) {
        this.timGroupCache = timGroupCache;
    }

    @RequestMapping(value = ApiUrl.GROUP_RED_ENVELOPE_PAGE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("分页")
    public RestResponse<PageVO<GroupRedEnvelopeAdminVO>> groupRedEnvelopePage(@RequestBody @Valid GroupRedEnvelopePageAdminParam param) {
        PageVO<GroupRedEnvelopeAdminVO> pageVO = groupRedEnvelopeService.pageVO(param, e -> new GroupRedEnvelopeAdminVO(e, timGroupCache));
        return ok(pageVO);
    }

    @RequestMapping(value = ApiUrl.GROUP_RED_ENVELOPE_RECEIVE_PAGE, method = RequestMethod.POST)
    @CheckPermission(url = ApiUrl.GROUP_RED_ENVELOPE_PAGE)
    @ApiOperation("领取记录")
    public RestResponse<PageVO<GroupRedEnvelopeReceiveAdminVO>> groupRedEnvelopeReceivePage(@RequestBody @Valid GroupRedEnvelopeReceivePageAdminParam param) {
        PageVO<GroupRedEnvelopeReceiveAdminVO> pageVO = groupRedEnvelopeService.pageReceivedByEnvelopeIdForAdmin(param);
        return ok(pageVO);
    }
}
