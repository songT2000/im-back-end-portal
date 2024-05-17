package com.im.common.util.api.im.tencent.entity.param.group;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 *  查询群组信息
 */
@NoArgsConstructor
@Data
public class TiGroupQueryParam implements Serializable {

    /**
     *  群组列表，必填
     */
    @JSONField(name = "GroupIdList")
    private List<String> groupIdList;
    /**
     * 包含四个过滤器：
     * <li>GroupBaseInfoFilter:基础信息字段过滤器</li>，
     * <li>MemberInfoFilter:成员信息字段过滤器</li>，
     * <li>AppDefinedDataFilter_Group:群组维度的自定义字段过滤器</li>
     * <li>AppDefinedDataFilter_GroupMember:群组成员字段过滤器</li>，，，
     */
    @JSONField(name = "ResponseFilter")
    private ResponseFilterDTO responseFilter;

    @NoArgsConstructor
    @Data
    public static class ResponseFilterDTO {
        /**
         * 基础信息字段过滤器,指定需要获取的基础信息字段
         */
        @JSONField(name = "GroupBaseInfoFilter")
        private List<String> groupBaseInfoFilter;
        /**
         *
         */
        @JSONField(name = "MemberInfoFilter")
        private List<String> memberInfoFilter;
        @JSONField(name = "AppDefinedDataFilter_Group")
        private List<String> appdefineddatafilterGroup;
        @JSONField(name = "AppDefinedDataFilter_GroupMember")
        private List<String> appdefineddatafilterGroupmember;
    }
}
