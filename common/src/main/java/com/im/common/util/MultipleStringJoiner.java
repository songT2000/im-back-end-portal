package com.im.common.util;

import com.im.common.constant.CommonConstant;

import java.util.*;

/**
 * @author Barry
 * @date 2021-03-31
 */
public final class MultipleStringJoiner {
    private final String everyPrefix;
    private final String everyDelimiter;
    private final String everySuffix;

    /**
     * 纯内容
     */
    private Collection<String> values;

    public MultipleStringJoiner() {
        this(CommonConstant.DOT_EN, "[", "]", false);
    }

    public MultipleStringJoiner(String everyDelimiter,
                                String everyPrefix,
                                String everySuffix,
                                boolean allowRepeat) {
        Objects.requireNonNull(everyDelimiter, "The prefix must not be null");
        Objects.requireNonNull(everyPrefix, "The delimiter must not be null");
        Objects.requireNonNull(everySuffix, "The suffix must not be null");

        // make defensive copies of arguments
        this.everyDelimiter = StrUtil.nullToEmpty(everyDelimiter);
        this.everyPrefix = StrUtil.nullToEmpty(everyPrefix);
        this.everySuffix = StrUtil.nullToEmpty(everySuffix);
        values = allowRepeat ? new ArrayList<>() : new HashSet<>();
    }

    @Override
    public String toString() {
        return CollectionUtil.join(CollectionUtil.toList(values, e -> everyPrefix + e.toString() + everySuffix), everyDelimiter);
    }

    public MultipleStringJoiner from(String strArr) {
        if (StrUtil.isBlank(strArr)) {
            return this;
        }
        String[] strArrSplit = StrUtil.splitToArray(strArr, everyDelimiter);
        for (String str : strArrSplit) {
            this.values.add(resolveValue(str));
        }
        return this;
    }

    private String resolveValue(String preSuffixValue) {
        if (StrUtil.isBlank(preSuffixValue)) {
            return StrUtil.EMPTY;
        }
        String value = StrUtil.subSuf(preSuffixValue, everyPrefix.length());
        value = StrUtil.sub(value, 0, everySuffix.length());
        return value;
    }

    /**
     * Adds a copy of the given {@code CharSequence} value as the next
     * element of the {@code StringJoiner} value. If {@code newElement} is
     * {@code null}, then {@code "null"} is added.
     *
     * @param newElement The element to add
     * @return a reference to this {@code StringJoiner}
     */
    public MultipleStringJoiner add(String newElement) {
        values.add(newElement);
        return this;
    }

    public boolean contains(String element) {
        return this.values.contains(element);
    }
}
