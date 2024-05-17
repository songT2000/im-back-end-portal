package com.im.common.util.oss;

import com.im.common.cache.sysconfig.bo.OssConfigBO;

import java.io.InputStream;

/**
 * 抽象OSS能力
 *
 * @author Barry
 * @date 2020-08-07
 */
public interface BaseOss {
    /**
     * 上传文件到OSS
     *
     * @param ossConfig   OSS配置
     * @param inputStream 文件流
     * @param ossFilePath 文件在OSS上存储的位置
     * @return 返回文件在OSS上的位置，必须返回文件绝对地址，不能有权限，能直接打开浏览器读的那种
     */
    String upload(OssConfigBO ossConfig, InputStream inputStream, String ossFilePath);
}
