package com.im.common.vo;

import com.im.common.cache.impl.TimGroupCache;
import com.im.common.entity.GroupRedEnvelope;
import com.im.common.entity.enums.GroupRedEnvelopeStatusEnum;
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
 * 群红包
 *
 * @author Barry
 */
@Data
@NoArgsConstructor
@ApiModel
public class GroupRedEnvelopeAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(GroupRedEnvelope.class, GroupRedEnvelopeAdminVO.class, false);

    public GroupRedEnvelopeAdminVO(GroupRedEnvelope e, TimGroupCache timGroupCache) {
        BEAN_COPIER.copy(e, this, null);
        this.groupName = timGroupCache.getNameBySysIdFromLocal(e.getGroupId());
        this.username = UserUtil.getUsernameByIdFromLocal(e.getUserId(), PortalTypeEnum.PORTAL);
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "订单号", position = 2)
    private String orderNum;

    @ApiModelProperty(value = "群组ID", position = 3)
    private Long groupId;

    @ApiModelProperty(value = "群组名称", position = 4)
    private String groupName;

    @ApiModelProperty(value = "用户名", position = 5)
    private String username;

    @ApiModelProperty(value = "金额", position = 6)
    private BigDecimal amount;

    @ApiModelProperty(value = "已领取金额", position = 7)
    private BigDecimal receivedAmount;

    @ApiModelProperty(value = "拆分个数", position = 8)
    private Integer num;

    @ApiModelProperty(value = "已领取个数", position = 9)
    private Integer receivedNum;

    @ApiModelProperty(value = "状态", position = 10)
    private GroupRedEnvelopeStatusEnum status;

    @ApiModelProperty(value = "创建时间", position = 11)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "过期时间", position = 12)
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "备注，用来显示在红包上的文字", position = 13)
    private String remark;
}
