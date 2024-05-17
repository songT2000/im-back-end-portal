package com.im.common.cache.sysconfig.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OSS配置
 *
 * @author Barry
 * @date 2018/6/8
 */
@Data
@NoArgsConstructor
public class OssConfigBO extends BaseSysConfigBO {
    /**
     * 当前系统启用的OSS类型
     **/
    private OssTypeEnum ossType;

    /**
     * 阿里云Endpoint，在阿里云控制台获取
     **/
    private String aliyunEndpoint;

    /**
     * 阿里云accessKeyId，在阿里云控制台获取
     * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
     */
    private String aliyunAccessKeyId;

    /**
     * 阿里云accessKeySecret，在阿里云控制台获取
     **/
    private String aliyunAccessKeySecret;

    /**
     * 阿里云bucketName，在阿里云控制台创建
     */
    private String aliyunBucketName;

    /**
     * 公网endpoint
     */
    private String minioEndpoint = "";
    /**
     * 内网endpoint，用于上传
     */
    private String minioInternalEndpoint="";
    /**
     * accessKey类似于用户ID，用于唯一标识你的账户
     */
    private String minioAccessKey = "";
    /**
     * secretKey是你账户的密码
     */
    private String minioSecretKey = "";
    /**
     * 默认的存储桶名称，默认值：default
     */
    private String minioBucketName = "default";

    /**
     * 凭证在OSS的文件夹位置
     */
    private String voucherFolder;

    /**
     * ID在OSS的文件夹位置
     */
    private String idFolder;

    /**
     * banner图所在文件夹位置
     */
    private String imageFolder;

    /**
     * avatar图所在文件夹位置
     */
    private String avatarFolder;


    /**
     * 视频文件所在文件夹位置
     */
    private String videoFolder;


    /**
     * 语音文件所在文件夹位置
     */
    private String soundFolder;


    /**
     * file所在文件夹位置
     */
    private String fileFolder;

    /**
     * 系统测速文件所在文件夹位置
     */
    private String circuitFolder;
}
