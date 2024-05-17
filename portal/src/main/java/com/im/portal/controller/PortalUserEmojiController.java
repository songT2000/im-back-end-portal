package com.im.portal.controller;

import com.im.common.cache.impl.PortalUserEmojiCache;
import com.im.common.cache.impl.TimFaceCache;
import com.im.common.entity.PortalUserEmoji;
import com.im.common.entity.tim.TimFace;
import com.im.common.param.IdParam;
import com.im.common.param.PortalUserEmojiAddParam;
import com.im.common.response.RestResponse;
import com.im.common.service.PortalUserEmojiService;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.PortalUserEmojiVo;
import com.im.common.vo.TimFaceVo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表情包接口
 */
@RestController
@Api(tags = "用户表情包接口")
public class PortalUserEmojiController extends BaseController{

    private PortalUserEmojiCache portalUserEmojiCache;
    private PortalUserEmojiService portalUserEmojiService;

    @Autowired
    public void setPortalUserEmojiCache(PortalUserEmojiCache portalUserEmojiCache) {
        this.portalUserEmojiCache = portalUserEmojiCache;
    }

    @Autowired
    public void setPortalUserEmojiService(PortalUserEmojiService portalUserEmojiService) {
        this.portalUserEmojiService = portalUserEmojiService;
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EMOJI_LIST, method = RequestMethod.POST)
    @ApiOperation(value = "获取我的表情包")
    public RestResponse<List<PortalUserEmojiVo>> getEmojiList(HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        List<PortalUserEmoji> list = portalUserEmojiCache.getPortalUserEmojiList(portalUser.getId());
        if(CollectionUtil.isNotEmpty(list)){
            return RestResponse.ok(list.stream().map(PortalUserEmojiVo::new).collect(Collectors.toList()));
        }
        return RestResponse.ok(new ArrayList<>());
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EMOJI_ADD, method = RequestMethod.POST)
    @ApiOperation(value = "新增我的表情")
    public RestResponse saveEmoji(@Valid @RequestBody PortalUserEmojiAddParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        return portalUserEmojiService.add(new PortalUserEmoji(portalUser.getId(),param.getUrl()));
    }

    @RequestMapping(value = ApiUrl.PORTAL_USER_EMOJI_DEL, method = RequestMethod.POST)
    @ApiOperation(value = "删除我的表情")
    public RestResponse delEmoji(@Valid @RequestBody IdParam param, HttpServletRequest request) {
        PortalSessionUser portalUser = getSessionUser(request);
        PortalUserEmoji emoji = portalUserEmojiService.getById(param.getId());
        if(emoji!=null && emoji.getUserId().equals(portalUser.getId())){
            portalUserEmojiService.delete(param.getId());
        }
        return RestResponse.OK;
    }

}
