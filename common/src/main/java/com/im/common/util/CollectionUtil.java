package com.im.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrSplitter;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.im.common.constant.CommonConstant;
import com.im.common.entity.enums.BaseEnum;
import com.im.common.util.mybatis.page.AbstractPageParam;
import com.im.common.util.mybatis.page.PageVO;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 集合工具类,本类部分代码采用Lambda语法
 * 需要注意的是,Lambda虽然优雅,但性能相比传统非常低(低很多倍),在对性能要求较高的地方,还是要用命令式风格来编写
 * 如果不熟悉Lambda,那可以不使用,尽量不留坑
 * <p>
 * 在进行大的集合循环操作时效率对比:
 * stream:串行
 * parallelStream:并行,效率比stream稍微高一点,但也要看情况
 * for-each:很快,比lambda快很多倍
 * iterator:最快,比lambda快很多倍
 *
 * @author Barry
 * @date 2018/5/12
 * @since JDK1.8
 */
public final class CollectionUtil extends CollUtil {
    /**
     * 将一个list随机打乱,直接调用Collections.shuffle
     *
     * @param list 源集合
     */
    public static void shuffle(List<?> list) {
        Collections.shuffle(list);
    }

    /**
     * 随机打乱数组
     *
     * @param arr
     * @param <T>
     */
    public static <T> void shuffleArray(T[] arr) {
        Random rand = new Random();

        int length = arr.length;
        for (int i = length; i > 0; i--) {
            int randInd = rand.nextInt(i);
            ArrayUtil.swap(arr, randInd, i - 1);
        }
    }

    /**
     * 判断一个数组是否是null或length<=0
     *
     * @param objs 数组
     * @return boolean
     */
    public static boolean isEmpty(Object[] objs) {
        return objs == null || objs.length <= 0;
    }

    /**
     * 取出集合中某个字段的最小值,集合中的数据类型不限制,性能不高
     * 使用：AdminUser min = min(list, AdminUser::getId);
     *
     * @param c      集合
     * @param mapper 取字段的映射
     * @param <T>    集合数据类型
     * @param <U>    取哪个字段作为比较字段
     * @return 最小值
     */
    public static <T, U extends Comparable<? super U>> T min(Collection<T> c, Function<? super T, ? extends U> mapper) {
        return min(c, null, mapper);
    }

    /**
     * 取出集合中某个字段的最小值对应对象,集合中的数据类型不限制,性能不高
     * 使用：AdminUser min = min(list, AdminUser::getId);
     *
     * @param c      集合
     * @param filter 过滤条件
     * @param mapper 取字段的映射
     * @param <T>    集合数据类型
     * @param <U>    取哪个字段作为比较字段
     * @return 最小值
     */
    public static <T, U extends Comparable<? super U>> T min(Collection<T> c, Predicate<T> filter, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(c, "集合不能为空");

        Stream<T> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        return stream.min(Comparator.comparing(mapper)).get();
    }

    /**
     * 取出集合中某个字段的最大值,集合中的数据类型不限制,性能不高
     * 使用：AdminUser max = max(list, AdminUser::getId);
     *
     * @param c      集合
     * @param mapper 取字段的映射
     * @param <T>    集合数据类型
     * @param <U>    取哪个字段作为比较字段
     * @return 最大值
     */
    public static <T, U extends Comparable<? super U>> T max(Collection<T> c, Function<? super T, ? extends U> mapper) {
        return max(c, null, mapper);
    }

    /**
     * 取出集合中某个字段的最大值,集合中的数据类型不限制,性能不高
     * 使用：AdminUser max = max(list, AdminUser::getId);
     *
     * @param c      集合
     * @param filter 过滤条件
     * @param mapper 取字段的映射
     * @param <T>    集合数据类型
     * @param <U>    取哪个字段作为比较字段
     * @return 最大值
     */
    public static <T, U extends Comparable<? super U>> T max(Collection<T> c, Predicate<T> filter, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(c, "集合不能为空");

        Stream<T> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        return stream.max(Comparator.comparing(mapper)).get();
    }

    /**
     * 给定范围内的整数列表
     *
     * @param start  开始（包含）
     * @param stop   结束（包含）
     * @param step   步进
     * @param filter 过滤条件
     * @return 整数列表
     */
    public static List<Long> rangeIntegerArr(long start, long stop, int step, Predicate<Long> filter) {
        List<Long> values = new LinkedList<>();

        if (start < stop) {
            step = Math.abs(step);
        } else if (start > stop) {
            step = -Math.abs(step);
        } else {
            values.add(start);
            return values;
        }

        for (long i = start; (step > 0) ? i <= stop : i >= stop; i += step) {
            values.add(i);
        }

        if (isNotEmpty(values)) {
            return getStream(values).filter(filter).collect(Collectors.toList());
        }

        return values;
    }

    /**
     * 集合转成Map,性能不高,key不允许重复
     * 使用：Map<String, AdminUser> stringAdminUserMap = toMap(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <K, V> Map<K, V> toMapWithKey(Collection<V> c, Function<V, K> keyMapper) {
        return toMapWithKey(c, null, keyMapper);
    }

    /**
     * 集合转成Map,性能不高,key不允许重复
     * 使用：Map<String, AdminUser> stringAdminUserMap = toMap(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param filter    过滤条件
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <K, V> Map<K, V> toMapWithKey(Collection<V> c, Predicate<V> filter, Function<V, K> keyMapper) {
        if (isEmpty(c)) {
            return MapUtil.newHashMap(0);
        }

        Stream<V> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        return stream.collect(Collectors.toMap(keyMapper, v -> v));
    }

    /**
     * 集合转成Map,性能不高,key不允许重复
     * 使用：Map<String, AdminUser> stringAdminUserMap = toMap(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <V, NK, NV> Map<NK, NV> toMapWithKeyValue(Collection<V> c, Function<V, NK> keyMapper, Function<V, NV> valueMapper) {
        return toMapWithKeyValue(c, null, keyMapper, valueMapper);
    }

    /**
     * 集合转成Map,性能不高,key不允许重复
     * 使用：Map<String, String> stringAdminUserMap = toMap(list, AdminUser::getUsername, AdminUser::getNickname);
     *
     * @param c         集合
     * @param filter    过滤条件
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <V, NK, NV> Map<NK, NV> toMapWithKeyValue(Collection<V> c, Predicate<V> filter, Function<V, NK> keyMapper, Function<V, NV> valueMapper) {
        if (isEmpty(c)) {
            return MapUtil.newHashMap(0);
        }

        Stream<V> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        return stream.collect(Collectors.toMap(keyMapper, valueMapper));
    }

    /**
     * 集合转成Map,性能不高
     * 使用：Map<String, List<AdminUser>> stringAdminUserMap = toMapList(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <K, V> Map<K, List<V>> toMapList(Collection<V> c, Function<V, K> keyMapper) {
        return toMapList(c, null, keyMapper);
    }

    /**
     * 集合转成Map,性能不高
     * 使用：Map<String, List<AdminUser>> stringAdminUserMap = toMapList(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param filter    过滤条件
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <K, V> Map<K, List<V>> toMapList(Collection<V> c, Predicate<V> filter, Function<V, K> keyMapper) {
        if (isEmpty(c)) {
            return MapUtil.newHashMap(2);
        }

        Stream<V> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        return stream.collect(Collectors.groupingBy(keyMapper));
    }

    /**
     * 集合转成Map,性能不高
     * 使用：Map<String, List<AdminUser>> stringAdminUserMap = toMapList(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <K, V, T> Map<K, Set<V>> toMapSet(Collection<T> c, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        return toMapSet(c, null, keyMapper, valueMapper);
    }

    /**
     * 集合转成Map,性能不高
     * 使用：Map<String, List<AdminUser>> stringAdminUserMap = toMapList(list, AdminUser::getUsername);
     *
     * @param c         集合
     * @param filter    过滤条件
     * @param keyMapper Key表达式
     * @return 返回Map
     */
    public static <K, V, T> Map<K, Set<V>> toMapSet(Collection<T> c, Predicate<T> filter, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (isEmpty(c)) {
            return MapUtil.newHashMap(2);
        }

        Stream<T> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        Map<K, List<T>> mapList = toMapList(c, filter, keyMapper);
        Map<K, Set<V>> mapSet = new HashMap<>(mapList.size());

        mapList.forEach((k, v) -> mapSet.put(k, CollectionUtil.toSet(v, valueMapper)));

        return mapSet;
    }

    /**
     * 集合转成Set,性能不高
     * 使用：Set<String> usernames = toSet(list, AdminUser::getUsername);
     *
     * @param c      集合
     * @param mapper 表达式
     * @return set
     */
    public static <T, R> Set<R> toSet(Collection<T> c, Function<T, R> mapper) {
        return toSet(c, null, mapper);
    }

    /**
     * 集合转成Set,性能不高
     * 使用：Set<String> usernames = toSet(list, AdminUser::getUsername);
     *
     * @param c      集合
     * @param filter 过滤条件
     * @param mapper 表达式
     * @return set
     */
    public static <T, R> Set<R> toSet(Collection<T> c, Predicate<T> filter, Function<T, R> mapper) {
        if (isEmpty(c)) {
            return new HashSet<>();
        }

        Stream<T> stream = getStream(c);
        if (filter != null) {
            stream = stream.filter(filter);
        }

        return stream.map(mapper).collect(Collectors.toSet());
    }

    /**
     * 集合转成Set
     * 使用：Set<String> usernames = toSet(obj1, obj2);
     *
     * @param c 集合
     * @return set
     */
    public static <T> Set<T> toSet(T... c) {
        if (isEmpty(c)) {
            return new HashSet<>();
        }

        Stream<T> stream = Stream.of(c);

        return stream.collect(Collectors.toSet());
    }

    /**
     * 集合转成另一个集合,性能不高
     * 使用：List<String> usernames = toList(list, AdminUser::getUsername);
     * 使用：List<AdminUserSessionVO> sessionVOS = toList(list, AdminUserSessionVO::new);
     *
     * @param c      集合
     * @param filter 过滤
     * @param mapper 表达式
     * @param <T>
     * @param <R>
     * @return 集合
     */
    public static <T, R> List<R> toList(Collection<T> c, Predicate<R> filter, Function<T, R> mapper) {
        if (isEmpty(c)) {
            return new ArrayList<>();
        }

        return getStream(c).map(mapper).filter(filter).collect(Collectors.toList());
    }

    /**
     * 集合转成另一个集合,性能不高
     * 使用：List<String> usernames = toList(list, AdminUser::getUsername);
     * 使用：List<AdminUserSessionVO> sessionVOS = toList(list, AdminUserSessionVO::new);
     *
     * @param c      集合
     * @param filter 过滤
     * @param mapper 表达式
     * @param <T>
     * @param <R>
     * @return 集合
     */
    public static <T, R> List<R> toListFilterByOriginal(Collection<T> c, Predicate<T> filter, Function<T, R> mapper) {
        if (isEmpty(c)) {
            return new ArrayList<>();
        }

        return getStream(c).filter(filter).map(mapper).collect(Collectors.toList());
    }

    /**
     * 集合转成另一个集合,性能不高
     * 使用：List<String> usernames = toList(list, AdminUser::getUsername);
     * 使用：List<AdminUserSessionVO> sessionVOS = toList(list, AdminUserSessionVO::new);
     *
     * @param c      集合
     * @param mapper 表达式
     * @return 集合
     */
    public static <T, R> List<R> toList(Collection<T> c, Function<T, R> mapper) {
        if (isEmpty(c)) {
            return new ArrayList<>();
        }

        return getStream(c).map(mapper).collect(Collectors.toList());
    }

    public static <T> List<T> filterList(Collection<T> c, Predicate<T> filter) {
        if (isEmpty(c)) {
            return new ArrayList<>();
        }

        return getStream(c).filter(filter).collect(Collectors.toList());
    }

    public static <T> Set<T> filterSet(Collection<T> c, Predicate<T> filter) {
        if (isEmpty(c)) {
            return new HashSet<>();
        }

        return getStream(c).filter(filter).collect(Collectors.toSet());
    }

    // /**
    //  * <p>将元素去重，参数为空时返回<code>new ArrayList</code></p>
    //  *
    //  * <p>使用：List<Integer> ids = distinct(list);</p>
    //  * @param c 集合，为空时返回
    //  * @return 已去重的集合
    //  */
    // public static <T> List<T> distinct(Collection<T> c) {
    //     if (isEmpty(c)) {
    //         return new ArrayList<>();
    //     }
    //
    //     return getStream(c).distinct().collect(Collectors.toList());
    // }

    /**
     * <p>集合按指定字段去重，参数为空时返回<code>new ArrayList</code></p>
     *
     * <p>使用：List<AdminUser> adminUsers = distinctBy(list, AdminUser::getUsername);</p>
     *
     * @param c      集合
     * @param mapper 表达式
     * @return 已去重的集合
     */
    public static <T, R> List<T> distinctBy(Collection<T> c, Function<T, R> mapper) {
        if (isEmpty(c)) {
            return new ArrayList<>();
        }

        return getStream(c).filter(distinctByKey(mapper)).collect(Collectors.toList());
    }

    /**
     * 根据指定的过滤条件返回一个Predicate
     *
     * @param keyExtractor lambda过滤条件
     * @param <T>
     * @return Predicate
     */
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * 求出set2->set1的差集，也就是set2不在set1中的
     * <p>
     * 如set1: [1,2,3,4]；set2: [2,3,4,5]；结果：[5]
     *
     * @param set1 集合1
     * @param set2 集合2
     * @param <T>
     * @return 差集
     */
    public static <T> Set<T> difference(Set<T> set1, Set<T> set2) {
        return set2.stream().filter(item2 -> !set1.contains(item2)).collect(Collectors.toSet());
    }

    /**
     * 求出list2->list1的差集，也就是list2不在list1中的
     * <p>
     * 如list1: [1,2,3,4]；list2: [2,3,4,5]；结果：[5]
     *
     * @param list1 集合1
     * @param list2 集合2
     * @param <T>
     * @return 差集
     */
    public static <T> Set<T> difference(List<T> list1, List<T> list2) {
        return difference(new HashSet<>(list1), new HashSet<>(list2));
    }

    /**
     * <p>根据某个字段值进行map分组</p>
     *
     * <p>使用：Map<Long, List<AdminUserRole>> map = CollectionUtil.groupingBy(adminUserRoles, AdminUserRole::getAdminId);</p>
     *
     * @param c          集合
     * @param classifier 要分组的字段
     * @param <T>        集合类型
     * @param <K>        值的类型
     * @return 分组
     */
    public static <T, K> Map<K, List<T>> groupingBy(Collection<T> c, Function<? super T, ? extends K> classifier) {
        if (isEmpty(c)) {
            return MapUtil.newHashMap(2);
        }

        return getStream(c).collect(Collectors.groupingBy(classifier));
    }

    /**
     * <p>统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：double sum = sumDouble(list, XXX::getDoubleField);</p>
     *
     * @param c      集合
     * @param mapper 表达式
     * @return 和值
     */
    public static <T> double sumDouble(Collection<T> c, Function<? super T, Double> mapper) {
        if (isEmpty(c)) {
            return CommonConstant.DOUBLE_0;
        }

        return getStream(c).map(mapper).mapToDouble(Double::doubleValue).sum();
    }

    /**
     * <p>分组统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：Map<String, Double> sumMap = </String,> = sumDouble(list, XXX::getName, XXX::getDoubleField);</p>
     *
     * @param c         集合
     * @param keyMapper key表达式
     * @param keyMapper valueMapper
     * @return HashMap
     */
    public static <T, K> Map<K, Double> groupSumDouble(Collection<T> c, Function<T, K> keyMapper, ToDoubleFunction<T> valueMapper) {
        if (isEmpty(c)) {
            return new HashMap<>(0);
        }

        return getStream(c).collect(Collectors.groupingBy(keyMapper,
                Collectors.summingDouble(valueMapper)));
    }

    /**
     * <p>统计集合中的某个字段的和值,性能不高</p>
     *
     * <p>使用：long sum = sumLong(list, XXX::getLongField);</p>
     *
     * @param c      集合
     * @param mapper 表达式
     * @return 和值
     */
    public static <T> long sumLong(Collection<T> c, Function<? super T, Long> mapper) {
        if (isEmpty(c)) {
            return CommonConstant.LONG_0;
        }

        return getStream(c).map(mapper).mapToLong(Long::longValue).sum();
    }

    /**
     * <p>分组统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：Map<String, Long> sumMap = </String,> = sumDouble(list, XXX::getName, XXX::getLongField);</p>
     *
     * @param c         集合
     * @param keyMapper key表达式
     * @param keyMapper valueMapper
     * @return HashMap
     */
    public static <T, K> Map<K, Long> groupSumLong(Collection<T> c, Function<T, K> keyMapper, ToLongFunction<T> valueMapper) {
        if (isEmpty(c)) {
            return new HashMap<>(0);
        }

        return getStream(c).collect(Collectors.groupingBy(keyMapper,
                Collectors.summingLong(valueMapper)));
    }

    /**
     * <p>统计集合中的某个字段的和值,性能不高</p>
     *
     * <p>使用：int sum = sumInteger(list, XXX::getIntField);</p>
     *
     * @param c      集合
     * @param mapper 表达式
     * @return 和值
     */
    public static <T> int sumInteger(Collection<T> c, Function<? super T, Integer> mapper) {
        if (isEmpty(c)) {
            return 0;
        }

        return getStream(c).map(mapper).mapToInt(Integer::intValue).sum();
    }

    /**
     * <p>分组统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：Map<String, Integer> sumMap = </String,> = sumDouble(list, XXX::getName, XXX::getIntegerField);</p>
     *
     * @param c         集合
     * @param keyMapper key表达式
     * @param keyMapper valueMapper
     * @return HashMap
     */
    public static <T, K> Map<K, Integer> groupSumInteger(Collection<T> c, Function<T, K> keyMapper, ToIntFunction<T> valueMapper) {
        if (isEmpty(c)) {
            return new HashMap<>(0);
        }

        return getStream(c).collect(Collectors.groupingBy(keyMapper,
                Collectors.summingInt(valueMapper)));
    }

    /**
     * <p>统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：double sum = sumDouble(list, XXX::getDoubleField);</p>
     *
     * @param c      集合
     * @param filter 过滤
     * @param mapper 表达式
     * @param <T>
     * @return 和值
     */
    public static <T> BigDecimal sumBigDecimal(Collection<T> c, Predicate<? super T> filter, Function<? super T, BigDecimal> mapper) {
        if (isEmpty(c)) {
            return CommonConstant.BIG_DECIMAL_0;
        }

        List<BigDecimal> values = getStream(c).filter(filter).map(mapper).collect(Collectors.toList());

        return NumberUtil.add(values);
    }

    /**
     * <p>统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：double sum = sumDouble(list, XXX::getDoubleField);</p>
     *
     * @param c      集合
     * @param mapper 表达式
     * @return 和值
     */
    public static <T> BigDecimal sumBigDecimal(Collection<T> c, Function<? super T, BigDecimal> mapper) {
        if (isEmpty(c)) {
            return CommonConstant.BIG_DECIMAL_0;
        }

        List<BigDecimal> values = getStream(c).map(mapper).collect(Collectors.toList());

        return NumberUtil.add(values);
    }

    /**
     * <p>统计Map中的value的和值，性能不高</p>
     *
     * <p>使用：double sum = sumMapBigDecimal(map);</p>
     *
     * @param map Map
     * @return 和值
     */
    public static BigDecimal sumMapBigDecimal(Map<?, BigDecimal> map) {
        if (isEmpty(map)) {
            return CommonConstant.BIG_DECIMAL_0;
        }

        Collection<BigDecimal> values = map.values();

        return NumberUtil.add(values);
    }

    /**
     * <p>分组统计集合中的某个字段的和值，性能不高</p>
     *
     * <p>使用：Map<String, BigDecimal> sumMap = </String,> = sumDouble(list, XXX::getName, XXX::getBigDecimalField);</p>
     *
     * @param c         集合
     * @param keyMapper key表达式
     * @param keyMapper valueMapper
     * @return HashMap
     */
    public static <T, K> Map<K, BigDecimal> groupSumBigDecimal(Collection<T> c, Function<T, K> keyMapper, Function<T, BigDecimal> valueMapper) {
        if (isEmpty(c)) {
            return new HashMap<>(0);
        }

        Map<K, List<T>> groupMap = toMapList(c, keyMapper);

        Map<K, BigDecimal> resultMap = new HashMap<>(groupMap.size());

        groupMap.forEach((key, list) -> {
            List<BigDecimal> values = getStream(list).map(valueMapper).collect(Collectors.toList());
            BigDecimal sum = NumberUtil.add(values);
            resultMap.put(key, sum);
        });

        return resultMap;
    }

    /**
     * 查找集合中是否存在某个条件的数据
     * 使用：anyMatch(list, obj -> obj.getId() == id);
     *
     * @param c         集合,为空时返回false
     * @param predicate 匹配条件
     * @return true:存在; false; 不存在
     */
    public static <T> boolean anyMatch(Collection<T> c, Predicate<? super T> predicate) {
        if (isEmpty(c)) {
            return false;
        }

        return getStream(c).anyMatch(predicate);
    }

    /**
     * 查找集合中是否全部是某个条件
     * 使用：allMatch(list, obj -> obj.getId() == id);
     *
     * @param c         集合,为空时返回false
     * @param predicate 匹配条件
     * @return true:全部匹配; false; 有不匹配的
     */
    public static <T> boolean allMatch(Collection<T> c, Predicate<? super T> predicate) {
        if (isEmpty(c)) {
            return false;
        }
        return getStream(c).allMatch(predicate);
    }

    /**
     * 查找集合中匹配到的第一个元素
     *
     * @param c         集合
     * @param predicate filter表达式
     * @param <T>       任意类型
     * @return 查找不到null，查找到指定类型实体
     */
    public static <T> T findFirst(Collection<T> c, Predicate<? super T> predicate) {
        if (isEmpty(c)) {
            return null;
        }

        Optional<T> first = getStream(c).filter(predicate).findFirst();

        return first.orElse(null);
    }

    /**
     * 查找集合中匹配到的第一个元素的下标
     *
     * @param c         集合
     * @param predicate filter表达式
     * @param <T>       任意类型
     * @return 查找不到-1，查找到指定目标的下标，0开始
     */
    public static <T> int findFirstIndex(Collection<T> c, Predicate<? super T> predicate) {
        if (isEmpty(c)) {
            return -1;
        }

        int index = -1;
        for (T t : c) {
            index++;
            if (predicate.test(t)) {
                return index;
            }
        }

        return -1;
    }

    /**
     * 检查指定值是否在列表中，如query=100，strList=100,200,300,400，则query在strList中存在
     *
     * @param query   要查询的值
     * @param strList 值列表，多个使用英文逗号分割
     * @return
     */
    public static boolean isInStringList(String query, String strList) {
        return isInStringList(query, strList, ",");
    }

    /**
     * 检查指定值是否在列表中，如query=100，strList=100,200,300,400，则query在strList中存在
     *
     * @param query            要查询的值
     * @param strList          值列表
     * @param strListSeparator 值列表分割符
     * @return
     */
    public static boolean isInStringList(String query, String strList, String strListSeparator) {
        if (StrUtil.isBlank(query) || StrUtil.isBlank(strList)) {
            return false;
        }

        Set<String> strSet = splitStrToSet(strList, strListSeparator);
        return strSet.contains(query);
    }

    /**
     * 切分字符串
     *
     * @param str 被切分的字符串
     * @return 字符串
     */
    public static Set<String> splitStrToSet(CharSequence str) {
        return splitStrToSet(str, CommonConstant.DOT_EN);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 字符串
     */
    public static Set<String> splitStrToSet(CharSequence str, CharSequence separator) {
        if (str == null) {
            return new HashSet<>();
        }

        final String separatorStr = (separator == null) ? null : separator.toString();
        String[] array = StrSplitter.splitToArray(str.toString(), separatorStr, 0, false, false);

        return new HashSet<>(Arrays.asList(array));
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符
     * @return 字符串
     */
    public static List<String> splitStrToList(CharSequence str, CharSequence separator) {
        if (str == null) {
            return new ArrayList<>();
        }

        final String separatorStr = (separator == null) ? null : separator.toString();
        String[] array = StrSplitter.splitToArray(str.toString(), separatorStr, 0, false, false);

        return new ArrayList<>(Arrays.asList(array));
    }

    // /**
    //  * 以 conjunction 为分隔符将数组转换为字符串
    //  *
    //  * @param conjunction 分隔符
    //  * @param ignoreEmpty 是否忽略空或null值
    //  * @param array       数组
    //  * @return 连接后的字符串
    //  */
    // public static String join(CharSequence conjunction, boolean ignoreEmpty, Object... array) {
    //     if (array == null) {
    //         return null;
    //     }
    //
    //     final StringBuilder sb = new StringBuilder();
    //     boolean isFirst = true;
    //     for (Object item : array) {
    //         if (ignoreEmpty) {
    //             if (item == null || StrUtil.isBlankIfStr(item)) {
    //                 continue;
    //             }
    //         }
    //
    //         if (isFirst) {
    //             isFirst = false;
    //         } else {
    //             sb.append(conjunction);
    //         }
    //         if (ArrayUtil.isArray(item)) {
    //             sb.append(join(conjunction, ignoreEmpty, ArrayUtil.wrap(item)));
    //         } else if (item instanceof Iterable<?>) {
    //             sb.append(IterUtil.join((Iterable<?>) item, conjunction));
    //         } else if (item instanceof Iterator<?>) {
    //             sb.append(IterUtil.join((Iterator<?>) item, conjunction));
    //         } else {
    //             sb.append(StrUtil.wrap(StrUtil.toString(item), null, null));
    //         }
    //     }
    //     return sb.toString();
    // }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     *
     * @param c
     * @param valueMapper
     * @param <T>         集合元素类型
     * @return 连接后的字符串
     */
    public static <T> String join(Collection<T> c, Function<T, Object> valueMapper) {
        if (isEmpty(c)) {
            return null;
        }

        return IterUtil.join(getStream(c).map(valueMapper).iterator(), ",");
    }

    public static String join(String[] arr) {
        if (isEmpty(arr)) {
            return null;
        }
        return IterUtil.join(Arrays.stream(arr).iterator(), ",");
    }

    public static String joinWithoutBlank(String[] arr) {
        if (isEmpty(arr)) {
            return null;
        }
        return IterUtil.join(Arrays.stream(arr).filter(e -> StrUtil.isNotBlank(e)).iterator(), ",");
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     *
     * @param c
     * @param valueMapper
     * @param <T>         集合元素类型
     * @return 连接后的字符串
     */
    public static <T> String joinWithoutRepeat(Collection<T> c, Function<T, String> valueMapper) {
        if (isEmpty(c)) {
            return null;
        }

        Set<String> set = toSet(c, valueMapper);

        return IterUtil.join(set.iterator(), ",");
    }

    /**
     * <p>判断数组中的字符串是否都存在于set中</p>
     *
     * <p>比如</p>
     *
     * <code>
     * String[] strArr = new String[]{"1", "2", "3", "4"};
     * Set<String> strSet = CollectionUtil.toSet("1", "2", "3");
     * <p>
     * boolean result = isStrArrInSet(strArr, strSet);
     * <p>
     * result会是false
     * </code>
     *
     * @param strArr 数组字符串
     * @param strSet 范围set
     * @return boolean
     */
    public static boolean isStrArrInSet(String[] strArr, Set<String> strSet) {
        if (isEmpty(strArr) || isEmpty(strSet)) {
            return false;
        }

        for (String s : strArr) {
            if (!strSet.contains(s)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>判断数组中的字符串是否都存在于set中</p>
     *
     * <p>比如</p>
     *
     * <code>
     * String[] strArr = new String[]{"1", "2", "3", "4"};
     * Set<String> strSet = CollectionUtil.toSet("1", "2", "3");
     * <p>
     * boolean result = isStrArrInSet("1,2,3", ",", strSet);
     * <p>
     * result会是false
     * </code>
     *
     * @param str          数组字符串
     * @param strSeparator 数组字符串的分割符，可以为空或空字符串
     * @param strSet       范围set
     * @return boolean
     */
    public static boolean isStrArrInSet(String str, String strSeparator, Set<String> strSet) {
        if (StrUtil.isBlank(str) || isEmpty(strSet)) {
            return false;
        }

        String[] strArr;
        if (StrUtil.isBlank(strSeparator)) {
            strArr = StrUtil.split(str, 1);
        } else {
            strArr = StrUtil.splitToArray(str, strSeparator);
        }

        for (String s : strArr) {
            if (!strSet.contains(s)) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>判断字符串str是否都存在于strArr中</p>
     *
     * <p>比如</p>
     *
     * <code>
     * String str = "100";
     * String strArr = "100,200,500,1000";
     * <p>
     * boolean result = isStrInStrArr(str, ",", strArr);
     * <p>
     * result会是false
     * </code>
     *
     * @param str          单个字符串
     * @param strArr       字符串数组
     * @param strSeparator 字符串数组的分割符，可以为空或空字符串
     * @return boolean
     */
    public static boolean isStrInStrArr(String str, String strArr, String strSeparator) {
        Set<String> strArrSet = splitStrToSet(strArr, strSeparator);

        return isNotEmpty(strArrSet) && strArrSet.contains(str);
    }

    /**
     * <p>判断数值字符串str是否都存在于strArr中</p>
     *
     * <p>比如</p>
     *
     * <code>
     * String str = "100";
     * String strArr = "100,200,500,1000";
     * <p>
     * boolean result = isStrInStrArr(str, ",", strArr);
     * <p>
     * result会是false
     * </code>
     *
     * @param amount       金额
     * @param strArr       字符串数组
     * @param strSeparator 字符串数组的分割符，可以为空或空字符串
     * @return boolean
     */
    public static boolean isNumberStrInNumberStrArr(BigDecimal amount, String strArr, String strSeparator) {
        Set<String> strArrSet = splitStrToSet(strArr, strSeparator);

        for (String numberStr : strArrSet) {
            BigDecimal bg = Convert.toBigDecimal(numberStr, null);
            if (bg == null) {
                return false;
            }

            if (NumberUtil.isEquals(bg, amount)) {
                return true;
            }
        }

        return false;
    }

    /**
     * <p>判断double是否都存在于doubleStrArr中</p>
     *
     * <p>比如</p>
     *
     * <code>
     * Double value = 100d;
     * String doubleStrArr = "100,200,500,1000";
     * <p>
     * boolean result = isDoubleInDoubleStrArr(value, doubleStrArr, ",");
     * <p>
     * result会是false
     * </code>
     *
     * @param value        单个数值
     * @param doubleStrArr 数值数组
     * @param strSeparator 数值数组的分割符，可以为空或空字符串
     * @return boolean
     */
    public static boolean isDoubleInDoubleStrArr(Double value, String doubleStrArr, String strSeparator) {
        Set<String> strArrSet = splitStrToSet(doubleStrArr, strSeparator);
        if (isEmpty(strArrSet)) {
            return false;
        }

        Set<Double> doubleArrSet = toSet(strArrSet, val -> Double.valueOf(val));

        boolean contains = false;
        for (Double arrDouble : doubleArrSet) {
            if (NumberUtil.isDiffInRange(arrDouble, value, 0.01)) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * 判断数组中是否有重复字符串
     *
     * @param strArr 数组字符串
     * @return boolean
     */
    public static boolean hasRepeatForStrArr(String[] strArr) {
        Set<String> strings = toSet(strArr);
        return strings.size() != strArr.length;
    }

    /**
     * 内存分页，分页的参数与AbstractPageParam一样，里面只需要有current和size就可以了
     *
     * @param c         所有数据的集合
     * @param pageParam 分页参数
     * @param <T>       类型
     * @return PageVO
     */
    public static <T> PageVO<T> memoryPageVo(Collection<T> c, AbstractPageParam<?> pageParam) {
        return memoryPageVo(c, pageParam.getCurrent().intValue(), pageParam.getSize().intValue());
    }

    /**
     * 内存分页，分页的参数与AbstractPageParam一样，里面只需要有current和size就可以了
     *
     * @param c       所有数据的集合
     * @param current 当前页，1是第1页
     * @param size    每页大小
     * @param <T>     类型
     * @return PageVO
     */
    public static <T> PageVO<T> memoryPageVo(Collection<T> c, int current, int size) {
        int start = (current - 1) * size;
        int end = start + size;

        // 分页
        List<T> list = CollectionUtil.sub(c, start, end);

        long total = c.size();
        long pages;
        if (total > 0 && total <= size) {
            pages = 1;
        } else {
            pages = total / size;
            pages += (total % size > 1) ? 1 : 0;
        }


        PageVO<T> pageVO = new PageVO<>();
        pageVO.setTotal(total);
        pageVO.setPages(pages);
        pageVO.setCurrent((long) current);
        pageVO.setSize((long) size);
        pageVO.setRecords(list);

        return pageVO;
    }

    /**
     * 将集合均等分配为n组
     *
     * @param list 集合
     * @param n    均等分为多少组
     * @param <T>
     * @return List<List < T>>
     */
    public static <T> List<List<T>> averageAssign(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        //(先计算出余数)
        int remainder = list.size() % n;
        // 然后是商
        int number = list.size() / n;
        // 偏移量
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remainder > 0) {
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

    /**
     * 对集合按照指定长度分段，每一个段为单独的集合，返回这个集合的列表
     * <p>
     * 性能比父类的split要高，但是每个子集合的修改会影响主集合
     *
     * @param <T>  集合元素类型
     * @param list 集合
     * @param size 每个段的长度
     * @return 分段列表
     */
    public static <T> List<List<T>> splitBySubList(List<T> list, int size) {
        final List<List<T>> result = new ArrayList<>();
        if (CollectionUtil.isEmpty(list)) {
            return result;
        }

        if (list.size() <= size) {
            result.add(list);
            return result;
        }

        int groupCount = list.size() / size;
        if (list.size() % size > 0) {
            groupCount++;
        }

        for (int i = 0; i < groupCount; i++) {
            int fromIndex = i * size;
            int toIndex = fromIndex + size;
            if (toIndex > list.size()) {
                toIndex = list.size();
            }
            List<T> subList = list.subList(fromIndex, toIndex);
            if (CollectionUtil.isNotEmpty(subList)) {
                result.add(subList);
            }
        }
        return result;
    }

    /**
     * 随机从list中选择一个
     *
     * @param list 集合
     * @param <T>  类型
     * @return
     */
    public static <T> T randomSelect(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }

        int randomIndex = RandomUtil.randomInt(list.size());
        return list.get(randomIndex);
    }

    /**
     * 从start~end组成set，并去除组合重的，比如0～99中，01跟10是一样的，02跟20是一样的，03跟30是一样的，78跟87是一样的以此类推
     * 0～99共计55个数字
     * 0~999共计220个数字
     *
     * @param start 从几开始
     * @param end   到几结束
     * @return
     */
    public static Set<String> rangeZuHeStrSet(int start, int end) {
        Set<String> set = new TreeSet<>();
        int length = (end + "").length();

        for (int i = start; i <= end; i++) {
            String num = StrUtil.padPre(i + "", length, "0");
            num = NumberUtil.sortIntStr(num);
            if (set.contains(num)) {
                continue;
            }
            set.add(num);
        }
        return set;
    }

    /**
     * 从start~end组成set
     *
     * @param start  从几开始
     * @param end    到几结束
     * @param length 数值长度，如果不够会往前拼0，该值小于等于0，则该逻辑无效
     * @return
     */
    public static Set<String> rangeStrSet(int start, int end, int length) {
        Set<String> set = new TreeSet<>();
        for (int i = start; i <= end; i++) {
            if (length > 0) {
                set.add(StrUtil.padPre(i + "", length, "0"));
            } else {
                set.add(i + "");
            }
        }
        return set;
    }

    /**
     * 从start~end组成set
     *
     * @param start 从几开始
     * @param end   到几结束
     * @return
     */
    public static Set<Integer> rangeIntegerSet(int start, int end) {
        Set<Integer> set = new TreeSet<>();
        for (int i = start; i <= end; i++) {
            set.add(i);
        }
        return set;
    }

    /**
     * 从start~end组成单号set
     *
     * @param start 从几开始
     * @param end   到几结束
     * @return
     */
    public static Set<Integer> rangeDanIntegerSet(int start, int end) {
        Set<Integer> set = new TreeSet<>();
        for (int i = start; i <= end; i++) {
            if (i % 2 != 0) {
                set.add(i);
            }
        }
        return set;
    }

    /**
     * 从start~end组成单号set
     *
     * @param start 从几开始
     * @param end   到几结束
     * @return
     */
    public static Set<Integer> rangeShuangIntegerSet(int start, int end) {
        Set<Integer> set = new TreeSet<>();
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                set.add(i);
            }
        }
        return set;
    }

    /**
     * 生成一个数字列表<br>
     * 自动判定正序反序
     *
     * @param includedStart 开始的数字（包含）
     * @param includedEnd   结束的数字（包含）
     * @return 数字列表
     */
    public static Integer[] rangeIntegerArr(int includedStart, int includedEnd) {
        return rangeIntegerArr(includedStart, includedEnd, 1);
    }

    /**
     * 生成一个数字列表<br>
     * 自动判定正序反序
     *
     * @param includedStart 开始的数字（包含）
     * @param includedEnd   结束的数字（包含）
     * @param step          步长值
     * @return 数字列表
     */
    public static Integer[] rangeIntegerArr(int includedStart, int includedEnd, int step) {
        if (includedStart > includedEnd) {
            int tmp = includedStart;
            includedStart = includedEnd;
            includedEnd = tmp;
        }

        if (step <= 0) {
            step = 1;
        }

        int deviation = (includedEnd + 1) - includedStart;
        int length = deviation / step;
        if (deviation % step != 0) {
            length += 1;
        }
        Integer[] range = new Integer[length];
        for (int i = 0; i < length; i++) {
            range[i] = includedStart;
            includedStart += step;
        }
        return range;
    }

    /**
     * 排序枚举值
     *
     * @param enumStrArrStr
     * @param enumClass
     * @return
     */
    public static String sortByEnum(String enumStrArrStr, Class<? extends BaseEnum> enumClass) {
        if (StrUtil.isBlank(enumStrArrStr)) {
            return enumStrArrStr;
        }
        // 枚举列表
        BaseEnum[] enumConstants = enumClass.getEnumConstants();

        // 传入的值
        Set<String> enumStrSet = CollectionUtil.splitStrToSet(enumStrArrStr, CommonConstant.DOT_EN);

        // 验证传入的值是不是合法的枚举值
        for (String enumStr : enumStrSet) {
            if (EnumUtil.valueOfIEnum(enumClass, enumStr) == null) {
                throw new IllegalArgumentException();
            }
        }

        // 排序后的值
        Set<String> sortedSet = new LinkedHashSet<>();

        // 排序
        for (BaseEnum enumConstant : enumConstants) {
            if (enumStrSet.contains(enumConstant.getVal())) {
                sortedSet.add(enumConstant.getVal());
            }
        }

        // 返回
        return CollectionUtil.join(sortedSet, CommonConstant.DOT_EN);
    }

    /**
     * 获取集合的stream
     *
     * @param c   集合
     * @param <T> 集合类型
     * @return stream或parallelStream
     */
    private static <T> Stream<T> getStream(Collection<T> c) {
        return c.stream();
    }

    /**
     * 将list分为几组
     * @param list
     * @param groupSize
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list , int groupSize){
        int length = list.size();
        // 计算可以分成多少组
        int num = ( length + groupSize - 1 )/groupSize ;
        List<List<T>> newList = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            // 开始位置
            int fromIndex = i * groupSize;
            // 结束位置
            int toIndex = (i+1) * groupSize < length ? ( i+1 ) * groupSize : length ;
            newList.add(list.subList(fromIndex,toIndex)) ;
        }
        return  newList;
    }
}
