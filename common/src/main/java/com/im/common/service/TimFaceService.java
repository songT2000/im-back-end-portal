package com.im.common.service;

import com.im.common.entity.tim.TimFace;
import com.im.common.param.TimFaceAddParam;
import com.im.common.param.TimFaceItemAddParam;
import com.im.common.param.TimFaceItemDeleteParam;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;
import java.util.List;

public interface TimFaceService extends MyBatisPlusService<TimFace> {

    /**
     * 获取所有表情包专辑
     */
    List<TimFace> getAll();

    /**
     * 删除表情包专辑
     */
    RestResponse delete(Long id);
    /**
     * 新增表情包专辑
     */
    RestResponse add(TimFaceAddParam param);

    /**
     * 删除表情包元素
     */
    RestResponse deleteItem(TimFaceItemDeleteParam param);

    /**
     * 新增表情包元素
     */
    RestResponse addItem(TimFaceItemAddParam param);

}
