package com.im.common.service;

import com.im.common.entity.GroupRedEnvelope;
import com.im.common.param.GroupRedEnvelopeReceivePageAdminParam;
import com.im.common.param.GroupRedEnvelopeSendPortalParam;
import com.im.common.param.IdParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.page.PageVO;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.GroupRedEnvelopePortalVO;
import com.im.common.vo.GroupRedEnvelopeReceiveAdminVO;
import com.im.common.vo.GroupRedEnvelopeReceivePortalVO;
import com.im.common.vo.PortalSessionUser;

import java.math.BigDecimal;
import java.util.List;

/**
 * 群红包 服务类
 *
 * @author Barry
 * @date 2021-12-20
 */
public interface GroupRedEnvelopeService extends MyBatisPlusService<GroupRedEnvelope> {
    /**
     * 获取红包
     *
     * @param sessionUser
     * @param param
     * @return
     */
    GroupRedEnvelopePortalVO getForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 发送红包，返回红包ID
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<Long> sendForPortal(PortalSessionUser sessionUser, GroupRedEnvelopeSendPortalParam param);

    /**
     * 领取红包，返回领取的金额
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<BigDecimal> receiveForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 为前台用户列出群红包领取记录
     *
     * @param sessionUser
     * @param param
     * @return
     */
    List<GroupRedEnvelopeReceivePortalVO> listReceivedByEnvelopeIdForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 为后台列出群红包领取记录
     *
     * @param param
     * @return
     */
    PageVO<GroupRedEnvelopeReceiveAdminVO> pageReceivedByEnvelopeIdForAdmin(GroupRedEnvelopeReceivePageAdminParam param);

    /**
     * 检查过期红包并退回
     */
    void checkExpired();
}
