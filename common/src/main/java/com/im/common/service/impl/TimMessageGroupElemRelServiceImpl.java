package com.im.common.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.im.common.entity.enums.MsgTypeEnum;
import com.im.common.entity.enums.TiMsgTypeEnum;
import com.im.common.entity.tim.*;
import com.im.common.mapper.*;
import com.im.common.service.*;
import com.im.common.util.api.im.tencent.entity.param.message.*;
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
public class TimMessageGroupElemRelServiceImpl 
        extends MyBatisPlusServiceImpl<TimMessageGroupElemRelMapper,TimMessageGroupElemRel>
        implements TimMessageGroupElemRelService {

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
    public void saveGroupMessageElem(TiMsgTypeEnum msgType, Long messageId, Object elem) {
        batchSaveGroupMessageElem(ListUtil.toList(new TimMessageElemBo(messageId,elem,msgType)));
    }

    @Override
    public void withdraw(Long messageId) {
        deleteByMessageIds(ListUtil.toList(messageId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByMessageIds(List<Long> messageIds) {
        if(CollUtil.isEmpty(messageIds)){
            return;
        }
        LambdaQueryWrapper<TimMessageGroupElemRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TimMessageGroupElemRel::getMessageId,messageIds);
        List<TimMessageGroupElemRel> timMessageGroupElemRels = list(queryWrapper);
        if(CollUtil.isEmpty(timMessageGroupElemRels)){
            return;
        }
        List<Long> textElemIds= new ArrayList<>() ,fileElemIds= new ArrayList<>() ,faceElemIds= new ArrayList<>(),videoElemIds= new ArrayList<>(),
                customElemIds= new ArrayList<>(),soundElemIds= new ArrayList<>(),locationElemIds= new ArrayList<>(),imageElemIds= new ArrayList<>();
        for (TimMessageGroupElemRel elemRel : timMessageGroupElemRels) {
            switch (elemRel.getMsgType()){
                case TIMTextElem:
                    textElemIds.add(elemRel.getElemId());
                    break;
                case TIMFileElem:
                    fileElemIds.add(elemRel.getElemId());
                    break;
                case TIMFaceElem:
                    faceElemIds.add(elemRel.getElemId());
                    break;
                case TIMVideoFileElem:
                    videoElemIds.add(elemRel.getElemId());
                    break;
                case TIMCustomElem:
                    customElemIds.add(elemRel.getElemId());
                    break;
                case TIMSoundElem:
                    soundElemIds.add(elemRel.getElemId());
                    break;
                case TIMLocationElem:
                    locationElemIds.add(elemRel.getElemId());
                    break;
                case TIMImageElem:
                    imageElemIds.add(elemRel.getElemId());
                    break;
                default:
                    break;
            }

        }
        if(CollUtil.isNotEmpty(textElemIds)){
            timMessageElemTextService.removeByIds(textElemIds);
        }
        if(CollUtil.isNotEmpty(fileElemIds)){
            timMessageElemFileService.removeByIds(fileElemIds);
        }
        if(CollUtil.isNotEmpty(faceElemIds)){
            timMessageElemFaceService.removeByIds(faceElemIds);
        }
        if(CollUtil.isNotEmpty(videoElemIds)){
            timMessageElemVideoService.removeByIds(videoElemIds);
        }
        if(CollUtil.isNotEmpty(customElemIds)){
            timMessageElemCustomService.removeByIds(customElemIds);
        }
        if(CollUtil.isNotEmpty(soundElemIds)){
            timMessageElemSoundService.removeByIds(soundElemIds);
        }
        if(CollUtil.isNotEmpty(locationElemIds)){
            timMessageElemLocationService.removeByIds(locationElemIds);
        }
        if(CollUtil.isNotEmpty(imageElemIds)){
            timMessageElemImageService.removeByIds(imageElemIds);
        }
        remove(queryWrapper);
    }

    @Override
    public void batchSaveGroupMessageElem(List<TimMessageElemBo> list) {
        List<TimMessageElemText> elemTexts= new ArrayList<>();
        List<TimMessageElemFile> elemFiles= new ArrayList<>() ;
        List<TimMessageElemFace> elemFaces= new ArrayList<>();
        List<TimMessageElemVideo> elemVideos= new ArrayList<>();
        List<TimMessageElemCustom> elemCustoms= new ArrayList<>();
        List<TimMessageElemSound>  elemSounds= new ArrayList<>();
        List<TimMessageElemLocation> elemLocations= new ArrayList<>();
        List<TimMessageElemImage> elemImages= new ArrayList<>();
        List<TimMessageGroupElemRel> rels = new ArrayList<>();
        for (TimMessageElemBo elem : list) {
            switch (elem.getMsgType()){
                case TIMTextElem:
                    TimMessageElemText elemText = (TimMessageElemText) elem.getElem();
                    elemText.setId(IdWorker.getId());
                    elemText.setCreateTime(LocalDateTime.now());
                    elemText.setUpdateTime(LocalDateTime.now());
                    elemTexts.add(elemText);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMTextElem,elem.getMessageId(),elemText.getId()));
                    break;
                case TIMFileElem:
                    TimMessageElemFile elemFile = (TimMessageElemFile) elem.getElem();
                    elemFile.setId(IdWorker.getId());
                    elemFile.setCreateTime(LocalDateTime.now());
                    elemFile.setUpdateTime(LocalDateTime.now());
                    elemFiles.add(elemFile);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMFileElem,elem.getMessageId(),elemFile.getId()));
                    break;
                case TIMFaceElem:
                    TimMessageElemFace elemFace = (TimMessageElemFace) elem.getElem();
                    elemFace.setId(IdWorker.getId());
                    elemFace.setCreateTime(LocalDateTime.now());
                    elemFace.setUpdateTime(LocalDateTime.now());
                    elemFaces.add(elemFace);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMFaceElem,elem.getMessageId(),elemFace.getId()));
                    break;
                case TIMVideoFileElem:
                    TimMessageElemVideo elemVideo = (TimMessageElemVideo) elem.getElem();
                    elemVideo.setId(IdWorker.getId());
                    elemVideo.setCreateTime(LocalDateTime.now());
                    elemVideo.setUpdateTime(LocalDateTime.now());
                    elemVideos.add(elemVideo);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMVideoFileElem,elem.getMessageId(),elemVideo.getId()));
                    break;
                case TIMCustomElem:
                    TimMessageElemCustom elemCustom = (TimMessageElemCustom) elem.getElem();
                    elemCustom.setId(IdWorker.getId());
                    elemCustom.setCreateTime(LocalDateTime.now());
                    elemCustom.setUpdateTime(LocalDateTime.now());
                    elemCustoms.add(elemCustom);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMCustomElem,elem.getMessageId(),elemCustom.getId()));
                    break;
                case TIMSoundElem:
                    TimMessageElemSound elemSound = (TimMessageElemSound) elem.getElem();
                    elemSound.setId(IdWorker.getId());
                    elemSound.setCreateTime(LocalDateTime.now());
                    elemSound.setUpdateTime(LocalDateTime.now());
                    elemSounds.add(elemSound);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMSoundElem,elem.getMessageId(),elemSound.getId()));
                    break;
                case TIMLocationElem:
                    TimMessageElemLocation elemLocation = (TimMessageElemLocation) elem.getElem();
                    elemLocation.setId(IdWorker.getId());
                    elemLocation.setCreateTime(LocalDateTime.now());
                    elemLocation.setUpdateTime(LocalDateTime.now());
                    elemLocations.add(elemLocation);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMLocationElem,elem.getMessageId(),elemLocation.getId()));
                    break;
                case TIMImageElem:
                    TimMessageElemImage elemImage = (TimMessageElemImage) elem.getElem();
                    elemImage.setId(IdWorker.getId());
                    elemImage.setCreateTime(LocalDateTime.now());
                    elemImage.setUpdateTime(LocalDateTime.now());
                    elemImages.add(elemImage);
                    rels.add(new TimMessageGroupElemRel(MsgTypeEnum.TIMImageElem,elem.getMessageId(),elemImage.getId()));
                    break;
                default:
                    break;
            }

        }
        if(CollUtil.isNotEmpty(elemTexts)){
            timMessageElemTextService.saveBatch(elemTexts);
        }
        if(CollUtil.isNotEmpty(elemFiles)){
            timMessageElemFileService.saveBatch(elemFiles);
        }
        if(CollUtil.isNotEmpty(elemFaces)){
            timMessageElemFaceService.saveBatch(elemFaces);
        }
        if(CollUtil.isNotEmpty(elemVideos)){
            timMessageElemVideoService.saveBatch(elemVideos);
        }
        if(CollUtil.isNotEmpty(elemCustoms)){
            timMessageElemCustomService.saveBatch(elemCustoms);
        }
        if(CollUtil.isNotEmpty(elemSounds)){
            timMessageElemSoundService.saveBatch(elemSounds);
        }
        if(CollUtil.isNotEmpty(elemLocations)){
            timMessageElemLocationService.saveBatch(elemLocations);
        }
        if(CollUtil.isNotEmpty(elemImages)){
            timMessageElemImageService.saveBatch(elemImages);
        }
        saveBatch(rels);
    }

    @Override
    public List<TimMessageBody> getByIds(List<Long> messageIds) {
        List<TimMessageBody> results = new ArrayList<>();
        List<TimMessageGroupElemRel> list = lambdaQuery().in(TimMessageGroupElemRel::getMessageId, messageIds).list();
        Map<MsgTypeEnum, List<TimMessageGroupElemRel>> map = list.stream().collect(Collectors.groupingBy(TimMessageGroupElemRel::getMsgType));
        for (MsgTypeEnum msgType : map.keySet()) {
            List<TimMessageGroupElemRel> items = map.get(msgType);
            if(CollUtil.isEmpty(items)){
                continue;
            }
            List<Long> elemIds = items.stream().map(TimMessageGroupElemRel::getElemId).collect(Collectors.toList());
            switch (msgType){
                case TIMTextElem:
                    List<TimMessageElemText> elemTexts = timMessageElemTextService.listByIds(elemIds);
                    for (TimMessageElemText elem : elemTexts) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMFileElem:
                    List<TimMessageElemFile> elemFiles = timMessageElemFileService.listByIds(elemIds);
                    for (TimMessageElemFile elem : elemFiles) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMCustomElem:
                    List<TimMessageElemCustom> elemCustoms = timMessageElemCustomService.listByIds(elemIds);
                    for (TimMessageElemCustom elem : elemCustoms) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMFaceElem:
                    List<TimMessageElemFace> elemFaces = timMessageElemFaceService.listByIds(elemIds);
                    for (TimMessageElemFace elem : elemFaces) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMImageElem:
                    List<TimMessageElemImage> elemImages = timMessageElemImageService.listByIds(elemIds);
                    for (TimMessageElemImage elem : elemImages) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMLocationElem:
                    List<TimMessageElemLocation> elems = timMessageElemLocationService.listByIds(elemIds);
                    for (TimMessageElemLocation elem : elems) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMSoundElem:
                    List<TimMessageElemSound> elemSounds = timMessageElemSoundService.listByIds(elemIds);
                    for (TimMessageElemSound elem : elemSounds) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
                case TIMVideoFileElem:
                    List<TimMessageElemVideo> elemVideos = timMessageElemVideoService.listByIds(elemIds);
                    for (TimMessageElemVideo elem : elemVideos) {
                        Long messageId = items.stream().filter(p->p.getElemId().equals(elem.getId())).map(TimMessageGroupElemRel::getMessageId).findAny().orElse(null);
                        results.add(new TimMessageBody(messageId,msgType,elem));
                    }
                    break;
            }
        }
        return results;
    }
}
