package com.im.portal.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.base.bo.SysCacheRefreshBO;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.vo.*;
import com.im.common.constant.RedisKeyEnum;
import com.im.common.entity.SysCacheRefresh;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.response.RestResponse;
import com.im.common.service.SysCacheRefreshService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.EnumUtil;
import com.im.common.util.aop.limit.RequestLimit;
import com.im.common.util.aop.limit.RequestLimitTypeEnum;
import com.im.common.vo.SysCacheRefreshVO;
import com.im.common.vo.SysConfigAndDict;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统配置Controller
 *
 * @author Barry
 * @date 2029-05-25
 */
@RestController
@Api(tags = "系统配置相关接口")
@ApiSupport(order = 1)
public class SysConfigController extends BaseController {
    private SysConfigCache sysConfigCache;
    private HashOperations<String, String, String> redisHash;
    private SysCacheRefreshService sysCacheRefreshService;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setRedisHash(HashOperations<String, String, String> redisHash) {
        this.redisHash = redisHash;
    }

    @Autowired
    public void setSysCacheRefreshService(SysCacheRefreshService sysCacheRefreshService) {
        this.sysCacheRefreshService = sysCacheRefreshService;
    }

    @RequestMapping(value = ApiUrl.SYS_CONFIG_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "获取系统配置和数据字典", notes = "各配置项详见本分组各项说明")
    @ApiOperationSupport(order = 1)
    @RequestLimit(type = RequestLimitTypeEnum.IP, second = 60, count = 500)
    public RestResponse<SysConfigAndDict> sysConfigSimpleList() {
        List<SysConfigVO> list = new ArrayList<>();

        // 全局配置
        {
            GlobalConfigCommonVO globalConfig = new GlobalConfigCommonVO(sysConfigCache.getGlobalConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.GLOBAL, globalConfig));
        }

        // 前台配置
        {
            PortalConfigPortalVO portalConfig = new PortalConfigPortalVO(sysConfigCache.getPortalConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.PORTAL, portalConfig));
        }

        // 注册配置
        {
            RegisterConfigPortalVO registerConfig = new RegisterConfigPortalVO(sysConfigCache.getRegisterConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.REGISTER, registerConfig));
        }

        // IM配置
        {
            ImConfigPortalVO imConfig = new ImConfigPortalVO(sysConfigCache.getImConfigFromLocal());
            list.add(new SysConfigVO(SysConfigGroupEnum.IM, imConfig));
        }

        // 红包配置
        {
            RedEnvelopeConfigPortalVO redEnvelopeConfig = new RedEnvelopeConfigPortalVO(sysConfigCache.getRedEnvelopeConfigFromRedis());
            list.add(new SysConfigVO(SysConfigGroupEnum.RED_ENVELOPE, redEnvelopeConfig));
        }

        return ok(new SysConfigAndDict(list, EnumUtil.getCurrentLanguageEnumMapForPortal()));
    }

    @RequestMapping(value = ApiUrl.SYS_CACHE_REFRESH_LIST, method = RequestMethod.POST)
    @ApiOperation("缓存时间戳")
    @ApiOperationSupport(order = 2)
    @RequestLimit(type = RequestLimitTypeEnum.IP, second = 60, count = 500)
    public RestResponse<List<SysCacheRefreshVO>> sysCacheRefresh() {
        Map<String, String> entries = redisHash.entries(RedisKeyEnum.SYS_CACHE_REFRESH.getVal());
        if (CollectionUtil.isEmpty(entries)) {
            // 如果没查到，可能是redis那边因为增删真空时间问题没有数据了，那么往数据库查一下先，避免前台因为刷不到把所有接口全部刷新，造成瞬间巨大压力
            List<SysCacheRefresh> list = sysCacheRefreshService.list();
            return ok(CollectionUtil.toList(list, SysCacheRefreshVO::new));
        }

        // 从redis来的
        List<SysCacheRefreshVO> list = entries
                .entrySet()
                .stream()
                .filter(entry -> SysCacheRefreshTypeEnum.valueOf(entry.getKey()) != null)
                .map(e -> new SysCacheRefreshVO(e.getKey(), JSONObject.parseObject(e.getValue(), SysCacheRefreshBO.class)))
                .collect(Collectors.toList());
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method4", method = RequestMethod.POST)
    @ApiOperation(value = "字段[GLOBAL]全局配置", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 5)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public GlobalConfigCommonVO globalConfigCommonVo() {
        return null;
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method6", method = RequestMethod.POST)
    @ApiOperation(value = "字段[PORTAL]前台配置", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 6)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public PortalConfigPortalVO portalConfigPortalVO() {
        return null;
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method7", method = RequestMethod.POST)
    @ApiOperation(value = "字段[REGISTER]注册配置", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 7)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public RegisterConfigPortalVO registerConfigPortalVO() {
        return null;
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method8", method = RequestMethod.POST)
    @ApiOperation(value = "字段[IM]IM配置", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 8)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public ImConfigPortalVO imConfigPortalVO() {
        return null;
    }

    @RequestMapping(value = ApiUrl.BASE_AUTH_URL + "/do_not_call_this_method9", method = RequestMethod.POST)
    @ApiOperation(value = "字段[RED_ENVELOPE]红包配置", notes = "该接口不可调用，仅说明每个字段的含义")
    @ApiOperationSupport(order = 9)
    @ConditionalOnProperty(name = "enable", prefix = "knife4j", havingValue = "true")
    public RedEnvelopeConfigPortalVO redEnvelopeConfigPortalVO() {
        return null;
    }
}
