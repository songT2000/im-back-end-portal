package com.im.common.service;

import com.im.common.entity.SysNotice;
import com.im.common.param.IdGoogleParam;
import com.im.common.param.SysNoticeAddParam;
import com.im.common.param.SysNoticeEditParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.AdminSessionUser;


/**
 * 系统公告 接口
 *
 * @author max.stark
 */
public interface SysNoticeService extends MyBatisPlusService<SysNotice> {
    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse addForAdmin(AdminSessionUser sessionUser, SysNoticeAddParam param);

    /**
     * 编辑
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse editForAdmin(AdminSessionUser sessionUser, SysNoticeEditParam param);

    /**
     * 删除
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse deleteForAdmin(AdminSessionUser sessionUser, IdGoogleParam param);
}
