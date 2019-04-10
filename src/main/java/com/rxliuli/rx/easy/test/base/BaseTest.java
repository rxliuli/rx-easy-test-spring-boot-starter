package com.rxliuli.rx.easy.test.base;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * SpringBoot 测试基类，自动加载 Spring 的环境
 * SpringBootTest: 默认使用 @SpringBootApplication 注解声明的启动类，如果没有，则需要手动声明需要依赖的 Bean
 *
 * @author rxliuli
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
}
