package com.rxliuli.rx.easy.test.base;

import com.rxliuli.rx.easy.test.base.data.bean.DemoComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rxliuli
 */
class AutowiredBeanTest extends BaseServiceTest<DemoComponent> {
    @Autowired
    private DemoComponent demoComponent;

    @Test
    void testBaseValue() {
        assertThat(base)
                .isEqualTo(demoComponent);
    }
}
