package com.im.common.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.TimMessageBackupFile;
import com.im.common.entity.enums.ChatTypeEnum;
import com.im.common.entity.tim.TimMessageC2c;
import com.im.common.entity.tim.TimMessageGroup;
import com.im.common.mapper.TimMessageBackupFileMapper;
import com.im.common.response.RestResponse;
import com.im.common.service.TimMessageDownloadService;
import com.im.common.util.CollectionUtil;
import com.im.common.util.FileUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.api.im.tencent.entity.param.operation.TiDownloadMsgParam;
import com.im.common.util.api.im.tencent.entity.result.group.TiGroupMessageFileResult;
import com.im.common.util.api.im.tencent.entity.result.message.TiSingleMessageFileResult;
import com.im.common.util.api.im.tencent.entity.result.operation.TiDownloadMsgFile;
import com.im.common.util.api.im.tencent.entity.result.operation.TiDownloadMsgResult;
import com.im.common.util.api.im.tencent.service.rest.TiAppOperationService;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Slf4j
@Service
public class TimMessageDownloadServiceImpl extends MyBatisPlusServiceImpl<TimMessageBackupFileMapper, TimMessageBackupFile>
        implements TimMessageDownloadService {

    private TiAppOperationService tiAppOperationService;
    private static final String TEMP_DIR = "data";      //下载文件临时目录，下载后删除文件
    private static final String END_CHAR = "]}";        //文件内容结束字符

    @Autowired
    public void setTiAppOperationService(TiAppOperationService tiAppOperationService) {
        this.tiAppOperationService = tiAppOperationService;
    }

    /**
     * 未完成，里面是全量的消息记录，后续如果有需要再继续
     * @param time      需要下载的时间，格式yyyyMMddHH
     */
    @Override
    public List<TimMessageC2c> downloadC2cMessage(String time) {

        List<TimMessageC2c> messages = new ArrayList<>();

        List<TimMessageBackupFile> list = download(time, ChatTypeEnum.C2C);
        if(CollectionUtil.isNotEmpty(list)){
            for (TimMessageBackupFile backupFile : list) {
                List<TiSingleMessageFileResult> results = parse(backupFile.getUrl(), TiSingleMessageFileResult.class);

            }
        }

        return null;
    }

    /**
     * 未完成，里面是全量的消息记录，后续如果有需要再继续
     * @param time      需要下载的时间，格式yyyyMMddHH
     */
    @Override
    public List<TimMessageGroup> downloadGroupMessage(String time) {
        List<TimMessageGroup> messages = new ArrayList<>();

        List<TimMessageBackupFile> list = download(time, ChatTypeEnum.Group);
        if(CollectionUtil.isNotEmpty(list)){
            for (TimMessageBackupFile backupFile : list) {
                List<TiGroupMessageFileResult> results = parse(backupFile.getUrl(), TiGroupMessageFileResult.class);

            }
        }

        return null;
    }

    private List<TimMessageBackupFile> download(String time, ChatTypeEnum type){
        RestResponse<TiDownloadMsgResult> downloadResult = tiAppOperationService.download(new TiDownloadMsgParam(type, time));
        List<TiDownloadMsgFile> fileList = downloadResult.getData().getFileList();
        if(CollectionUtil.isNotEmpty(fileList)){
            return fileList.stream().map(p->{
                TimMessageBackupFile item = new TimMessageBackupFile(p);
                item.setBelongTime(time);
                item.setChatType(type);
                return item;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 下载并解析文件中的内容
     * @param url           下载地址
     * @param clazz         解析后的类型class
     * @param <T>           返回结构
     */
    private <T> List<T> parse(String url,Class<T> clazz) {
        //获得文件扩展名
        String extensionName = FileUtil.extName(url).toLowerCase();
        String tempFileName = CommonConstant.getLocalFileDir(TEMP_DIR)+RandomUtil.randomString(20) + CharUtil.DOT + extensionName;
        HttpUtil.downloadFile(url,tempFileName);
        List<T> results=new ArrayList<>();
        InputStream in = null;
        try {
            //使用GZIPInputStream解压GZ文件
            in = new GZIPInputStream(new FileInputStream(tempFileName));
            Scanner sc=new Scanner(in);

            int cnt = -1;
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                System.out.println(line);
                if(END_CHAR.equals(line)){
                    break;
                }
                if(cnt>=0){
                    if(line.endsWith(StrUtil.COMMA)){
                        line = line.substring(0,line.length()-1);
                    }
                    T bean = JSON.parseObject(line, clazz);
                    results.add(bean);
                }
                cnt++;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            IoUtil.close(in);
        }
        //删除临时文件
        FileUtil.del(tempFileName);
        return results;
    }
}
