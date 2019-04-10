package com.rxliuli.rx.easy.test.base;

import com.rxliuli.rx.easy.test.spring.SpringConfigUtil;
import com.rxliuli.rx.easy.test.base.data.bean.DemoComponent;
import com.rxliuli.rx.easy.test.spring.SpringBeanUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试获取指定类型的 Bean 并且自动扫描依赖的 Bean
 * 对比 {@link GetSpecifyClassBeanForManualDeclaringDependenciesTest} 手动声明依赖看看
 *
 * @author rxliuli
 */
class GetSpecifyClassBeanForAutoScanDependenciesTest extends BaseTest {
    @Autowired(required = false)
    private SpringBeanUtil springBeanUtil;
    @Autowired(required = false)
    private SpringConfigUtil springConfigUtil;
    @Autowired(required = false)
    private DemoComponent demoComponent;

    @Test
    void testGetBean() {
        // 扫描不到
        assertThat(springBeanUtil).isNull();
        // 扫描不到
        assertThat(springConfigUtil).isNull();
        // 因为在 Application 或其子包下所以可以扫描到
        assertThat(demoComponent).isNotNull();
    }
}
