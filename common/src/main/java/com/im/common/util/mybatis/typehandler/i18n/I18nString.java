package com.im.common.util.mybatis.typehandler.i18n;

import java.io.Serializable;

/**
 * mybatis自动国际化业务数据
 * 要求字段是String+Varchar类型，同时{@link com.im.common.util.fastjson.FastJsonConfigUtil}自动将对象转为String进行输出
 *
 * @author Barry
 * @date 2019/2/15
 */
public class I18nString implements Serializable, Comparable<String>, CharSequence {
    private String value;

    public I18nString() {
    }

    public I18nString(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(String o) {
        return o.compareTo(value);
    }
}