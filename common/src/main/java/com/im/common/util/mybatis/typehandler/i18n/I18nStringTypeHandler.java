package com.im.common.util.mybatis.typehandler.i18n;

import com.im.common.util.i18n.I18nTranslateUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis自动国际化业务数据处理类
 *
 * @author Barry
 * @date 2019/2/15
 */
public class I18nStringTypeHandler extends BaseTypeHandler<I18nString> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, I18nString parameter, JdbcType jdbcType) throws SQLException {
        // ps.setString(i, I18nTranslateUtil.translate(parameter.toString()));
        ps.setString(i, parameter.toString());
    }

    @Override
    public I18nString getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String str = rs.getString(columnName);
        if (str != null) {
            // return new I18nString(I18nTranslateUtil.translate(str));
            return new I18nString(str);
        }
        return null;
    }

    @Override
    public I18nString getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String str = rs.getString(columnIndex);
        if (str != null) {
            // return new I18nString(I18nTranslateUtil.translate(str));
            return new I18nString(str);
        }
        return null;
    }

    @Override
    public I18nString getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String str = cs.getString(columnIndex);
        if (str != null) {
            // return new I18nString(I18nTranslateUtil.translate(str));
            return new I18nString(str);
        }
        return null;
    }
}