package com.rxliuli.rx.easy.test.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rxliuli
 */
@Rollback
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public abstract class BaseServiceTest<Service> extends BaseTest {
    @Autowired
    protected Service base;
}
