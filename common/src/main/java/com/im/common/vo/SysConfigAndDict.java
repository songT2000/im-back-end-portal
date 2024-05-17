package com.im.common.vo;

import com.im.common.cache.sysconfig.vo.SysConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Barry
 * @date 2020-07-05
 */
@Data
@ApiModel
public class SysConfigAndDict {
    @ApiModelProperty("系统配置列表")
    private List<SysConfigVO> list;

    @ApiModelProperty("数据字典列表, <key, <key, value>>")
    private Map<String, Map<String, String>> dict;

    public SysConfigAndDict(List<SysConfigVO> list, Map<String, Map<String, String>> dict) {
        this.list = list;
        this.dict = dict;
    }
}
