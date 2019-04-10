package com.rxliuli.rx.easy.test.base.data.web;

import com.rxliuli.rx.easy.test.base.BaseWebUnitTest;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import com.rxliuli.rx.easy.test.util.RandomDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author rxliuli
 */
class UserApiForUnitTest extends BaseWebUnitTest<UserApi> {
    @Test
    void insert() throws Exception {
        final String username = "琉璃";
        final User user = RandomDataUtil.random(User.class).setUsername(username);
        restMockMvc.perform(
                post("/api/user/insert")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(om.writeValueAsString(user))
        )
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void list() throws Exception {
        restMockMvc.perform(get("/api/user/list"))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试其他 API
     */
    @Test
    void testOtherApi() throws Exception {
        boolean result;
        try {
            restMockMvc.perform(get("/"));
            result = true;
        } catch (AssertionError e) {
            log.error("request error: {} {} ", e.getMessage(), e.getClass());
            result = false;
        }
        assertThat(result)
                .isFalse();
    }
}