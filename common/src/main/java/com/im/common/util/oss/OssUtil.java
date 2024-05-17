package com.im.common.util.oss;

import com.im.common.cache.sysconfig.bo.OssConfigBO;
import com.im.common.cache.sysconfig.bo.OssTypeEnum;

import java.io.InputStream;

/**
 * OSS操作工具类
 *
 * @author Barry
 * @date 2020-08-07
 */
public class OssUtil {

    public static String upload(OssConfigBO ossConfig, InputStream inputStream, String ossFilePath) {
        BaseOss client = getClient(ossConfig);
        return client.upload(ossConfig, inputStream, ossFilePath);
    }

    private static BaseOss getClient(OssConfigBO ossConfig) {
        if (ossConfig.getOssType() == OssTypeEnum.ALIYUN) {
            return new AliyunOss();
        }
        if (ossConfig.getOssType() == OssTypeEnum.MINIO) {
            return new MinioOss();
        }
        return null;
    }

    public static String uploadByAliyun(OssConfigBO ossConfig, InputStream inputStream, String ossFilePath) {
        BaseOss client = getClientByAliyun();
        return client.upload(ossConfig, inputStream, ossFilePath);
    }

    private static BaseOss getClientByAliyun() {
        return new AliyunOss();
    }
}
