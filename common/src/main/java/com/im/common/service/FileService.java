package com.im.common.service;

import com.im.common.response.RestResponse;
import com.im.common.util.redis.RedisSessionUser;

import java.io.InputStream;

/**
 * 文件
 *
 * @author Barry
 * @date 2020-11-09
 */
public interface FileService {
    /**
     * 上传凭证
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     * @return
     */
    RestResponse<String> uploadVoucher(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);

    /**
     * 上传ID
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     * @return
     */
    RestResponse<String> uploadId(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);

    /**
     * 上传图片
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     * @return
     */
    RestResponse<String> uploadImage(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);


    /**
     * 上传头像
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     * @return
     */
    RestResponse<String> uploadAvatar(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);


    /**
     * 上传视频
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     */
    RestResponse<String> uploadVideo(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);


    /**
     * 上传语音文件
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     */
    RestResponse<String> uploadSound(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);


    /**
     * 上传文件
     *
     * @param sessionUser      可以为空，这个是用来给文件名重命名加的用户名前缀，如果没传，则不会加前缀
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     */
    RestResponse<String> uploadFile(RedisSessionUser sessionUser, String originalFileName, InputStream inputStream);

    /**
     * 上传json
     *
     * @param originalFileName 原始文件名，服务器要根据文件名后缀来改名
     * @param inputStream      文件流
     * @return
     */
    RestResponse<String> uploadJson(String originalFileName, InputStream inputStream);
}
