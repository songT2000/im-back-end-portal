package com.im.common.util.mybatis.typehandler.fill;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * 自动填充createTime和updateTime两个字段
 *
 * @author Barry
 * @date 10/10/19
 */
public class MybatisMetaObjectHandler implements MetaObjectHandler {

    private static final String FIELD_CREATE_TIME = "createTime";
    private static final String FIELD_UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        insertFillWithTime(metaObject, FIELD_CREATE_TIME);
        insertFillWithTime(metaObject, FIELD_UPDATE_TIME);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        updateFillWithTime(metaObject, FIELD_UPDATE_TIME);
    }

    private void insertFillWithTime(MetaObject metaObject, String fieldName) {
        // 必须要有这个字段
        if (!hasProperty(metaObject, fieldName)) {
            return;
        }

        // 如果已经设置过值，则不再自动设置
        if (hasValue(metaObject, fieldName)) {
            return;
        }

        strictInsertFill(metaObject, fieldName, LocalDateTime.class, LocalDateTime.now());
        // setInsertFieldValByName(fieldName, LocalDateTime.now(), metaObject);
    }

    private void updateFillWithTime(MetaObject metaObject, String fieldName) {
        // 必须要有这个字段
        if (!hasProperty(metaObject, fieldName)) {
            return;
        }

        // 无论是否设置过值，都进行修改
        // setUpdateFieldValByName(fieldName, LocalDateTime.now(), metaObject);
        strictUpdateFill(metaObject, fieldName, LocalDateTime.class, LocalDateTime.now());
    }

    private boolean hasProperty(MetaObject metaObject, String fieldName) {
        String property = metaObject.findProperty(fieldName, true);

        return StrUtil.isNotBlank(property);
    }

    private boolean hasSetter(MetaObject metaObject, String fieldName) {
        return metaObject.hasSetter(fieldName);
    }

    private boolean hasValue(MetaObject metaObject, String fieldName) {
        Object val = getFieldValByName(fieldName, metaObject);
        return val != null;
    }
}
