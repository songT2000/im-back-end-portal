package com.im.common.cache.sysconfig.resolver;

import com.im.common.cache.sysconfig.SysConfigResolver;
import com.im.common.cache.sysconfig.SysConfigResolverGroup;
import com.im.common.cache.sysconfig.bo.OssConfigBO;
import com.im.common.cache.sysconfig.bo.OssTypeEnum;
import com.im.common.entity.SysConfig;
import com.im.common.entity.enums.SysConfigGroupEnum;
import com.im.common.util.EnumUtil;
import com.im.common.util.StrUtil;

import java.util.List;

/**
 * OSS存储
 *
 * @author Barry
 * @date 2019/10/26
 */
@SysConfigResolverGroup(SysConfigGroupEnum.OSS)
public class OssConfigResolver implements SysConfigResolver<OssConfigBO> {
    @Override
    public OssConfigBO resolve(List<SysConfig> sysConfigs) {
        OssConfigBO config = new OssConfigBO();

        sysConfigs.forEach(sysConfig -> this.resolveSingle(config, sysConfig));

        return config;
    }

    @Override
    public OssConfigBO getDefault() {
        return new OssConfigBO();
    }

    private void resolveSingle(OssConfigBO config, SysConfig sysConfig) {
        String item = sysConfig.getItem();
        String value = sysConfig.getValue();

        switch (item) {
            case "OSS_TYPE":
                config.setOssType(EnumUtil.valueOfIEnum(OssTypeEnum.class, value));
                break;
            case "VOUCHER_FOLDER":
                config.setVoucherFolder(StrUtil.trim(value));
                break;
            case "ID_FOLDER":
                config.setIdFolder(StrUtil.trim(value));
                break;
            case "IMAGE_FOLDER":
                config.setImageFolder(StrUtil.trim(value));
                break;
            case "CIRCUIT_FOLDER":
                config.setCircuitFolder(StrUtil.trim(value));
                break;
            case "AVATAR_FOLDER":
                config.setAvatarFolder(StrUtil.trim(value));
                break;
            case "VIDEO_FOLDER":
                config.setVideoFolder(StrUtil.trim(value));
                break;
            case "SOUND_FOLDER":
                config.setSoundFolder(StrUtil.trim(value));
                break;
            case "FILE_FOLDER":
                config.setFileFolder(StrUtil.trim(value));
                break;
            case "ALIYUN_ENDPOINT":
                config.setAliyunEndpoint(StrUtil.trim(value));
                break;
            case "ALIYUN_ACCESS_KEY_ID":
                config.setAliyunAccessKeyId(StrUtil.trim(value));
                break;
            case "ALIYUN_ACCESS_KEY_SECRET":
                config.setAliyunAccessKeySecret(StrUtil.trim(value));
                break;
            case "ALIYUN_BUCKET_NAME":
                config.setAliyunBucketName(StrUtil.trim(value));
                break;
            case "MINIO_SECRET_KEY":
                config.setMinioSecretKey(StrUtil.trim(value));
                break;
            case "MINIO_ACCESS_KEY":
                config.setMinioAccessKey(StrUtil.trim(value));
                break;
            case "MINIO_INTERNAL_ENDPOINT":
                config.setMinioInternalEndpoint(StrUtil.trim(value));
                break;
            case "MINIO_ENDPOINT":
                config.setMinioEndpoint(StrUtil.trim(value));
                break;
            case "MINIO_BUCKET_NAME":
                config.setMinioBucketName(StrUtil.trim(value));
                break;
            default:
                break;
        }
    }
}
