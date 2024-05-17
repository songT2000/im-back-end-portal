package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.cache.impl.PortalNavigatorCache;
import com.im.common.cache.impl.PortalUserCache;
import com.im.common.entity.PortalNavigator;
import com.im.common.entity.PortalNavigatorClick;
import com.im.common.entity.PortalNavigatorStatistic;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalNavigatorClickService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.LocalDateTimeUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.PortalNavigatorClickVO;
import com.im.common.vo.PortalNavigatorStatisticVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 前台导航统计
 * 统计按天能查询每个链接点击人数和哪些用户
 */
@RestController
@Api(tags = "前台导航统计相关接口")
public class PortalNavigatorStatisticController extends BaseController {
    private PortalNavigatorClickService portalNavigatorClickService;
    private PortalNavigatorCache portalNavigatorCache;
    private PortalUserCache portalUserCache;

    @Autowired
    public void setPortalNavigatorClickService(PortalNavigatorClickService portalNavigatorClickService) {
        this.portalNavigatorClickService = portalNavigatorClickService;
    }
    @Autowired
    public void setPortalNavigatorCache(PortalNavigatorCache portalNavigatorCache) {
        this.portalNavigatorCache = portalNavigatorCache;
    }
    @Autowired
    public void setPortalUserCache(PortalUserCache portalUserCache) {
        this.portalUserCache = portalUserCache;
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_STATISTIC_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("查询统计")
    public RestResponse<List<PortalNavigatorStatisticVO>> portalNavigatorStatistic(@RequestBody @Valid PortalNavigatorStatisticParam param) {
        param.setEndDate(LocalDateTimeUtil.offset(param.getEndDate().atStartOfDay(),1, ChronoUnit.DAYS).toLocalDate());
        List<PortalNavigatorStatistic> statistic = portalNavigatorClickService.queryStatistic(param);
        List<PortalNavigatorStatisticVO> result = new ArrayList<>();
        List<PortalNavigator> navigators = portalNavigatorCache.listFromRedis();
        if(CollectionUtil.isNotEmpty(statistic)){
            for (PortalNavigatorStatistic portalNavigatorStatistic : statistic) {
                Optional<PortalNavigator> any = navigators.stream().filter(p -> p.getId().equals(portalNavigatorStatistic.getPortalNavigatorId())).findAny();
                if(any.isPresent()){
                    PortalNavigatorStatisticVO vo = new PortalNavigatorStatisticVO(any.get());
                    vo.setClickCount(portalNavigatorStatistic.getClickCount());
                    result.add(vo);
                }
            }
        }
        return ok(result);
    }

    @RequestMapping(value = ApiUrl.PORTAL_NAVIGATOR_STATISTIC_DETAIL, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("查询统计详情")
    public RestResponse<PageVO<PortalNavigatorClickVO>> portalNavigatorStatisticDetail(@RequestBody @Valid PortalNavigatorStatisticParam param) {
        param.setEndDate(LocalDateTimeUtil.offset(param.getEndDate().atStartOfDay(),1, ChronoUnit.DAYS).toLocalDate());
        if(StrUtil.isNotBlank(param.getUsername())){
            param.setUserId(portalUserCache.getIdByUsernameFromLocal(param.getUsername()));
        }
        PageVO<PortalNavigatorClickVO> list = portalNavigatorClickService.queryDetail(param);
        return ok(list);
    }
}
