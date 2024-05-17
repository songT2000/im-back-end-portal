package com.im.admin.controller;

import com.im.admin.controller.url.ApiUrl;
import com.im.common.param.SysDefaultAvatarAddParam;
import com.im.common.response.RestResponse;
import com.im.common.service.SysDefaultAvatarService;
import com.im.common.util.CollectionUtil;
import com.im.common.vo.SysDefaultAvatarSimpleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

/**
 * 系统默认头像Controller
 *
 * @author Barry
 * @date 2022-04-07
 */
@RestController
@Api(tags = "系统默认头像相关接口")
public class SysDefaultAvatarController extends BaseController {
    private SysDefaultAvatarService sysDefaultAvatarService;

    @Autowired
    public void setSysDefaultAvatarService(SysDefaultAvatarService sysDefaultAvatarService) {
        this.sysDefaultAvatarService = sysDefaultAvatarService;
    }

    @RequestMapping(value = ApiUrl.SYS_DEFAULT_AVATAR_SIMPLE_LIST, method = RequestMethod.POST)
    @ApiOperation("列表")
    public RestResponse<List<SysDefaultAvatarSimpleVO>> sysDefaultAvatarSimpleList() {
        List<SysDefaultAvatarSimpleVO> list = sysDefaultAvatarService.listVO(SysDefaultAvatarSimpleVO::new);
        list = CollectionUtil.sort(list, Comparator.comparingLong(SysDefaultAvatarSimpleVO::getId).reversed());
        return ok(list);
    }

    @RequestMapping(value = ApiUrl.SYS_DEFAULT_AVATAR_ADD, method = RequestMethod.POST)
    @ApiOperation("新增")
    public RestResponse sysDefaultAvatarAdd(@RequestBody @Valid SysDefaultAvatarAddParam param) {
        return sysDefaultAvatarService.addForAdmin(param);
    }
}
