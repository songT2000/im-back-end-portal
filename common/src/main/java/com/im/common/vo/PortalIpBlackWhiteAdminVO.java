package com.im.common.vo;

import cn.hutool.core.net.Ipv4Util;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.PortalIpBlackWhite;
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
 * 用户IP黑白名单管理后台VO
 *
 * @author Max
 * @date 2021-02-21
 */
@Data
@NoArgsConstructor
@ApiModel
public class PortalIpBlackWhiteAdminVO {
    public static final BeanCopier BEAN_COPIER = BeanCopier.create(PortalIpBlackWhite.class, PortalIpBlackWhiteAdminVO.class, false);

    public PortalIpBlackWhiteAdminVO(PortalIpBlackWhite e) {
        BEAN_COPIER.copy(e, this, null);
        this.createAdminUsername = UserUtil.getUsernameByIdFromLocal(e.getCreateAdminId(), PortalTypeEnum.ADMIN);
        if (e.getUpdateAdminId() != null) {
            this.updateAdminUsername = UserUtil.getUsernameByIdFromLocal(e.getUpdateAdminId(), PortalTypeEnum.ADMIN);
        } else {
            this.updateTime = null;
        }

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

    @ApiModelProperty(value = "用户名列表，多个用英文逗号分割，没有就是全局", position = 2)
    private String usernames;

    @ApiModelProperty(value = "IP，全文匹配，支持IPV4/IPV4掩码/IPV4段/IPV6", position = 3)
    private String ip;

    @ApiModelProperty(value = "黑白名单", position = 4)
    private BlackWhiteTypeEnum blackWhite;

    @ApiModelProperty(value = "开始IP", position = 5)
    private String startIp;

    @ApiModelProperty(value = "结束IP", position = 6)
    private String endIp;

    @ApiModelProperty(value = "IP个数", position = 7)
    private Integer ipCount;

    @ApiModelProperty(value = "备注", position = 8)
    private String remark;

    @ApiModelProperty(value = "创建人", position = 9)
    private String createAdminUsername;

    @ApiModelProperty(value = "创建时间", position = 10)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人", position = 11)
    private String updateAdminUsername;

    @ApiModelProperty(value = "修改时间", position = 12)
    private LocalDateTime updateTime;
}
