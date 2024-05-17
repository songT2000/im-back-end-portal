package com.im.common.service.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.cache.sysconfig.bo.OssConfigBO;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.FileService;
import com.im.common.util.FileUtil;
import com.im.common.util.oss.OssUtil;
import com.im.common.util.redis.RedisSessionUser;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Barry
 * @date 2020-11-09
 */
@Service
public class FileServiceImpl implements FileService {
    private SysConfigCache sysConfigCache;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Override
    public RestResponse<String> uploadVoucher(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        if (!FileUtil.isJpgOrPngSuffix(originalFileName)) {
            return RestResponse.failed(ResponseCode.SYS_UPLOAD_FILE_ONLY_ACCEPT_JPG_OR_PNG);
        }
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();

        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getVoucherFolder());
    }

    @Override
    public RestResponse<String> uploadId(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        if (!FileUtil.isJpgOrPngSuffix(originalFileName)) {
            return RestResponse.failed(ResponseCode.SYS_UPLOAD_FILE_ONLY_ACCEPT_JPG_OR_PNG);
        }
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();

        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getIdFolder());
    }

    @Override
    public RestResponse<String> uploadImage(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        if (!FileUtil.isJpgOrPngSuffix(originalFileName)) {
            return RestResponse.failed(ResponseCode.SYS_UPLOAD_FILE_ONLY_ACCEPT_JPG_OR_PNG);
        }
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();

        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getImageFolder());
    }

    @Override
    public RestResponse<String> uploadAvatar(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        if (!FileUtil.isJpgOrPngSuffix(originalFileName)) {
            return RestResponse.failed(ResponseCode.SYS_UPLOAD_FILE_ONLY_ACCEPT_JPG_OR_PNG);
        }
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();

        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getAvatarFolder());
    }

    @Override
    public RestResponse<String> uploadVideo(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();
        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getVideoFolder());
    }

    @Override
    public RestResponse<String> uploadSound(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();
        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getSoundFolder());
    }

    @Override
    public RestResponse<String> uploadFile(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream) {
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();
        return uploadFile(sessionUser, originalFileName, inputStream, ossConfig, ossConfig.getFileFolder());
    }

    /**
     * @param sessionUser      如果为空，如果不为空将会为文件名加用户名前缀
     * @param originalFileName 原文件名
     * @param inputStream      文件流
     * @param folder           放在哪个文件夹
     * @return
     */
    private RestResponse<String> uploadFile(RedisSessionUser sessionUser,
                                            String originalFileName,
                                            InputStream inputStream,
                                            OssConfigBO ossConfig,
                                            String folder) {
        String fileName = FileUtil.randomFileName(originalFileName, sessionUser == null ? null : sessionUser.getUsername());
        if (FileUtil.isJpgOrPngSuffix(originalFileName)) {
            File localFile = null;
            try {
                localFile = thumbnails(inputStream, fileName);
                String ossFilePath = OssUtil.upload(ossConfig, FileUtil.getInputStream(localFile), folder + "/" + fileName);
                return RestResponse.ok(ossFilePath);
            } catch (IOException e) {
                // 压缩失败
                return RestResponse.failed(ResponseCode.SYS_UPLOAD_FILE_ERROR, ExceptionUtil.getSimpleMessage(e));
            } finally {
                if (FileUtil.exist(localFile)) {
                    FileUtil.del(localFile);
                }
            }
        }
        String ossFilePath = OssUtil.upload(ossConfig, inputStream, folder + "/" + fileName);
        return RestResponse.ok(ossFilePath);
    }

    /**
     * 压缩图片，用完记得记得删除file
     *
     * @param inputStream 文件流
     * @param fileName    文件名
     */
    private File thumbnails(InputStream inputStream, String fileName) throws IOException {
        /*
         * size(width,height) 若图片横比720小，高比1080小，不变
         * 若图片横比720小，高比1080大，高缩小到1080，图片比例不变
         * 若图片横比720大，高比1080小，横缩小到720，图片比例不变
         */
        File localFile = FileUtil.newFile(fileName);
        FileUtil.inputStreamToFile(inputStream, localFile);
        BufferedImage bi = ImageIO.read(localFile);
        int height = Math.min(bi.getHeight(), 1080);
        int width = Math.min(bi.getWidth(), 720);
        Thumbnails.of(localFile).size(width, height).toFile(localFile);
        return localFile;
    }

    @Override
    public RestResponse<String> uploadJson(String originalFileName, InputStream inputStream) {
        OssConfigBO ossConfig = sysConfigCache.getOssConfigFromRedis();
        String ossFilePath = OssUtil.uploadByAliyun(ossConfig, inputStream, ossConfig.getCircuitFolder() + originalFileName);
        return RestResponse.ok(ossFilePath);
    }

}
