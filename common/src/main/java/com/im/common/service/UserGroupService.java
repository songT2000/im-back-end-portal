package com.im.common.service;

import com.im.common.entity.UserGroup;
import com.im.common.param.IdParam;
import com.im.common.param.UserGroupAddAdminParam;
import com.im.common.param.UserGroupEditAdminParam;
import com.im.common.param.UserGroupPageAdminParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.UserGroupAdminVO;

/**
 * 用户组 服务类
 *
 * @author Barry
 * @date 2021-04-11
 */
public interface UserGroupService extends MyBatisPlusService<UserGroup> {
    /**
     * 后台分页
     *
     * @param param
     * @return
     */
    PageVO<UserGroupAdminVO> pageVOForAdmin(UserGroupPageAdminParam param);

    /**
     * 新增
     *
     * @param param
     * @return
     */
    RestResponse addForAdmin(UserGroupAddAdminParam param);

    /**
     * 编辑
     *
     * @param param
     * @return
     */
    RestResponse editForAdmin(UserGroupEditAdminParam param);

    /**
     * 删除
     *
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
