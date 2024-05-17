package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.*;
import com.im.common.mapper.TimMessageC2cElemRelMapper;
import com.im.common.service.*;
import com.im.common.util.mybatis.service.MyBatisPlusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TimMessageC2cElemRelServiceImpl
        extends MyBatisPlusServiceImpl<TimMessageC2cElemRelMapper, TimMessageC2cElemRel>
        implements TimMessageC2cElemRelService {

    private TimMessageElemCustomService timMessageElemCustomService;

    private TimMessageElemFaceService timMessageElemFaceService;

    private TimMessageElemImageService timMessageElemImageService;

    private TimMessageElemLocationService timMessageElemLocationService;

    private TimMessageElemTextService timMessageElemTextService;

    private TimMessageElemSoundService timMessageElemSoundService;

    private TimMessageElemVideoService timMessageElemVideoService;

    private TimMessageElemFileService timMessageElemFileService;

    @Autowired
    public void setTimMessageElemCustomService(TimMessageElemCustomService timMessageElemCustomService) {
        this.timMessageElemCustomService = timMessageElemCustomService;
    }

    @Autowired
    public void setTimMessageElemFaceService(TimMessageElemFaceService timMessageElemFaceService) {
        this.timMessageElemFaceService = timMessageElemFaceService;
    }

    @Autowired
    public void setTimMessageElemImageService(TimMessageElemImageService timMessageElemImageService) {
        this.timMessageElemImageService = timMessageElemImageService;
    }

    @Autowired
    public void setTimMessageElemLocationService(TimMessageElemLocationService timMessageElemLocationService) {
        this.timMessageElemLocationService = timMessageElemLocationService;
    }

    @Autowired
    public void setTimMessageElemTextService(TimMessageElemTextService timMessageElemTextService) {
        this.timMessageElemTextService = timMessageElemTextService;
    }

    @Autowired
    public void setTimMessageElemSoundService(TimMessageElemSoundService timMessageElemSoundService) {
        this.timMessageElemSoundService = timMessageElemSoundService;
    }

    @Autowired
    public void setTimMessageElemVideoService(TimMessageElemVideoService timMessageElemVideoService) {
        this.timMessageElemVideoService = timMessageElemVideoService;
    }

    @Autowired
    public void setTimMessageElemFileService(TimMessageElemFileService timMessageElemFileService) {
        this.timMessageElemFileService = timMessageElemFileService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveC2cMessageElem(TiMsgTypeEnum msgType, Long messageId, Object elem) {
        batchSaveGroupMessageElem(ListUtil.of(new TimMessageElemBo(messageId, elem, msgType)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdraw(Long messageId) {
        LambdaQueryWrapper<TimMessageC2cElemRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TimMessageC2cElemRel::getMessageId, messageId);
        List<TimMessageC2cElemRel> timMessageC2cElemRels = list(queryWrapper);
        if (CollUtil.isEmpty(timMessageC2cElemRels)) {
            return;
        }
        //目前消息和元素都是一对一，直接循环内删除关联数据
        for (TimMessageC2cElemRel elemRel : timMessageC2cElemRels) {
            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMTextElem)) {
                timMessageElemTextService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMFaceElem)) {
                timMessageElemFaceService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMCustomElem)) {
                timMessageElemFaceService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMFileElem)) {
                timMessageElemFileService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMImageElem)) {
                timMessageElemImageService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMSoundElem)) {
                timMessageElemSoundService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMLocationElem)) {
                timMessageElemLocationService.removeById(elemRel.getElemId());
            }

            if (elemRel.getMsgType().equals(MsgTypeEnum.TIMFileElem)) {
                timMessageElemFileService.removeById(elemRel.getElemId());
            }
        }
        remove(queryWrapper);
    }

    @Override
    public void batchSaveGroupMessageElem(List<TimMessageElemBo> list) {
        List<TimMessageElemText> elemTexts = new ArrayList<>();
        List<TimMessageElemFile> elemFiles = new ArrayList<>();
        List<TimMessageElemFace> elemFaces = new ArrayList<>();
        List<TimMessageElemVideo> elemVideos = new ArrayList<>();
        List<TimMessageElemCustom> elemCustoms = new ArrayList<>();
        List<TimMessageElemSound> elemSounds = new ArrayList<>();
        List<TimMessageElemLocation> elemLocations = new ArrayList<>();
        List<TimMessageElemImage> elemImages = new ArrayList<>();
        List<TimMessageC2cElemRel> rels = new ArrayList<>();
        for (TimMessageElemBo elem : list) {
            switch (elem.getMsgType()) {
                case TIMTextElem:
                    TimMessageElemText elemText = (TimMessageElemText) elem.getElem();
                    elemText.setId(IdWorker.getId());
                    elemText.setCreateTime(LocalDateTime.now());
                    elemText.setUpdateTime(LocalDateTime.now());
                    elemTexts.add(elemText);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMTextElem, elem.getMessageId(), elemText.getId()));
                    break;
                case TIMFileElem:
                    TimMessageElemFile elemFile = (TimMessageElemFile) elem.getElem();
                    elemFile.setId(IdWorker.getId());
                    elemFile.setCreateTime(LocalDateTime.now());
                    elemFile.setUpdateTime(LocalDateTime.now());
                    elemFiles.add(elemFile);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMFileElem, elem.getMessageId(), elemFile.getId()));
                    break;
                case TIMFaceElem:
                    TimMessageElemFace elemFace = (TimMessageElemFace) elem.getElem();
                    elemFace.setId(IdWorker.getId());
                    elemFace.setCreateTime(LocalDateTime.now());
                    elemFace.setUpdateTime(LocalDateTime.now());
                    elemFaces.add(elemFace);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMFaceElem, elem.getMessageId(), elemFace.getId()));
                    break;
                case TIMVideoFileElem:
                    TimMessageElemVideo elemVideo = (TimMessageElemVideo) elem.getElem();
                    elemVideo.setId(IdWorker.getId());
                    elemVideo.setCreateTime(LocalDateTime.now());
                    elemVideo.setUpdateTime(LocalDateTime.now());
                    elemVideos.add(elemVideo);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMVideoFileElem, elem.getMessageId(), elemVideo.getId()));
                    break;
                case TIMCustomElem:
                    TimMessageElemCustom elemCustom = (TimMessageElemCustom) elem.getElem();
                    elemCustom.setId(IdWorker.getId());
                    elemCustom.setCreateTime(LocalDateTime.now());
                    elemCustom.setUpdateTime(LocalDateTime.now());
                    //补充ext字段
                    boolean isJson = JSONUtil.isJson(elemCustom.getData());
                    if (isJson) {
                        JSONObject jsonObject = JSONUtil.parseObj(elemCustom.getData());
                        String businessID = jsonObject.getStr("businessID");
                        elemCustom.setExt(businessID);
                    }
                    elemCustoms.add(elemCustom);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMCustomElem, elem.getMessageId(), elemCustom.getId()));
                    break;
                case TIMSoundElem:
                    TimMessageElemSound elemSound = (TimMessageElemSound) elem.getElem();
                    elemSound.setId(IdWorker.getId());
                    elemSound.setCreateTime(LocalDateTime.now());
                    elemSound.setUpdateTime(LocalDateTime.now());
                    elemSounds.add(elemSound);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMSoundElem, elem.getMessageId(), elemSound.getId()));
                    break;
                case TIMLocationElem:
                    TimMessageElemLocation elemLocation = (TimMessageElemLocation) elem.getElem();
                    elemLocation.setId(IdWorker.getId());
                    elemLocation.setCreateTime(LocalDateTime.now());
                    elemLocation.setUpdateTime(LocalDateTime.now());
                    elemLocations.add(elemLocation);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMLocationElem, elem.getMessageId(), elemLocation.getId()));
                    break;
                case TIMImageElem:
                    TimMessageElemImage elemImage = (TimMessageElemImage) elem.getElem();
                    elemImage.setId(IdWorker.getId());
                    elemImage.setCreateTime(LocalDateTime.now());
                    elemImage.setUpdateTime(LocalDateTime.now());
                    elemImages.add(elemImage);
                    rels.add(new TimMessageC2cElemRel(MsgTypeEnum.TIMImageElem, elem.getMessageId(), elemImage.getId()));
                    break;
                default:
                    break;
            }

        }
        if (CollUtil.isNotEmpty(elemTexts)) {
            timMessageElemTextService.saveBatch(elemTexts);
        }
        if (CollUtil.isNotEmpty(elemFiles)) {
            timMessageElemFileService.saveBatch(elemFiles);
        }
        if (CollUtil.isNotEmpty(elemFaces)) {
            timMessageElemFaceService.saveBatch(elemFaces);
        }
        if (CollUtil.isNotEmpty(elemVideos)) {
            timMessageElemVideoService.saveBatch(elemVideos);
        }
        if (CollUtil.isNotEmpty(elemCustoms)) {
            timMessageElemCustomService.saveBatch(elemCustoms);
        }
        if (CollUtil.isNotEmpty(elemSounds)) {
            timMessageElemSoundService.saveBatch(elemSounds);
        }
        if (CollUtil.isNotEmpty(elemLocations)) {
            timMessageElemLocationService.saveBatch(elemLocations);
        }
        if (CollUtil.isNotEmpty(elemImages)) {
            timMessageElemImageService.saveBatch(elemImages);
        }
        saveBatch(rels);
    }

    @Override
    public List<TimMessageBody> getByIds(List<Long> messageIds) {
        List<TimMessageBody> results = new ArrayList<>();
        List<TimMessageC2cElemRel> list = lambdaQuery().in(TimMessageC2cElemRel::getMessageId, messageIds).list();
        Map<MsgTypeEnum, List<TimMessageC2cElemRel>> map = list.stream().collect(Collectors.groupingBy(TimMessageC2cElemRel::getMsgType));
        for (MsgTypeEnum msgType : map.keySet()) {
            List<TimMessageC2cElemRel> items = map.get(msgType);
            if (CollUtil.isEmpty(items)) {
                continue;
            }
            List<Long> elemIds = items.stream().map(TimMessageC2cElemRel::getElemId).collect(Collectors.toList());
            switch (msgType) {
                case TIMTextElem:
                    List<TimMessageElemText> elemTexts = timMessageElemTextService.listByIds(elemIds);
                    for (TimMessageElemText elem : elemTexts) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMFileElem:
                    List<TimMessageElemFile> elemFiles = timMessageElemFileService.listByIds(elemIds);
                    for (TimMessageElemFile elem : elemFiles) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMCustomElem:
                    List<TimMessageElemCustom> elemCustoms = timMessageElemCustomService.listByIds(elemIds);
                    for (TimMessageElemCustom elem : elemCustoms) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMFaceElem:
                    List<TimMessageElemFace> elemFaces = timMessageElemFaceService.listByIds(elemIds);
                    for (TimMessageElemFace elem : elemFaces) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMImageElem:
                    List<TimMessageElemImage> elemImages = timMessageElemImageService.listByIds(elemIds);
                    for (TimMessageElemImage elem : elemImages) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMLocationElem:
                    List<TimMessageElemLocation> elems = timMessageElemLocationService.listByIds(elemIds);
                    for (TimMessageElemLocation elem : elems) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMSoundElem:
                    List<TimMessageElemSound> elemSounds = timMessageElemSoundService.listByIds(elemIds);
                    for (TimMessageElemSound elem : elemSounds) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
                case TIMVideoFileElem:
                    List<TimMessageElemVideo> elemVideos = timMessageElemVideoService.listByIds(elemIds);
                    for (TimMessageElemVideo elem : elemVideos) {
                        Long messageId = items.stream().filter(p -> p.getElemId().equals(elem.getId())).map(TimMessageC2cElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId, msgType, elem));
                    }
                    break;
            }
        }
        return results;
    }
}