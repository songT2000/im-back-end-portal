package com.im.portal.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.im.common.cache.impl.SysNoticeCache;
import com.im.common.param.PortalSysNoticeListParam;
import com.im.common.param.PortalSysNoticePageParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.vo.SysNoticePortalVO;
import com.im.portal.controller.url.ApiUrl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 系统公告相关接口
 *
 * @author max.stark
 */
@RestController
@Api(tags = "系统公告相关接口")
@ApiSupport(order = 17)
public class SysNoticeController extends BaseController {
    private SysNoticeCache sysNoticeCache;

    /**
     * 系统公告分页，默认按照排序索引aec，创建日期desc排序
     * 返回完整信息
     */
    @RequestMapping(value = ApiUrl.SYS_NOTICE_PAGE, method = RequestMethod.POST)
    @ApiOperation(value = "分页", notes = "默认按照置顶desc，排序索引asc排序，返回完整信息")
    public RestResponse<PageVO<SysNoticePortalVO>> sysNoticePage(@RequestBody @Valid PortalSysNoticePageParam param) {
        // 只展示 显示的公告
        List<SysNoticePortalVO> sysNoticePortalVOS = sysNoticeCache.pageFromRedis(param.getLanguageCode(), param.getCurrent(), param.getSize());
        PageVO<SysNoticePortalVO> pageVO = (PageVO<SysNoticePortalVO>) sysNoticePortalVOS;
        return ok(pageVO);
    }

    /**
     * 查询公告列表
     *
     * @return 公告列表
     */
    @RequestMapping(value = ApiUrl.SYS_NOTICE_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "列表", notes = "默认按照置顶desc，排序索引asc排序，返回完整信息")
    public RestResponse<List<SysNoticePortalVO>> sysNoticeList(@RequestBody @Valid PortalSysNoticeListParam param) {
        List<SysNoticePortalVO> list = sysNoticeCache.listFromRedis(param.getLanguageCode());
        return ok(list);
    }

    @Autowired
    public void setSysNoticeCache(SysNoticeCache sysNoticeCache) {
        this.sysNoticeCache = sysNoticeCache;
    }
}
