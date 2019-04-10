package com.rxliuli.rx.easy.test.base.data.web;

import com.rxliuli.rx.easy.test.base.BaseWebIntegratedTest;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import com.rxliuli.rx.easy.test.util.RandomDataUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author rxliuli
 */
class UserApiForIntegratedTest extends BaseWebIntegratedTest {
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
        restMockMvc.perform(get("/"))
                .andExpect(jsonPath("$.data").value("index"));
    }
}