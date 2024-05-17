package com.im.common.service;

import com.im.common.entity.UserMoments;
import com.im.common.entity.UserMomentsCallTrends;
import com.im.common.param.IdParam;
import com.im.common.param.IdReadUnreadParam;
import com.im.common.param.MomentsIdParam;
import com.im.common.param.UserMomentsAddParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PortalSessionUser;

import java.util.List;


/**
 * 朋友圈提醒谁看 接口
 *
 * @author max.stark
 */
public interface UserMomentsCallTrendsService extends MyBatisPlusService<UserMomentsCallTrends> {

    /**
     * 根据用户查询提醒数据
     * @param userId
     * @param read
     * @return
     */
    List<UserMomentsCallTrends> listByUser(Long userId, Boolean read);

    /**
     * 标记为已读
     * @param userId
     * @param param
     * @return
     */
    RestResponse readForPortal(Long userId, MomentsIdParam param);

    /**
     * 删除提醒数据
     * @param param 主键
     * @return
     */
    RestResponse deleteForAdmin(IdParam param);

    /**
     * 编辑已读状态
     * @param param
     * @return
     */
    RestResponse readForAdmin(IdReadUnreadParam param);

}
