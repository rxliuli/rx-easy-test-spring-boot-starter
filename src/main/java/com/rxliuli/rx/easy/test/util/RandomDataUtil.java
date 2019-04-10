package com.rxliuli.rx.easy.test.util;

import com.rxliuli.rx.easy.test.base.BaseTest;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 生成随机数据，混入到 {@link BaseTest} 中
 *
 * @author rxliuli
 */
public class RandomDataUtil {
    private static EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .stringLengthRange(8, 20)
            .charset(Charset.forName("UTF-8"))
            .build();

    /**
     * 注册一个全局的 EnhancedRandom 对象替换掉默认的 random 生成器对象
     *
     * @param random random 生成对象
     */
    public static void register(EnhancedRandom random) {
        RandomDataUtil.random = random;
    }

    /**
     * 生成一个随机数据
     * 注：此处默认排除 id 字段，因为它是自增的
     *
     * @param type           生成的类型
     * @param excludedFields 排除的字段
     * @param <T>            类型
     * @return 生成的对象
     */
    public static <T> T random(final Class<T> type, final String... excludedFields) {
        final String[] fields = Arrays.copyOf(excludedFields, excludedFields.length + 1);
        fields[excludedFields.length] = "id";
        return random.nextObject(type, fields);
    }
}
