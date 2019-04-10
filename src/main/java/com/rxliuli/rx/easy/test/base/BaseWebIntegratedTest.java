package com.rxliuli.rx.easy.test.base;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * web 集成测试
 * 将启动并且加载所有的 Controller, 所以效率上之于 BaseWebUnitTest 来说非常低下，仅适用于集成测试多个 Controller 时使用
 *
 * @author rxliuli by 2018/5/23 1:43
 */
public abstract class BaseWebIntegratedTest extends BaseWebTest {
    @BeforeEach
    public void setupMockMvc() {
        // 初始化 MockMvc 对象
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        restMockMvc = MockMvcBuilders.webAppContextSetup(context)
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .alwaysExpect(status().isOk())
                .build();
    }
}