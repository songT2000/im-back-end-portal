package com.im.admin.controller;

import com.im.admin.config.permission.CheckPermission;
import com.im.admin.controller.url.ApiUrl;
import com.im.common.param.AdminMenuEditParam;
import com.im.common.param.IdSortParam;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.response.RestResponse;
import com.im.common.service.AdminMenuService;
import com.im.common.vo.AdminMenuVO;
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
 * 菜单Controller
 *
 * @author Barry
 * @date 2019-11-20
 */
@RestController
@Api(tags = "后台菜单相关接口")
public class AdminMenuController extends BaseController {
    private AdminMenuService menuService;

    @Autowired
    public void setMenuService(AdminMenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(value = ApiUrl.ADMIN_MENU_LIST, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("列表")
    public RestResponse<List<AdminMenuVO>> adminMenuList() {
        List<AdminMenuVO> adminMenus = menuService.listVO(AdminMenuVO::new);
        return ok(adminMenus);
    }

    @RequestMapping(value = ApiUrl.ADMIN_MENU_EDIT, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("编辑")
    public RestResponse adminMenuEdit(@RequestBody @Valid AdminMenuEditParam param) {
        return menuService.edit(param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_MENU_ENABLE_DISABLE, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("启/禁")
    public RestResponse adminMenuEnableDisable(@RequestBody @Valid IdEnableDisableParam param) {
        return menuService.enableDisable(param);
    }

    @RequestMapping(value = ApiUrl.ADMIN_MENU_EDIT_SORT, method = RequestMethod.POST)
    @CheckPermission
    @ApiOperation("编辑排序")
    public RestResponse adminMenuEditSort(@RequestBody @Valid IdSortParam param) {
        return menuService.editSort(param);
    }
}
