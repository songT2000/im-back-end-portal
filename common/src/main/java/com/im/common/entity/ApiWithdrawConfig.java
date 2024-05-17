package com.im.common.entity;

import com.im.common.entity.base.BaseDeletableEnableEntity;
import com.im.common.entity.enums.ApiWithdrawConfigCodeEnum;
import com.im.common.entity.enums.WithdrawConfigSourceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * API代付配置
 *
 * @author Barry
 * @date 2021-09-29
 */
@Data
@NoArgsConstructor
public class ApiWithdrawConfig extends BaseDeletableEnableEntity implements Serializable {
    private static final long serialVersionUID = 1423923279916079192L;

    /**
     * 名称
     **/
    private String name;

    /**
     * 编码，每种编码都有不同的处理类
     **/
    private ApiWithdrawConfigCodeEnum code;

    /**
     * 适用提现方式，只适应于同类型的提单配置来源，比如用户是银行卡提现配置来的，那么只能用银行卡提现的API代付
     **/
    private WithdrawConfigSourceEnum withdrawConfigSource;

    /**
     * 三方配置，密钥，URL什么的，JSON格式
     */
    private String thirdConfig;

    /**
     * 三方回调IP白名单，多个用英文逗号分隔，支持IPV4/IPV4掩码/IPV6，为空不生效
     */
    private String thirdCallbackWhitelistIp;

    /**
     * 排序号，越小排越前面
     */
    private Integer sort;
}
