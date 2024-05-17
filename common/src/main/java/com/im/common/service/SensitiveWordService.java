package com.im.common.service;

import com.im.common.entity.SensitiveWord;
import com.im.common.response.RestResponse;
import com.im.common.util.mybatis.service.MyBatisPlusService;

import java.util.List;

public interface SensitiveWordService extends MyBatisPlusService<SensitiveWord> {

    /**
     * 删除敏感词
     */
    RestResponse delete(Long id);

    /**
     * 新增敏感词
     */
    RestResponse add(List<String> words);

}
