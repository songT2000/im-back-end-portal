package com.im.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.StringUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.im.common.cache.base.CacheProxy;
import com.im.common.cache.impl.SysConfigCache;
import com.im.common.entity.SysCircuit;
import com.im.common.entity.enums.SysCacheRefreshTypeEnum;
import com.im.common.entity.enums.SysCircuitTypeEnum;
import com.im.common.mapper.SysCircuitMapper;
import com.im.common.param.IdEnableDisableParam;
import com.im.common.param.IdParam;
import com.im.common.param.SysCircuitAddAdminParam;
import com.im.common.param.SysCircuitEditAdminParam;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;
import com.im.common.service.FileService;
import com.im.common.service.SysCircuitService;
import com.im.common.util.NumberUtil;
import com.im.common.util.OrderUtil;
import com.im.common.util.StrUtil;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统测速线路
 *
 * @author max.stark
 */
@Service
public class SysCircuitServiceImpl
        extends MyBatisPlusServiceImpl<SysCircuitMapper, SysCircuit> implements SysCircuitService {
    private CacheProxy cacheProxy;

    private FileService fileService;

    private SysConfigCache sysConfigCache;

    @Autowired
    public void setSysConfigCache(SysConfigCache sysConfigCache) {
        this.sysConfigCache = sysConfigCache;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setCacheProxy(CacheProxy cacheProxy) {
        this.cacheProxy = cacheProxy;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse addForAdmin(SysCircuitAddAdminParam param) {
        {
            // 检查基本参数
            Integer count = lambdaQuery().eq(SysCircuit::getCircuitType, param.getCircuitType())
                    .eq(SysCircuit::getCircuitUrl, param.getCircuitUrl())
                    .count();
            if (NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
            }
        }
        SysCircuit sysCircuit = new SysCircuit();
        sysCircuit.setCircuitType(param.getCircuitType());
        sysCircuit.setCircuitUrl(StrUtil.trim(param.getCircuitUrl()));
        sysCircuit.setRemark(param.getRemark());
        sysCircuit.setEnabled(param.getEnabled());

        // 保存
        boolean save = save(sysCircuit);
        if (!save) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_CIRCUIT);
        this.refreshOssForSysCircuit(null);
        return RestResponse.OK;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse editForAdmin(SysCircuitEditAdminParam param) {
        {
            // 检查修改后是否重复
            Integer count = lambdaQuery().eq(SysCircuit::getCircuitType, param.getCircuitType())
                    .eq(SysCircuit::getCircuitUrl, param.getCircuitUrl())
                    .ne(SysCircuit::getId, param.getId()).count();
            if (NumberUtil.isGreatThenZero(count)) {
                return RestResponse.failed(ResponseCode.SYS_DATA_EXISTED);
            }
        }

        LambdaUpdateChainWrapper<SysCircuit> wrapper = lambdaUpdate()
                .eq(SysCircuit::getId, param.getId())
                .set(SysCircuit::getEnabled, param.getEnabled())
                .set(SysCircuit::getCircuitType, param.getCircuitType())
                .set(StrUtil.isNotBlank(param.getCircuitUrl()), SysCircuit::getCircuitUrl, StrUtil.trim(param.getCircuitUrl()))
                .set(StrUtil.isNotBlank(param.getRemark()), SysCircuit::getRemark, StrUtil.trim(param.getRemark()))
                .set(SysCircuit::getUpdateTime, LocalDateTime.now());

        boolean updated = wrapper.update();

        // 修改
        if (!updated) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_CIRCUIT);
        this.refreshOssForSysCircuit(null);
        return RestResponse.OK;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse deleteForAdmin(IdParam param) {
        SysCircuit sysCircuit = lambdaQuery().getBaseMapper().selectById(param.getId());
        boolean removed = removeById(param.getId());
        if (!removed) {
            return RestResponse.SYS_DATA_STATUS_ERROR;
        }

        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_CIRCUIT);
        this.refreshOssForSysCircuit(sysCircuit.getThirdPartyAddress());
        return RestResponse.OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RestResponse enableDisableForAdmin(IdEnableDisableParam param) {
        boolean updated = lambdaUpdate()
                .eq(SysCircuit::getId, param.getId())
                .set(SysCircuit::getEnabled, param.getEnable())
                .update();

        if (!updated) {
            return RestResponse.failed(ResponseCode.SYS_DATA_STATUS_ERROR);
        }
        cacheProxy.signalRefreshCache(SysCacheRefreshTypeEnum.SYS_CIRCUIT);
        this.refreshOssForSysCircuit(null);
        return RestResponse.OK;
    }

    private void refreshOssForSysCircuit(String tageName) {
        List<SysCircuit> list = lambdaQuery().eq(SysCircuit::getEnabled, true).list();
        String originalFilename = "";  // 拼接文件完整路径
        if (list != null && list.size() > 0 && !StringUtils.isNullOrEmpty(list.get(0).getThirdPartyAddress())) {
            originalFilename = list.get(0).getThirdPartyAddress().substring(list.get(0).getThirdPartyAddress().lastIndexOf("/"));
        } else {
            if (!StringUtils.isNullOrEmpty(tageName)) {
                originalFilename = tageName.substring(tageName.lastIndexOf("/"));
            } else {
                originalFilename = "/" + OrderUtil.orderNumberToMs() + ".json";
            }
        }
        // 生成json格式文件
        try {
            // 保证创建一个新文件
            File file = new File(originalFilename);
            if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                file.getParentFile().mkdirs();
            }
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
            file.createNewFile();

            // 格式化json字符串
            Map<String, Object> map = new HashMap<>();
            map.put("errno", 0);
            List<SysCircuit> sysCircuits = lambdaQuery().eq(SysCircuit::getEnabled, true).list();
            if (sysCircuits != null && sysCircuits.size() > 0) {
                List<String> appStrs = new ArrayList<>();
                List<String> webStrs = new ArrayList<>();
                sysCircuits.stream().forEach(sc -> {
                    String rsaUrl = StrUtil.rsaPublicEncryptApiData(sc.getCircuitUrl());
                    if (sc.getCircuitType() == SysCircuitTypeEnum.APP) {
                        appStrs.add(rsaUrl);
                    } else {
                        webStrs.add(rsaUrl);
                    }
                });
                map.put("errno", 0);
                map.put("app", appStrs);
                map.put("web", webStrs);
            } else {
                map.put("errno", 1);
            }
            RestResponse restResponse = RestResponse.OK;
            restResponse.setData(map);
            String jsonString = JSON.toJSONString(restResponse);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
            RestResponse<String> stringRestResponse = fileService.uploadJson(originalFilename, new FileInputStream(file));
            lambdaUpdate().eq(SysCircuit::getEnabled, true).set(SysCircuit::getThirdPartyAddress, stringRestResponse.getData()).update();
            if (file.exists()) { // 如果已存在,删除旧文件
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
