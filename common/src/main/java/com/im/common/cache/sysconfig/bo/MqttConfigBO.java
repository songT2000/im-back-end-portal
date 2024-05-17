package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT配置
 *
 * @author Barry
 * @date 2021-03-01
 */
@Data
@NoArgsConstructor
public class MqttConfigBO extends BaseSysConfigBO {
    /**
     * 当前系统启用的MQTT类型
     **/
    private MqttTypeEnum mqttType;

    /**
     * 阿里云全权限accessKeyId，在阿里云控制台获取，所有平台共用，该权限是给应用服务器用，给AliyunMQFullAccess权限
     */
    private String aliyunFullAccessAccessKeyId;

    /**
     * 阿里云全权限accessKeySecret，在阿里云控制台获取，所有平台共用，该权限是给应用服务器用，给AliyunMQFullAccess权限
     **/
    private String aliyunFullAccessAccessKeySecret;

    /**
     * 阿里云只读accessKeyId，在阿里云控制台获取，所有平台共用，该权限是给客户端用，给AliyunMQReadOnlyAccess权限
     */
    private String aliyunReadOnlyAccessKeyId;

    /**
     * 阿里云只读accessKeySecret，在阿里云控制台获取，所有平台共用，该权限是给客户端用，给AliyunMQReadOnlyAccess权限
     **/
    private String aliyunReadOnlyAccessKeySecret;

    /**
     * 阿里云实例ID，创建实例后在实例详情复制[实例 ID]，根据平台数量和实例连接上限配置情况决定要创建多少个实例，可以多个平台可以共用同一组实例，但要注意阿里云子订阅主题必须不一样
     */
    private String aliyunInstanceId;

    /**
     * 阿里云GroupId，创建实例后在控制台创建一个groupId，同一实例共用
     */
    private String aliyunGroupId;

    /**
     * 阿里云接入点公网域名，创建实例后在实例详情复制[公网接入点]，同一实例共用
     */
    private String aliyunInternetHost;

    /**
     * 阿里云接入点VPC域名，创建实例后在实例详情复制[VPC 接入点]，应用服务器和MQTT服务器如果在同一VPC，则这里可以设置VPC的值，否则不要设置然后留空，同一实例共用
     */
    private String aliyunVpcHost;

    /**
     * 阿里云父订阅主题，创建实例后在控制台创建一个topic，同一实例共用
     */
    private String aliyunParentTopic;

    /**
     * 阿里云子订阅主题，每个平台一个子题（必须唯一，任意字符串，不能为空），这是虚拟的值，不需要在阿里云创建
     */
    private String aliyunChildTopic;
}
