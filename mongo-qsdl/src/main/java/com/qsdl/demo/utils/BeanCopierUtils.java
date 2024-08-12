package com.qsdl.demo.utils;
import net.sf.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName: BeanCopierUtils
 * @Description: 对象拷贝
 * @Author: ice
 * @Date: 2023/10/12 15:51
 */
public class BeanCopierUtils {

    // 创建一个map来存储BeanCopier
    private static final Map<String, BeanCopier> beanCopierMap = new HashMap<>();

    /**
     * 深拷贝,我们可以直接传实例化的拷贝对象和被实例化的拷贝对象进行深拷贝
     *
     * @param source 源
     * @param target 目标
     */
    public static void copy(Object source, Object target) {
        if (source == null) {
            return;
        }
        // 用来判断空指针异常
        Objects.requireNonNull(target);
        String key = getKey(source, target);
        BeanCopier beanCopier;
        // 判断键是否存在，不存在就将BeanCopier插入到map里，存在就直接获取
        if (!beanCopierMap.containsKey(key)) {
            beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
            beanCopierMap.put(key, beanCopier);
        } else {
            beanCopier = beanCopierMap.get(key);
        }
        beanCopier.copy(source, target, null);
    }

    /**
     * 深拷贝
     * @param source 源
     * @param tClass 类
     * @param <T> t
     * @return t
     */
    public static <T> T copy(Object source, Class<T> tClass) {
        if (source == null) {
            return null;
        }
        // 用来判断空指针异常
        Objects.requireNonNull(tClass);
        T dest = null;
        try {
            dest = tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        copy(source, dest);
        return dest;
    }

    /**
     * List深拷贝
     *
     * @param strList 源
     * @param tClass 拷贝为
     * @param <T> t
     * @param <S> s
     * @return list 拷贝list
     */
    public static <T, S> List<T> copyList(List<S> strList, Class<T> tClass) {
        // 判断空指针异常
        Objects.requireNonNull(tClass);
        return strList.stream().map(src -> copy(src, tClass)).collect(Collectors.toList());

    }

    /**
     * 获取Map Key
     * @param object obj
     * @param target target
     * @return string
     */
    private static String getKey(Object object, Object target) {
        return object.getClass().getName() + target.getClass().getName();
    }
}
