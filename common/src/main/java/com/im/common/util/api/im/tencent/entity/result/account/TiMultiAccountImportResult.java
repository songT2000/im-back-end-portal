package com.im.common.util.api.im.tencent.entity.result.account;

import com.alibaba.fastjson.annotation.JSONField;
import com.im.common.util.api.im.tencent.entity.result.TiBaseResult;
import lombok.Data;

import java.util.List;

@Data
public class TiMultiAccountImportResult extends TiBaseResult {
    @JSONField(name = "FailAccounts")
    private List<String> failAccounts;
}
