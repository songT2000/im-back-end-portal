package com.im.common.vo;

import cn.hutool.core.net.Ipv4Util;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.AdminIpBlackWhite;
import com.im.common.entity.enums.BlackWhiteTypeEnum;
import com.im.common.entity.enums.PortalTypeEnum;
import com.im.common.util.StrUtil;
import com.im.common.util.ip.IpAddressUtil;
import com.im.common.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;

import java.time.LocalDateTime;

/**
 * 后台IP黑白名单
 *
 * @author max.stark
 */
@Data
@NoArgsConstructor
@ApiModel
public class AdminIpBlackWhiteAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(AdminIpBlackWhite.class, AdminIpBlackWhiteAdminVO.class, false);

    public AdminIpBlackWhiteAdminVO(AdminIpBlackWhite e) {
        BEAN_COPIER.copy(e, this, null);
        this.updateAdminUsername = UserUtil.getUsernameByIdFromLocal(e.getUpdateAdminId(), PortalTypeEnum.ADMIN);

        if (IpAddressUtil.isIpv4Mask(e.getIp())) {
            try {
                String[] ipParam = StrUtil.splitToArray(e.getIp(), Ipv4Util.IP_MASK_SPLIT_MARK);
                this.startIp = Ipv4Util.getBeginIpStr(ipParam[0], Integer.valueOf(ipParam[1]));
                this.endIp = Ipv4Util.getEndIpStr(ipParam[0], Integer.valueOf(ipParam[1]));
                this.ipCount = Ipv4Util.countByMaskBit(Integer.valueOf(ipParam[1]), true);
            } catch (Exception exception) {
                this.startIp = null;
                this.endIp = null;
                this.ipCount = CommonConstant.INT_0;
            }
        } else if (IpAddressUtil.isIpv4Range(e.getIp())) {
            try {
                String[] ipParam = StrUtil.splitToArray(e.getIp(), CommonConstant.RANGE_EN);
                this.startIp = ipParam[0];
                this.endIp = ipParam[1];
                this.ipCount = Ipv4Util.countByIpRange(this.startIp, this.endIp);
            } catch (Exception exception) {
                this.startIp = null;
                this.endIp = null;
                this.ipCount = CommonConstant.INT_0;
            }
        } else {
            this.ipCount = CommonConstant.INT_1;
        }
    }

    @ApiModelProperty(value = "ID", position = 1)
    private Long id;

    @ApiModelProperty(value = "IP，全文匹配，支持IPV4/IPV4掩码/IPV4段/IPV6", position = 2)
    private String ip;

    @ApiModelProperty(value = "黑白名单", position = 3)
    private BlackWhiteTypeEnum blackWhite;

    @ApiModelProperty(value = "开始IP", position = 4)
    private String startIp;

    @ApiModelProperty(value = "结束IP", position = 5)
    private String endIp;

    @ApiModelProperty(value = "IP个数", position = 6)
    private Integer ipCount;

    @ApiModelProperty(value = "备注", position = 7)
    private String remark;

    @ApiModelProperty(value = "修改人", position = 8)
    private String updateAdminUsername;

    @ApiModelProperty(value = "修改时间", position = 9)
    private LocalDateTime updateTime;
}
