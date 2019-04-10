package com.rxliuli.rx.easy.test.base.data.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.rxliuli.rx.easy.test.base.BaseWebUnitTest;
import com.rxliuli.rx.easy.test.base.data.entity.Result;
import com.rxliuli.rx.easy.test.base.data.entity.User;
import com.rxliuli.rx.easy.test.base.data.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Objects;

import static com.rxliuli.rx.easy.test.util.ListGenerator.generate;
import static com.rxliuli.rx.easy.test.util.RandomDataUtil.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author rxliuli
 */
class UserApiForUnitTest extends BaseWebUnitTest<UserApi> {
    @Autowired
    private UserService userService;

    @Test
    void insert() throws Exception {
        final String username = "琉璃";
        final User user = random(User.class).setUsername(username);
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
     * 测试反序列化 list
     */
    @Test
    void listOfDeserialize() throws Exception {
        final int num = 10;
        final List<User> list = generate(num, i -> random(User.class));
        final boolean updateResult = userService.insertBatch(list);
        assertThat(updateResult)
                .isTrue();
        final String str = restMockMvc.perform(get("/api/user/list"))
                .andExpect(jsonPath("$.data").isArray())
                .andReturn()
                .getResponse()
                .getContentAsString();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        final Result<List<User>> result = om.readValue(str, new TypeReference<Result<List<User>>>() {
        });
        assertThat(result.getData())
                .hasSize(num)
                .allMatch(u1 -> list.stream().anyMatch(u2 ->
                        Objects.equals(u1.getUsername(), u2.getUsername())
                                && Objects.equals(u1.getPassword(), u2.getPassword())
                ));
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