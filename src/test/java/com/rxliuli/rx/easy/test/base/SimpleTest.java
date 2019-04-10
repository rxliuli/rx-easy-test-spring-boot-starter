package com.rxliuli.rx.easy.test.base;


import com.rxliuli.rx.easy.test.base.data.bean.DemoComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 基本的测试
 *
 * @author rxliuli
 */
class SimpleTest extends BaseTest {
    @Autowired
    private ApplicationContext context;
    @Autowired(required = false)
    private DemoComponent demoComponent;

    @Test
    void testApplicationContext() {
        assertThat(context)
                .isNotNull();
    }

    @Test
    void testGetDemoComponent() {
        assertThat(demoComponent)
                .isNotNull();
    }
}
