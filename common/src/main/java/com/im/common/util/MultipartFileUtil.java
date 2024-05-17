package com.im.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MultipartFileUtil {

    /**
     * MultipartFile 转 File
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.getSize() > 0) {
            InputStream ins;
            ins = file.getInputStream();
            //获得文件扩展名
            String extensionName = FileUtil.extName(file.getOriginalFilename()).toLowerCase();
            String tempFileName = RandomUtil.randomString(20) + CharUtil.DOT + extensionName;
            toFile = new File(tempFileName);
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static  boolean isImage(File file){
        MimetypesFileTypeMap mtftp = new MimetypesFileTypeMap();
        mtftp.addMimeTypes("image png tif jpg jpeg bmp");
        String mimetype= mtftp.getContentType(file);
        String type = mimetype.split("/")[0];
        return "image".equals(type);
    }

}
