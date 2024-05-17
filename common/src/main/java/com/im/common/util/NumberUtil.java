package com.im.common.util;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.StrSplitter;
import com.im.common.constant.CommonConstant;
import com.im.common.response.ResponseCode;
import com.im.common.response.RestResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 数字工具类
 *
 * @author Barry
 * @date 2019-10-18
 */
public final class NumberUtil extends cn.hutool.core.util.NumberUtil {

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static String divToStr(long v1, int v2, int scale) {
        BigDecimal v1Decimal = BigDecimal.valueOf(v1);
        BigDecimal v2Decimal = BigDecimal.valueOf(v2);

        BigDecimal div = div(v1Decimal, v2Decimal, 10);

        String str = roundStr(div.doubleValue(), scale);

        return str;
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static String divToStrByLong(long v1, long v2) {
        BigDecimal v1Decimal = BigDecimal.valueOf(v1);
        BigDecimal v2Decimal = BigDecimal.valueOf(v2);

        BigDecimal div = div(v1Decimal, v2Decimal, 10);

        return NumberUtil.toStr(div);
    }

    /**
     * 把系统的百分比值转成百分比字符串，如0.1转成10%，0.011就是1.1%，0.0111就是1.11%
     *
     * @param point 为空就输出0%
     * @return
     */
    public static String pointToStr(Double point) {
        if (point == null) {
            return "0%";
        }

        BigDecimal pointDecimal = BigDecimal.valueOf(point);

        BigDecimal multiply = pointDecimal.multiply(BigDecimal.valueOf(100d));

        return doubleToString(multiply.doubleValue()) + "%";
    }

    /**
     * 把系统的百分比值转成百分比字符串，如0.1转成10%，0.011就是1.1%，0.0111就是1.11%
     *
     * @param point 为空就输出0%
     * @return
     */
    public static String pointToStr(BigDecimal point) {
        if (point == null) {
            return "0%";
        }

        BigDecimal multiply = point.multiply(BigDecimal.valueOf(100d));

        return doubleToString(multiply.doubleValue()) + "%";
    }

    /**
     * 计算占比百分比
     *
     * @param rate  占比数
     * @param total 总数
     * @param scale 百分比保留几位小数
     * @return
     */
    public static String calculatePercentStr(Integer rate, Integer total, int scale) {
        if (rate == null || total == null) {
            return "0%";
        }
        // 总数不能小于0
        if (!isGreatThenZero(total)) {
            return "0%";
        }
        // 占比数为0，百分比为0
        if (rate == CommonConstant.INT_0) {
            return "0%";
        }

        BigDecimal percent = NumberUtil.div(rate, total, 20);
        percent = percent.setScale(scale + 2, RoundingMode.DOWN);
        return NumberUtil.pointToStr(percent);
    }

    /**
     * 计算占比百分比
     *
     * @param rate  占比数
     * @param total 总数
     * @param scale 百分比保留几位小数
     * @return
     */
    public static BigDecimal calculatePercent(BigDecimal rate, BigDecimal total, int scale) {
        if (rate == null || total == null) {
            return BigDecimal.ZERO;
        }
        // 总数不能小于0
        if (!isGreatThenZero(total)) {
            return BigDecimal.ZERO;
        }
        // 占比数为0，百分比为0
        if (isEquals(rate, BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        }

        BigDecimal percent = NumberUtil.div(rate, total, 20);
        percent = percent.setScale(scale + 2, RoundingMode.DOWN);
        return percent;
    }

    /**
     * 计算占比百分比
     *
     * @param rate  占比数
     * @param total 总数
     * @param scale 百分比保留几位小数
     * @return
     */
    public static String calculatePercentStr(BigDecimal rate, BigDecimal total, int scale) {
        BigDecimal percent = calculatePercent(rate, total, scale);
        return NumberUtil.pointToStr(percent);
    }

    public static String doubleToString(double value) {
        if (value % 1 == 0) {
            return Double.valueOf(value).intValue() + "";
        } else {
            return Double.valueOf(value).toString() + "";
        }
    }

    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被加值
     * @return 和
     * @since 4.0.0
     */
    public static BigDecimal add(Collection<BigDecimal> values) {
        if (CollectionUtil.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                result = result.add(value);
            }
        }
        return result;
    }

    public static double div(long v1, int v2) {
        return div(Float.toString(v1), Float.toString(v2), 2, RoundingMode.HALF_UP).doubleValue();
    }

    public static double div(long v1, int v2, int scale) {
        return div(Float.toString(v1), Float.toString(v2), scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static double div(long v1, int v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 是否是0
     *
     * @param val 数字
     * @return 是否0
     */
    public static boolean isNullOrZero(BigDecimal val) {
        if (val == null || isEquals(val, BigDecimal.ZERO)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是0
     *
     * @param val 数字
     * @return 是否0
     */
    public static boolean isNullOrZero(Integer val) {
        if (val == null || val.compareTo(0) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param integer 数字
     * @return 是否大于
     */
    public static boolean isGreatThenZero(Integer integer) {
        if (integer != null && integer.compareTo(0) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatThenZero(Double value) {
        if (value != null && value.compareTo(0d) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatThenZero(Long value) {
        if (value != null && value.compareTo(0L) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatThenZero(BigDecimal value) {
        if (value != null && isGreater(value, BigDecimal.ZERO)) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param integer 数字
     * @return 是否大于
     */
    public static boolean isGreatOrEqualZero(Integer integer) {
        if (integer != null && integer.compareTo(0) != -1) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatOrEqualZero(Double value) {
        if (value != null && value.compareTo(0d) != -1) {
            return true;
        }
        return false;
    }

    /**
     * 是否大于0，只有当大于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatOrEqualZero(Long value) {
        if (value != null && value.compareTo(0L) != -1) {
            return true;
        }
        return false;
    }

    /**
     * 只有当大于等于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatOrEqualZero(BigDecimal value) {
        if (value != null && value.doubleValue() >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 只有当大于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isGreatZero(BigDecimal value) {
        if (value != null && value.doubleValue() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 比较大小，参数1 > 参数2 返回true
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否大于
     */
    public static boolean isGreater(Long bigNum1, Long bigNum2) {
        Assert.notNull(bigNum1);
        Assert.notNull(bigNum2);
        return bigNum1.compareTo(bigNum2) > 0;
    }

    /**
     * 比较大小，参数1 > 参数2 返回true
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否大于
     */
    public static boolean isGreater(Integer bigNum1, Integer bigNum2) {
        Assert.notNull(bigNum1);
        Assert.notNull(bigNum2);
        return bigNum1.compareTo(bigNum2) > 0;
    }

    /**
     * 比较大小，参数1 > 参数2 返回true
     *
     * @param value1 数字1
     * @param value2 数字2
     * @return 是否大于
     */
    public static boolean isGreater(Double value1, Double value2) {
        Assert.notNull(value1);
        Assert.notNull(value2);
        return value1.compareTo(value2) > 0;
    }

    /**
     * 比较大小，参数1 < 参数2 返回true
     *
     * @param value1 数字1
     * @param value2 数字2
     * @return 是否小于
     */
    public static boolean isLower(Double value1, Double value2) {
        Assert.notNull(value1);
        Assert.notNull(value2);
        return value1.compareTo(value2) < 0;
    }

    /**
     * 比较大小，参数1 < 参数2 返回true
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否小于
     */
    public static boolean isLower(Integer bigNum1, Integer bigNum2) {
        Assert.notNull(bigNum1);
        Assert.notNull(bigNum2);
        return bigNum1.compareTo(bigNum2) < 0;
    }

    /**
     * 比较大小，参数1 >= 参数2 返回true
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否大于等于
     */
    public static boolean isGreaterOrEqual(Long bigNum1, Long bigNum2) {
        Assert.notNull(bigNum1);
        Assert.notNull(bigNum2);
        return bigNum1.compareTo(bigNum2) >= 0;
    }

    /**
     * 是否小于0，只有当小于0才是true，其它全部是false
     *
     * @param value 数字
     * @return 是否大于
     */
    public static boolean isLessThenZero(BigDecimal value) {
        if (value != null && isLess(value, BigDecimal.ZERO)) {
            return true;
        }
        return false;
    }

    /**
     * 比较大小，参数1 >= 参数2 返回true
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否大于等于
     */
    public static boolean isGreaterOrEqual(Integer bigNum1, Integer bigNum2) {
        Assert.notNull(bigNum1);
        Assert.notNull(bigNum2);
        return bigNum1.compareTo(bigNum2) >= 0;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isEqualsZeroOrNull(BigDecimal value) {
        if (value == null) {
            return true;
        }
        return isEquals(value, BigDecimal.ZERO);
    }

    /**
     * 比较两个数值是否相等
     *
     * @param value1 BigDecimal
     * @param value2 BigDecimal
     * @return boolean
     */
    public static boolean isEquals(BigDecimal value1, BigDecimal value2) {
        if (isGreater(value1, value2) || isLess(value1, value2)) {
            return false;
        }

        return true;
    }

    public static boolean isEquals(Long val1, Long val2) {
        if (val1 == null && val2 == null) {
            return true;
        }
        if (val1 == null && val2 != null) {
            return false;
        }
        if (val1 != null && val2 == null) {
            return false;
        }
        return val1.equals(val2);
    }

    /**
     * <p>比较两个浮点数绝对差值是否在范围内，浮点数不要直接用==或者equals来比，要用范围来比</p>
     *
     * <p>用法：boolean isInRange = isDiffInRange(0.1, 0.1, 0);</p>
     *
     * @param value1 值1
     * @param value2 值2
     * @param diff   允许差值，必须大于0
     * @return true则在范围内，反则不在
     */
    public static boolean isDiffInRange(double value1, double value2, double diff) {
        double diffValue = Math.abs(NumberUtil.sub(value1, value2));
        if (diffValue <= diff) {
            return true;
        }
        return false;
    }

    /**
     * <p>比较两个浮点数绝对差值是否在范围内，浮点数不要直接用==或者equals来比，要用范围来比</p>
     *
     * <p>用法：boolean isInRange = isDiffInRange(0.1, 0.1, 0);</p>
     *
     * @param value1 值1
     * @param value2 值2
     * @param diff   允许差值，必须大于0
     * @return true则在范围内，反则不在
     */
    public static boolean isDiffInRange(BigDecimal value1, BigDecimal value2, BigDecimal diff) {
        BigDecimal diffValue = NumberUtil.sub(value1, value2).abs();
        if (NumberUtil.isLessOrEqual(diffValue, diff)) {
            return true;
        }
        return false;
    }

    /**
     * 判断金额是否在范围内
     * <p>
     * 如果范围值是10000，则最小10000，无上限
     * <p>
     * 如果范围值0~99999999，则金额只能是0～99999999之间
     * <p>
     * 如果范围值是0,100,200，则金额只能是0或者100或者200
     * <p>
     * 如果范围值是100~200,500~1000,1200,1500，则金额只能是100～200之间，500～1000之间，或者1200，或者1500
     * <p>
     * 其它非法值一律返回false，注意分割符是英文~和英文,
     * <p>
     * 判断小数位数请使用{@link #isAmount(BigDecimal, boolean, int)}方法
     *
     * @param rangeStr 范围值，格式看上面的解释
     * @param value    金额
     * @return
     */
    public static boolean isBigDecimalInRangeStr(String rangeStr, BigDecimal value) {
        if (!isMultipleAmountRangeStr(rangeStr) || value == null) {
            return false;
        }

        if (isNumber(rangeStr)) {
            // 最小值
            BigDecimal min = Convert.toBigDecimal(rangeStr, null);
            return isGreaterOrEqual(value, min);
        } else if (StrUtil.contains(rangeStr, CommonConstant.RANGE_EN) || StrUtil.contains(rangeStr, CommonConstant.DOT_EN)) {
            String[] rangeStrArr = StrUtil.splitToArray(rangeStr, CommonConstant.DOT_EN);

            for (String rangeStrSub : rangeStrArr) {
                if (isNumber(rangeStrSub)) {
                    BigDecimal bg = Convert.toBigDecimal(rangeStrSub, null);
                    if (isEquals(value, bg)) {
                        return true;
                    }
                } else if (StrUtil.contains(rangeStr, CommonConstant.RANGE_EN)) {
                    // 组合范围
                    String[] rangeStrSubArr = StrUtil.splitToArray(rangeStrSub, CommonConstant.RANGE_EN);
                    BigDecimal min = Convert.toBigDecimal(rangeStrSubArr[0], null);
                    BigDecimal max = Convert.toBigDecimal(rangeStrSubArr[1], null);

                    if (isBetween(min, value, max)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查金额是否在范围内，并检查小数点
     *
     * @param rangeStr
     * @param maxPrecision
     * @param value
     * @return
     */
    public static RestResponse isBigDecimalInRangeStr(String rangeStr, int maxPrecision, BigDecimal value) {
        // 检查金额是否在允许范围内
        if (!isBigDecimalInRangeStr(rangeStr, value)) {
            return RestResponse.failed(ResponseCode.SYS_AMOUNT_NOT_IN_RANGE, rangeStr);
        }

        // 检查金额小数点
        int precision = NumberUtil.getValidPrecision(value);
        if (precision > maxPrecision) {
            if (maxPrecision <= 0) {
                return RestResponse.failed(ResponseCode.SYS_AMOUNT_NO_PRECISION);
            } else {
                return RestResponse.failed(ResponseCode.SYS_AMOUNT_MAX_PRECISION_INCORRECT, maxPrecision);
            }
        }

        return RestResponse.OK;
    }

    /**
     * 是否是多金额范围的格式，如下几种是合法的
     * <p>
     * 10000：最小10000，无上限
     * <p>
     * 0~99999999：范围为0～99999999之间
     * <p>
     * 0,100,200：金额只能是0或者100或者200
     * <p>
     * 100~200,500~1000,1200,1500：金额只能是100～200之间，500～1000之间，或者1200，或者1500
     * <p>
     * 其它非法值一律返回false，注意分割符是英文~和英文,
     * <p>
     *
     * @param rangeStr
     * @return
     */
    public static boolean isMultipleAmountRangeStr(String rangeStr) {
        if (StrUtil.isBlank(rangeStr)) {
            return false;
        }

        if (isNumber(rangeStr)) {
            // 纯数字是合法的
            return true;
        } else if (StrUtil.contains(rangeStr, CommonConstant.RANGE_EN) || StrUtil.contains(rangeStr, CommonConstant.DOT_EN)) {
            // 英文逗号分割
            String[] rangeStrArr = StrUtil.splitToArray(rangeStr, CommonConstant.DOT_EN);
            if (rangeStrArr == null || rangeStrArr.length <= 0) {
                return false;
            }

            for (String rangeStrSub : rangeStrArr) {
                // 只要是数字或者范围值都是合法的
                boolean isNumber = isNumber(rangeStrSub);
                boolean isSingleAmountRangeStr = isSingleAmountRangeStr(rangeStrSub);

                if (!isNumber && !isSingleAmountRangeStr) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 是否是100~49999这种格式
     *
     * @param rangeStr
     * @return
     */
    public static boolean isSingleAmountRangeStr(String rangeStr) {
        if (StrUtil.isBlank(rangeStr)) {
            return false;
        }

        if (!StrUtil.contains(rangeStr, CommonConstant.RANGE_EN)) {
            return false;
        }

        String[] rangeStrArr = StrUtil.splitToArray(rangeStr, CommonConstant.RANGE_EN);

        if (rangeStrArr == null || rangeStrArr.length != 2) {
            return false;
        }

        BigDecimal min = Convert.toBigDecimal(rangeStrArr[0], null);
        BigDecimal max = Convert.toBigDecimal(rangeStrArr[1], null);

        return min != null && max != null;
    }

    /**
     * 是否在中间 middle <= end && middle >= start
     *
     * @param start  起始值
     * @param middle 中间值
     * @param end    结束值
     * @return
     */
    public static boolean isBetween(Integer start, Integer middle, Integer end) {
        if (start == null || middle == null || end == null) {
            return false;
        }

        return middle <= end && middle >= start;
    }

    /**
     * 是否在中间 middle <= end && middle >= start
     *
     * @param start  起始值
     * @param middle 中间值
     * @param end    结束值
     * @return
     */
    public static boolean isBetween(BigDecimal start, BigDecimal middle, BigDecimal end) {
        if (start == null || middle == null || end == null) {
            return false;
        }

        return isLessOrEqual(middle, end) && isGreaterOrEqual(middle, start);
    }

    /**
     * 是否是合法的数组分割字符串，如100,200,300,400
     *
     * @param numberArrStr 字符串
     * @param predicate    判断，一定是有值的
     * @return boolean
     */
    public static boolean isLegalNumberArr(String numberArrStr, Predicate<BigDecimal> predicate) {
        if (StrUtil.isBlank(numberArrStr)) {
            return false;
        }

        String[] numberArr = StrUtil.splitToArray(numberArrStr, CommonConstant.DOT_EN);
        if (numberArr == null || numberArr.length <= 0) {
            return false;
        }

        for (String number : numberArr) {
            BigDecimal bg = Convert.toBigDecimal(number, null);
            if (bg == null) {
                return false;
            }

            if (!predicate.test(bg)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断是否是数字
     *
     * @param numberStr    数字字符串
     * @param negate       true必须是正数，false必须是负数，0都符合
     * @param maxPrecision 最多几位小数
     * @return
     */
    public static boolean isAmount(String numberStr, boolean negate, int maxPrecision) {
        if (!isNumber(numberStr)) {
            return false;
        }

        BigDecimal bigDecimal = Convert.toBigDecimal(numberStr, null);
        if (bigDecimal == null) {
            return false;
        }

        if (negate && !NumberUtil.isGreatThenZero(bigDecimal)) {
            return false;
        }
        if (!negate && NumberUtil.isGreatThenZero(bigDecimal)) {
            return false;
        }

        int numberPrecision = getValidPrecision(bigDecimal);

        return numberPrecision <= maxPrecision;
    }

    /**
     * 判断是否是数字
     *
     * @param amount       数字
     * @param negate       true必须是正数，false必须是负数，0都符合
     * @param maxPrecision 最多几位小数
     * @return
     */
    public static boolean isAmount(BigDecimal amount, boolean negate, int maxPrecision) {
        if (amount == null) {
            return false;
        }

        if (negate && !NumberUtil.isGreatThenZero(amount)) {
            return false;
        }
        if (!negate && NumberUtil.isGreatThenZero(amount)) {
            return false;
        }

        int numberPrecision = getValidPrecision(amount);

        return numberPrecision <= maxPrecision;
    }

    public static BigDecimal formatAmountFromUnknownStr(String amountStr) {
        if (StrUtil.isBlank(amountStr)) {
            return null;
        }

        String numberStr = amountStr.replaceAll("[^\\d-.]", "");
        numberStr = StrUtil.trim(StrUtil.replace(numberStr, ",", ""));
        return Convert.toBigDecimal(numberStr, null);
    }

    /**
     * 获取数值的有效小数点位数，小数点后的0不算，比如1.000=0；1.100=1；1.120=2
     *
     * @param bigDecimal
     * @return
     */
    public static int getValidPrecision(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            return 0;
        }
        String numberStr = NumberUtil.toStr(bigDecimal);

        String numberPrecisionStr = StrUtil.subAfter(numberStr, ".", true);
        int numberPrecision = StrUtil.isBlank(numberPrecisionStr) ? 0 : numberPrecisionStr.length();

        return numberPrecision;
    }

    /**
     * 求a的b次方
     *
     * @param a
     * @param b
     * @return
     */
    public static int power(int a, int b) {
        int power = 1;
        for (int c = 0; c < b; c++) {
            power *= a;
        }
        return power;
    }

    /**
     * 如果是null，则返回0
     *
     * @param value 值
     * @return BigDecimal
     */
    public static BigDecimal nullOrZero(BigDecimal value) {
        return Optional.ofNullable(value).orElse(BigDecimal.ZERO);
    }

    /**
     * 如果是null，则返回0
     *
     * @param value 值
     * @return BigDecimal
     */
    public static Long nullOrZero(Long value) {
        return Optional.ofNullable(value).orElse(CommonConstant.LONG_0);
    }

    /**
     * 如果是null，则返回0
     *
     * @param value 值
     * @return BigDecimal
     */
    public static Integer nullOrZero(Integer value) {
        return Optional.ofNullable(value).orElse(CommonConstant.INT_0);
    }

    /**
     * 如果是null或者小于0，则返回0
     *
     * @param value 值
     * @return BigDecimal
     */
    public static BigDecimal belowZeroOrZero(BigDecimal value) {
        BigDecimal val = nullOrZero(value);

        if (!NumberUtil.isGreatThenZero(val)) {
            return BigDecimal.ZERO;
        }
        return val;
    }

    /**
     * 如果是null或者小于0，则返回0
     *
     * @param value 值
     * @return BigDecimal
     */
    public static Integer belowZeroOrZero(Integer value) {
        Integer val = nullOrZero(value);

        if (!NumberUtil.isGreatThenZero(val)) {
            return CommonConstant.INT_0;
        }
        return val;
    }

    /**
     * 如果是null或者小于0，则返回0
     *
     * @param value 值
     * @return Long
     */
    public static Long belowZeroOrZero(Long value) {
        Long val = nullOrZero(value);

        if (!NumberUtil.isGreatThenZero(val)) {
            return CommonConstant.LONG_0;
        }
        return val;
    }

    /**
     * 把金额元转换为分的字符串，多余的小数会截除
     *
     * @param amountStr 金额，单位元，必须是正数
     * @return
     */
    public static String amountYuanToFenLongStr(String amountStr) {
        BigDecimal amount = Convert.toBigDecimal(amountStr, BigDecimal.ZERO);
        if (amount.longValue() == CommonConstant.LONG_0) {
            return "0";
        }

        BigDecimal multiply = amount.multiply(BigDecimal.valueOf(100d));

        return multiply.longValue() + "";
    }

    /**
     * 把金额元转换为分的字符串，多余的小数会截除
     *
     * @param amount 金额，单位元，必须是正数
     * @return
     */
    public static String amountYuanToFenLongStr(BigDecimal amount) {
        if (amount.longValue() == CommonConstant.LONG_0) {
            return "0";
        }

        BigDecimal multiply = amount.multiply(BigDecimal.valueOf(100d));

        return multiply.longValue() + "";
    }

    /**
     * 浮点数转整数，是截取，不是四舍五入
     *
     * @param amountStr
     * @return
     */
    public static String floatStrToIntStr(String amountStr) {
        if (!isNumber(amountStr)) {
            return null;
        }

        BigDecimal amount = Convert.toBigDecimal(amountStr, BigDecimal.ZERO);

        return amount.longValue() + "";
    }

    /**
     * 是否是不限制的数字
     *
     * @param value 数字
     * @return boolean
     */
    public static boolean isUnlimited(Long value) {
        if (value != null && value <= CommonConstant.UNLIMITED_LONG) {
            return true;
        }
        return false;
    }

    /**
     * 是否是不限制的数字
     *
     * @param value 数字
     * @return boolean
     */
    public static boolean isUnlimited(Double value) {
        if (value != null && value <= CommonConstant.UNLIMITED_DOUBLE) {
            return true;
        }
        return false;
    }

    /**
     * 是否是不限制的数字
     *
     * @param value 数字
     * @return boolean
     */
    public static boolean isUnlimited(Integer value) {
        if (value != null && value <= CommonConstant.UNLIMITED_INT) {
            return true;
        }
        return false;
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被乘值
     * @return 积
     * @since 4.0.0
     */
    public static int mul(List<Integer> values) {
        if (CollectionUtil.isEmpty(values)) {
            return CommonConstant.INT_0;
        }

        int result = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            result = result * values.get(i);
        }
        return result;
    }

    public static String sortIntStr(String intStr) {
        if (!isInteger(intStr)) {
            return intStr;
        }

        String[] intArr = StrSplitter.splitByLength(intStr, 1);
        List<String> sortedList = CollectionUtil.sort(ListUtil.toList(intArr), Comparator.comparingInt(Integer::new));

        return IterUtil.join(sortedList.listIterator(), StrUtil.EMPTY);
    }

    public static BigDecimal subPercent(BigDecimal value, BigDecimal percent) {
        BigDecimal percentValue = NumberUtil.mul(value, percent);
        return NumberUtil.sub(value, percentValue);
    }

    public static BigDecimal addPercent(BigDecimal value, BigDecimal percent) {
        BigDecimal percentValue = NumberUtil.mul(value, percent);
        return NumberUtil.add(value, percentValue);
    }
}
