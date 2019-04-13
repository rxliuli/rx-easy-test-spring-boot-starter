package com.rxliuli.rx.easy.test.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rxliuli.rx.easy.test.inner.ref.SuperClassRefUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 基本的 web 测试
 * 自定义测试类最好不要继承这个
 * 一般选择子类 BaseWebUnitTest/BaseWebIntegratedTest 即可
 *
 * @author rxliuli
 * @date 2018/6/9
 */
@Rollback
@Transactional(rollbackFor = Exception.class)
public abstract class BaseWebTest extends BaseTest {
    /**
     * 提供一个全局可用的序列化 Bean
     */
    protected ObjectMapper om = new ObjectMapper()
            //Date 对象的格式
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS"))
            //默认忽略值为 null 的属性
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            //禁止序列化时间为时间戳
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            //启用序列化 BigDecimal 为非科学计算法格数
            .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
            .registerModules(
                    //注册 Jsr310（Java8 的时间兼容模块）
                    new JavaTimeModule(),
                    //序列化 Long 为 String
                    new SimpleModule()
                            //大数字直接序列化为 String
                            .addSerializer(Long.class, ToStringSerializer.instance)
                            .addSerializer(long.class, ToStringSerializer.instance)
                            .addSerializer(Long.TYPE, ToStringSerializer.instance)
                            .addSerializer(BigInteger.class, ToStringSerializer.instance)
                            .addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                                @Override
                                public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                    return LocalDateTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_DATE_TIME);
                                }
                            })
                            .addDeserializer(LocalDate.class, new JsonDeserializer<LocalDate>() {
                                @Override
                                public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                    return LocalDate.parse(p.getValueAsString(), DateTimeFormatter.ISO_DATE);
                                }
                            })
                            .addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
                                @Override
                                public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                                    return LocalTime.parse(p.getValueAsString(), DateTimeFormatter.ISO_TIME);
                                }
                            })
            )
            // JSON 序列化移除 transient 修饰的 Page 无关紧要的返回属性
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Autowired
    protected WebApplicationContext context;
    /**
     * 基本的用户 MockMvc
     */
    protected MockMvc mockMvc;
    /**
     * 用于 rest 服务的特化 MockMvc
     */
    protected MockMvc restMockMvc;

    /**
     * 将一个可序列化的对象转换为 MultiValueMap 集合
     *
     * @param param 参数对象
     * @param <T>   参数类型，必须实现了 Serializable 接口
     * @return 由对象属性名 -> 属性值组成的 MultiValueMap 映射表
     */
    protected <T extends Serializable> MultiValueMap<String, String> param2Map(T param) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        final HashSet<String> set = new HashSet<>();
        Map<String, List<String>> map = SuperClassRefUtil.getAllDeclaredField(param.getClass()).stream()
                .filter(field -> {
                    final String name = field.getName();
                    final boolean result = set.contains(name);
                    set.add(name);
                    return !result;
                })
                .collect(Collectors.toMap(Field::getName, field -> {
                    Object fieldValue = SuperClassRefUtil.getFieldValue(param, field.getName());
                    return Collections.singletonList(
                            fieldValue == null ? null : String.valueOf(fieldValue)
                    );
                }));
        multiValueMap.putAll(map);
        return multiValueMap;
    }
}