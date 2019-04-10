package com.rxliuli.rx.easy.test.base;

import com.rxliuli.rx.easy.test.spring.SpringConfigUtil;
import com.rxliuli.rx.easy.test.base.data.bean.DemoComponent;
import com.rxliuli.rx.easy.test.spring.SpringBeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试获取指定类型的 Bean 并且手动声明测试所依赖的 Bean
 *
 * @author rxliuli
 */
@SpringBootTest(classes = {SpringBeanUtil.class})
class GetSpecifyClassBeanForManualDeclaringDependenciesTest extends BaseTest {
    @Autowired(required = false)
    private SpringBeanUtil springBeanUtil;
    @Autowired(required = false)
    private SpringConfigUtil springConfigUtil;
    @Autowired(required = false)
    private DemoComponent demoComponent;

    @Test
    void testGetBean() {
        // 手动声明所以可以拿到
        assertThat(springBeanUtil).isNotNull();
        // 否则拿不到
        assertThat(springConfigUtil).isNull();
        // 本该可以自动拿到的 Bean 也拿不到，因为没有手动声明
        // 一旦手动声明依赖，则自动扫描就失效了，对于测试一些简单依赖的 Bean 有时候会在速度上很有优势
        assertThat(demoComponent).isNull();
    }
}
