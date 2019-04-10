package com.rxliuli.rx.easy.test.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

/**
 * @author rxliuli by 2018/5/23 1:42
 */
@Rollback
public abstract class BaseServiceTest<Service> extends BaseTest {
    @Autowired
    protected Service base;
}
