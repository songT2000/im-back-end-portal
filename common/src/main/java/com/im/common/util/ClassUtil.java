package com.im.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 类工具类，本类补充{@link cn.hutool.core.util.ClassUtil}中不能扫描子目录，其它方法使用hutool中的工具类
 *
 * @author Barry
 * @date 2018/6/8
 */
public final class ClassUtil extends cn.hutool.core.util.ClassUtil {
    private static final String CLASS_SUFFIX = ".class";

    private ClassUtil() {
    }

    /**
     * 获取同一路径下所有子类或接口实现类,包含子目录
     *
     * @param clz 类
     * @return 所有class
     */
    public static List<Class<?>> getAllSubClasses(Class<?> clz) {
        return getAllSubClasses(clz, true);
    }

    /**
     * 获取指定路径下所有子类或接口实现类,包含子目录
     *
     * @param clz 类
     * @return 所有class
     */
    public static List<Class<?>> getAllSubClasses(String packageName, Class<?> clz) {
        return getAllSubClasses(packageName, clz, true);
    }

    /**
     * 获取同一路径下所有子类或接口实现类
     *
     * @param clz                   类
     * @param includeSubDirectories 是否查找子目录
     * @return 所有class
     */
    public static List<Class<?>> getAllSubClasses(Class<?> clz, boolean includeSubDirectories) {
        List<Class<?>> classes = new ArrayList<>();
        List<Class<?>> findClazz = getClasses(clz, includeSubDirectories);
        for (Class<?> c : findClazz) {
            if (clz.isAssignableFrom(c) && !clz.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 获取指定路径下所有子类或接口实现类
     *
     * @param clz                   类
     * @param includeSubDirectories 是否查找子目录
     * @return 所有class
     */
    public static List<Class<?>> getAllSubClasses(String packageName, Class<?> clz, boolean includeSubDirectories) {
        List<Class<?>> classes = new ArrayList<>();
        List<Class<?>> findClazz = getClasses(packageName, includeSubDirectories);
        for (Class<?> c : findClazz) {
            if (clz.isAssignableFrom(c) && !clz.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * 获取指定包下所有类
     *
     * @param clz                   类
     * @param includeSubDirectories 是否查找子目录
     * @return 所有class
     */
    private static List<Class<?>> getClasses(Class<?> clz, boolean includeSubDirectories) {
        String packageName = clz.getPackage().getName();

        return getClasses(packageName, includeSubDirectories);
    }

    /**
     * 获取指定包下所有类
     *
     * @param packageName           包名
     * @param includeSubDirectories 是否查找子目录
     * @return 所有class
     */
    private static List<Class<?>> getClasses(String packageName, boolean includeSubDirectories) {
        String path = packageName.replace('.', '/');

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        File file = new File(classloader.getResource(path).getFile());

        return getClasses(file, packageName, includeSubDirectories);
    }

    /**
     * 查找指定路径下的所有类
     *
     * @param dir                   路径
     * @param packageName           包名
     * @param includeSubDirectories 是否查找子目录
     * @return 类列表
     */
    private static List<Class<?>> getClasses(File dir, String packageName, boolean includeSubDirectories) {
        try {
            List<Class<?>> classes = new ArrayList<>();
            if (!dir.exists()) {
                return classes;
            }

            File[] files = dir.listFiles();
            if (files == null || files.length <= 0) {
                return classes;
            }

            for (File file : files) {
                if (includeSubDirectories && file.isDirectory()) {
                    List<Class<?>> subClass = getClasses(file, packageName + "." + file.getName(), includeSubDirectories);
                    if (subClass != null && subClass.size() > 0) {
                        classes.addAll(subClass);
                    }
                }

                String name = file.getName();
                if (name.endsWith(CLASS_SUFFIX)) {
                    String classPackageName = name.substring(0, name.length() - CLASS_SUFFIX.length());
                    String fullPackageName = packageName + "." + classPackageName;
                    Class<?> aClass = Class.forName(fullPackageName);
                    classes.add(aClass);
                }
            }
            return classes;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
