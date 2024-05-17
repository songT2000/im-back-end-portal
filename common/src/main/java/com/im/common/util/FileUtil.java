package com.im.common.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.im.common.constant.CommonConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.*;

/**
 * 文件操作工具类
 *
 * @author Barry
 * @date 10/10/19
 */
public final class FileUtil extends cn.hutool.core.io.FileUtil {
    private static final Log LOG = LogFactory.get();

    /**
     * Bytes per Kilobyte.
     */
    private static long BYTES_PER_KB = 1024;

    /**
     * Bytes per Megabyte.
     */
    private static long BYTES_PER_MB = BYTES_PER_KB * 1024;

    /**
     * Bytes per Gigabyte.
     */
    private static long BYTES_PER_GB = BYTES_PER_MB * 1024;

    /**
     * Bytes per Terabyte.
     */
    private static long BYTES_PER_TB = BYTES_PER_GB * 1024;

    /**
     * 将classpath下的文件拷贝到临时目录去
     */
    public static File copyResourceFileIntoTemp(String fileClasspath, ApplicationContext applicationContext) throws Exception {
        ClassPathResource resource = new ClassPathResource(fileClasspath);

        String fileName = FileUtil.getName(fileClasspath);

        // 转成临时文件
        String tempPath = cn.hutool.core.io.FileUtil.getUserHomePath() + "/" + applicationContext.getId() + "_" + fileName;

        File tempFile = new File(tempPath);
        if (tempFile.exists()) {
            if (tempFile.canWrite()) {
                try {
                    tempFile.delete();
                    // 转存到临时文件
                    IoUtil.copy(resource.getInputStream(), new FileOutputStream(tempFile));
                } catch (IOException e) {
                    LOG.error(e, "文件{}删除失败", tempPath);
                }
            } else {
                LOG.warn("文件{}没有写权限，无法删除，请保障该目录有写权限", tempFile.getPath());
            }
        } else {
            // 转存到临时文件
            IoUtil.copy(resource.getInputStream(), new FileOutputStream(tempFile));
        }

        return tempFile;
    }

    /**
     * 判断文件名是否是以jpg或者png结尾
     *
     * @param fileName 文件名
     * @return
     */
    public static boolean isJpgOrPngSuffix(String fileName) {
        String suffix = getFileSuffix(fileName);
        if (StrUtil.isBlank(suffix)) {
            return false;
        }
        switch (suffix) {
            case ImgUtil.IMAGE_TYPE_JPG:
            case ImgUtil.IMAGE_TYPE_JPEG:
            case ImgUtil.IMAGE_TYPE_PNG:
                return true;
        }
        return false;
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName 文件名
     * @return
     */
    public static String getFileSuffix(String fileName) {
        String suffix = StrUtil.subAfter(fileName, CommonConstant.POINT_EN, true);
        return suffix;
    }

    /**
     * 随机获取一个文件名
     *
     * @param prefix 前缀
     * @return
     */
    public static String randomFileName(String originalFileName, String prefix) {
        String fileName = StrUtil.EMPTY;

        if (StrUtil.isNotBlank(prefix)) {
            fileName = prefix + "_";
        }

        fileName += OrderUtil.orderNumber();

        String suffix = getFileSuffix(originalFileName);

        fileName += CommonConstant.POINT_EN + suffix;

        return fileName;
    }

    /**
     * 把字节转换为可以读懂的文字
     *
     * @param bytes 字节
     * @return
     */
    public static String translateByte(long bytes) {
        if (bytes < BYTES_PER_KB) {
            return bytes + "B";
        } else if (bytes < BYTES_PER_MB) {
            return NumberUtil.divToStrByLong(bytes, BYTES_PER_KB) + "KB";
        } else if (bytes < BYTES_PER_GB) {
            return NumberUtil.divToStrByLong(bytes, BYTES_PER_MB) + "MB";
        } else if (bytes < BYTES_PER_TB) {
            return NumberUtil.divToStrByLong(bytes, BYTES_PER_GB) + "GB";
        } else {
            return NumberUtil.divToStrByLong(bytes, BYTES_PER_TB) + "TB";
        }
    }

    /**
     * inputStream转文件
     *
     * @param ins
     * @param file
     * @throws IOException
     */
    public static void inputStreamToFile(InputStream ins, File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
}
