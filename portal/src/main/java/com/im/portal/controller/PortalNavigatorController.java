package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.PortalNavigatorCache;
import com.im.common.entity.PortalNavigator;
import com.im.common.entity.PortalNavigatorClick;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalNavigatorClickService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.StrUtil;
import com.im.common.vo.PortalNavigatorPortalVO;
import com.im.common.vo.PortalSessionUser;
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
import java.util.Comparator;
import java.util.List;

/**
 * 前台导航Controller
 *
 * @author Barry
 * @date 2022-03-25
 */
@RestController
@Api(tags = "前台导航相关接口")
@ApiSupport(order = 14)
public class PortalNavigatorController extends BaseController {
    private PortalNavigatorCache portalNavigatorCache;
    private PortalNavigatorClickService portalNavigatorClickService;

    @Autowired
    public void setPortalNavigatorCache(PortalNavigatorCache portalNavigatorCache) {
        this.portalNavigatorCache = portalNavigatorCache;
    }

    @Autowired
    public void setPortalNavigatorClickService(PortalNavigatorClickService portalNavigatorClickService) {
        this.portalNavigatorClickService = portalNavigatorClickService;
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "列表[支持缓存]", notes = "缓存Key=PORTAL_NAVIGATOR")
    @ApiOperationSupport(order = 1)
    public RestResponse<List<PortalNavigatorPortalVO>> portalNavigatorList() {
        List<PortalNavigator> list = portalNavigatorCache.listFromRedis();
        list = CollectionUtil.filterList(list, e -> Boolean.TRUE.equals(e.getEnabled()));
        list = CollectionUtil.sort(list, Comparator.comparingInt(PortalNavigator::getSort));

        List<PortalNavigatorPortalVO> voList = CollectionUtil.toList(list, PortalNavigatorPortalVO::new);
        return ok(voList);
    }

    /**
     * 有些客户只想发现有一条链接，取第一条
     */
    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_CLUB, method = RequestMethod.POST)
    @ApiOperation(value = "单条[某些客户需要]", notes = "缓存Key=PORTAL_NAVIGATOR")
    @ApiOperationSupport(order = 2)
    public RestResponse<String> portalNavigatorOne() {
        List<PortalNavigator> list = portalNavigatorCache.listFromRedis();
        list = CollectionUtil.filterList(list, e -> Boolean.TRUE.equals(e.getEnabled()));
        list = CollectionUtil.sort(list, Comparator.comparingInt(PortalNavigator::getSort));
        if (CollectionUtil.isNotEmpty(list)) {
            return ok(list.get(0).getUrl());
        }
        return ok(StrUtil.EMPTY);
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_CLICK, method = RequestMethod.POST)
    @ApiOperation(value = "收集导航点击", notes = "收集用户点击导航数据")
    @ApiOperationSupport(order = 3)
    public RestResponse portalNavigatorClick(@RequestBody @Valid IdParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);

        PortalNavigatorClick item = new PortalNavigatorClick();
        item.setUserId(portalUser.getId());
        item.setPortalNavigatorId(param.getId());

        portalNavigatorClickService.save(item);
        return ok();
    }
}
