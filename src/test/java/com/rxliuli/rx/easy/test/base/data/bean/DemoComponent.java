package com.rxliuli.rx.easy.test.base.data.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 用于测试的 demo component
 *
 * @author rxliuli
 */
@Component
public class DemoComponent {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostConstruct
    private void init() {
        log.info("initial DemoComponent");
    }
}
