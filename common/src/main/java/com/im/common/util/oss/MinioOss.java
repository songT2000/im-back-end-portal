package com.im.common.util.oss;

import cn.hutool.core.util.URLUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.cache.sysconfig.bo.OssConfigBO;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.util.FileContentTypeUtil;
import com.im.common.util.FileUtil;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自建minio
 *
 * @author mozzie
 * @date 2020-08-07
 */
public class MinioOss implements BaseOss {
    private static final Log LOG = LogFactory.get();

    @Override
    public String upload(OssConfigBO ossConfig, InputStream inputStream, String ossFilePath) {
        try {
            String contentType = FileContentTypeUtil.getContentType(FileUtil.getFileSuffix(ossFilePath));
            // 上传文件
            getInternalMinioClient(ossConfig).putObject(ossConfig.getMinioBucketName(), ossFilePath, inputStream, contentType);
        } catch (Exception e) {
            LOG.error(e, "minio上传文件出错");
            throw new ImException(ResponseCode.OSS_CONNECT_ERROR, new Object[]{e.getMessage()});
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getFileUrl(ossConfig, ossFilePath);
    }

    private String getFileUrl(OssConfigBO ossConfig, String ossFilePath) {
        String bucketUrl;
        if (ossConfig.getMinioEndpoint().endsWith("/")) {
            bucketUrl = ossConfig.getMinioEndpoint() + ossConfig.getMinioBucketName();
        } else {
            bucketUrl = ossConfig.getMinioEndpoint() + "/" + ossConfig.getMinioBucketName();
        }
        bucketUrl += FileUtil.FILE_SEPARATOR + ossFilePath;
        return URLUtil.normalize(bucketUrl);
    }

    public static void main(String[] args) {
        String url1 = "http://54.199.17.3:9001//";
        String url2 = "im-file";

        String bucketUrl;
        if (url1.endsWith("/")) {
            bucketUrl = url1 + url2;
        } else {
            bucketUrl = url1 + "/" + url2;
        }
        bucketUrl += FileUtil.FILE_SEPARATOR + "id-voucher/123.png";
        System.out.println(URLUtil.normalize(bucketUrl));

    }

    /**
     * 获取内网地址的minioClient，用于上传
     */
    private MinioClient getInternalMinioClient(OssConfigBO configBo) {
        try {
            return new MinioClient(configBo.getMinioInternalEndpoint(), configBo.getMinioAccessKey(), configBo.getMinioSecretKey());
        } catch (InvalidEndpointException | InvalidPortException e) {
            LOG.error("minio init client error", e);
            throw new ImException(ResponseCode.OSS_CONNECT_ERROR, new Object[]{e.getMessage()});
        }
    }
}
