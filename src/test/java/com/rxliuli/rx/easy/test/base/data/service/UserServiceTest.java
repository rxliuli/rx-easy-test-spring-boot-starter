package com.rxliuli.rx.easy.test.base.data.service;

import com.rxliuli.rx.easy.test.base.BaseServiceTest;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import com.rxliuli.rx.easy.test.util.RandomDataUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

/**
 * @author rxliuli
 */
class UserServiceTest extends BaseServiceTest<UserService> {
    @Test
    void testTransactional() {
        final String username = "琉璃";
        // 第一条数据没有错误能插入成功
        final User user1 = RandomDataUtil.random(User.class).setUsername(username);
        // 第二条数据存在错误
        final User user2 = RandomDataUtil.random(User.class).setUsername(null);
        boolean result = false;
        try {
            // 发生异常导致回滚
            result = base.insertBatch(list(user1, user2));
        } catch (Exception e) {
            log.error("insert error: {}", e);
        }
        assertThat(result).isFalse();

        // 两条数据都没有被插入
        final List<User> list = base.list();
        log.info("list: {}", list);
        assertThat(list.stream().anyMatch(u -> Objects.equals(u.getUsername(), username)))
                .isFalse();
    }
}