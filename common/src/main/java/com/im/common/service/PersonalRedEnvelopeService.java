package com.im.common.service;

import com.im.common.entity.PersonalRedEnvelope;
import com.im.common.param.IdParam;
import com.im.common.param.PersonalRedEnvelopeSendPortalParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import com.im.common.vo.PersonalRedEnvelopePortalVO;
import com.im.common.vo.PortalSessionUser;

/**
 * 个人红包 服务类
 *
 * @author Barry
 * @date 2021-12-20
 */
public interface PersonalRedEnvelopeService extends MyBatisPlusService<PersonalRedEnvelope> {
    /**
     * 获取红包
     *
     * @param sessionUser
     * @param param
     * @return
     */
    PersonalRedEnvelopePortalVO getForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 发送红包，返回红包ID
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse<Long> sendForPortal(PortalSessionUser sessionUser, PersonalRedEnvelopeSendPortalParam param);

    /**
     * 领取红包
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse receiveForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 退回红包
     *
     * @param sessionUser
     * @param param
     * @return
     */
    RestResponse refundForPortal(PortalSessionUser sessionUser, IdParam param);

    /**
     * 检查过期红包并退回
     */
    void checkExpired();
}
