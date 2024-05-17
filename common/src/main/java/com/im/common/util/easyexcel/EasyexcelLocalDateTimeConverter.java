package com.im.common.util.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.im.common.util.DateTimeUtil;
import com.im.common.util.StrUtil;

import java.time.LocalDateTime;

/**
 * @author Barry
 * @date 2020-09-08
 */
public class EasyexcelLocalDateTimeConverter implements Converter<LocalDateTime> {

    @Override
    public Class<LocalDateTime> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {
        if (cellData == null || StrUtil.isBlank(cellData.getStringValue())) {
            return null;
        }
        return DateTimeUtil.fromDateTimeStr(cellData.getStringValue());
    }

    @Override
    public CellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (value == null) {
            return new CellData<>(StrUtil.EMPTY);
        }
        return new CellData<>(DateTimeUtil.toDateTimeStr(value));
    }

}
