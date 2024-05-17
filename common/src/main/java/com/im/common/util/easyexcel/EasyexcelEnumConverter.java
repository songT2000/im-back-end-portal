package com.im.common.util.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.im.common.entity.enums.BaseEnum;
import com.im.common.util.i18n.I18nTranslateUtil;

/**
 * @author Barry
 * @date 2020-09-08
 */
public class EasyexcelEnumConverter implements Converter<BaseEnum> {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public BaseEnum convertToJavaData(CellData cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(BaseEnum value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (value == null) {
            return null;
        }
        String text = I18nTranslateUtil.translate(value.getStr());
        return new CellData(text);
    }
}
