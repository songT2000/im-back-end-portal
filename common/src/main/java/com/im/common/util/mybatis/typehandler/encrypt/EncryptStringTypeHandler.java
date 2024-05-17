package com.im.common.util.mybatis.typehandler.encrypt;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis自动加解密类型处理类
 *
 * @author Barry
 * @date 2019/2/15
 */
public class EncryptStringTypeHandler extends BaseTypeHandler<EncryptString> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, EncryptString parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, EncryptStringUtil.encrypt(parameter));
    }

    @Override
    public EncryptString getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String encryptedStr = rs.getString(columnName);
        if (encryptedStr != null) {
            return EncryptStringUtil.decryptToObj(encryptedStr);
        }
        return null;
    }

    @Override
    public EncryptString getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String encryptedStr = rs.getString(columnIndex);
        if (encryptedStr != null) {
            return EncryptStringUtil.decryptToObj(encryptedStr);
        }
        return null;
    }

    @Override
    public EncryptString getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String encryptedStr = cs.getString(columnIndex);
        if (encryptedStr != null) {
            return EncryptStringUtil.decryptToObj(encryptedStr);
        }
        return null;
    }
}