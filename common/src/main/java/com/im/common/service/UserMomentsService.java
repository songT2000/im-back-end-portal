package com.im.common.service;

import com.im.common.entity.UserMoments;
import com.im.common.param.*;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PortalSessionUser;
import com.im.common.vo.UserMomentsAdminVO;
import com.im.common.vo.UserMomentsTrendsVO;
import com.im.common.vo.UserMomentsVO;


/**
 * 朋友圈信息 接口
 *
 * @author max.stark
 */
public interface UserMomentsService extends MyBatisPlusService<UserMoments> {


    /**
     * 动态提醒
     * @param sessionUser
     * @return
     */
    RestResponse<UserMomentsTrendsVO> trendsForPortal(PortalSessionUser sessionUser);

    /**
     * 朋友圈分页 前台
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<PageVO<UserMomentsVO>> pageForPortal(PortalSessionUser sessionUser, UserMomentsPortalParam param);

    /**
     * 朋友圈分页 后台
     * @param param
     * @return
     */
    RestResponse<PageVO<UserMomentsAdminVO>> pageForAdmin(UserMomentsPageAdminParam param);

    /**
     * 某个用户的朋友圈
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<PageVO<UserMomentsVO>> pageByUserForPortal(PortalSessionUser sessionUser, UserMomentsByUserPortalParam param);

    /**
     * 查看某条朋友圈
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<UserMomentsVO> detailForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 新增
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse addForPortal(PortalSessionUser sessionUser, UserMomentsAddParam param);

    /**
     * 删除
     *
     * @param sessionUser
     * @param param
     * @return 返回OK表示成功
     */
    RestResponse deleteForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 删除朋友圈
     * @param param
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);
}
