package com.rxliuli.rx.easy.test.base.data.dao;

import com.rxliuli.rx.easy.test.base.BaseServiceTest;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rxliuli
 */
class UserDaoTest extends BaseServiceTest<UserDao> {

    private final String username = "rxliuli";

    @Test
    void insert() {
        final int res = base.insert(new User().setUsername(username).setPassword("123456"));
        assertThat(res)
                .isEqualTo(1);
    }

    @Test
    void list() {
        insert();
        final List<User> list = base.list();
        final boolean exists = list.stream()
                .anyMatch(u -> Objects.equals(u.getUsername(), username));
        assertThat(exists)
                .isTrue();
    }
}