package com.rxliuli.rx.easy.test.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 生成指定数量的列表
 *
 * @author rxliuli
 */
public interface ListGenerator {
    /**
     * 生成一个指定数量的 List
     *
     * @param num 数量
     * @param fn  生成的函数
     * @param <T> list 的泛型类型
     * @return 生成的 list 集合
     */
    static <T> List<T> generate(int num, Function<Integer, T> fn) {
        return IntStream.range(0, num)
                .mapToObj(fn::apply)
                .collect(Collectors.toList());
    }
}
