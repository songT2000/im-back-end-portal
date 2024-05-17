package com.im.common.util.oss;

import cn.hutool.core.util.URLUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.im.common.cache.sysconfig.bo.OssConfigBO;
import com.im.common.exception.ImException;
import com.im.common.response.ResponseCode;
import com.im.common.util.RequestUtil;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 阿里云OSS，Bucket ACL必须设置为共公读
 *
 * @author Barry
 * @date 2020-08-07
 */
public class AliyunOss implements BaseOss {
    private static final Log LOG = LogFactory.get();

    @Override
    public String upload(OssConfigBO ossConfig, InputStream inputStream, String ossFilePath) {
        OSS ossClient = null;
        try {
            ossClient = buildOssClient(ossConfig);

            // 创建PutObjectRequest对象。<ossFilePath>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getAliyunBucketName(), ossFilePath, inputStream);

            // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件
            ossClient.putObject(putObjectRequest);
        } catch (Exception e) {
            LOG.error(e, "阿里云OSS上传文件出错");
            throw new ImException(ResponseCode.OSS_CONNECT_ERROR, new Object[]{e.getMessage()});
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return getFileUrl(ossConfig, ossFilePath);
    }

    private String getFileUrl(OssConfigBO ossConfig, String ossFilePath) {
        // Bucket ACL是共公读，直接返回拼接的地址
        String bucketUrl = RequestUtil.appendSecondDomain(ossConfig.getAliyunEndpoint(), ossConfig.getAliyunBucketName());
        bucketUrl += "/" + ossFilePath;
        return URLUtil.normalize(bucketUrl);
    }

    private String generatePresignedUrl(OssConfigBO ossConfig, String ossFilePath) {
        OSS ossClient = null;

        try {
            ossClient = buildOssClient(ossConfig);

            // 设置URL过期时间为1小时。
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);

            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(ossConfig.getAliyunBucketName(), ossFilePath, expiration);

            return url == null ? null : url.toString();
        } catch (Exception e) {
            LOG.error(e, "阿里云OSS查看文件地址出错");
            throw new ImException(ResponseCode.OSS_CONNECT_ERROR, new Object[]{e.getMessage()});
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    private OSS buildOssClient(OssConfigBO ossConfig) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossConfig.getAliyunEndpoint();

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ossConfig.getAliyunAccessKeyId();
        String accessKeySecret = ossConfig.getAliyunAccessKeySecret();

        // 创建OSSClient实例。
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
