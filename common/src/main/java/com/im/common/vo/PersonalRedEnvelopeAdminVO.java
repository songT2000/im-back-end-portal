package com.im.common.vo;

import com.im.common.entity.PersonalRedEnvelope;
import com.im.common.entity.enums.PersonalRedEnvelopeStatusEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 个人红包
 *
 * @author Barry
 */
@Data
@NoArgsConstructor
@ApiModel
public class PersonalRedEnvelopeAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PersonalRedEnvelope.class, PersonalRedEnvelopeAdminVO.class, false);

    public PersonalRedEnvelopeAdminVO(PersonalRedEnvelope e) {
        BEAN_COPIER.copy(e, this, null);
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
        this.receiveUsername = UserUtil.getUsernameByIdFromLocal(e.getReceiveUserId(), PortalTypeEnum.PORTAL);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    private String orderNum;

    @ApiModelProperty(value = "用户名", position = 3)
    private String username;

    @ApiModelProperty(value = "领取用户名", position = 4)
    private String receiveUsername;

    @ApiModelProperty(value = "金额", position = 5)
    private BigDecimal amount;

    @ApiModelProperty(value = "状态", position = 6)
    private PersonalRedEnvelopeStatusEnum status;

    @ApiModelProperty(value = "创建时间", position = 7)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "领取时间", position = 8)
    private LocalDateTime receiveTime;

    @ApiModelProperty(value = "过期时间", position = 9)
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "备注，用来显示在红包上的文字", position = 10)
    private String remark;
}
