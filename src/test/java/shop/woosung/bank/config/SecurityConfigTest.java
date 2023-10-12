package shop.woosung.bank.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {
    @Autowired
    private MockMvc mvc;

    @DisplayName("로그인 필요한 요청에 로그인을 하고 접근하면 정상적으로 응답한다.")
    @Test
    void needed_login_request_do_login_response_success() throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(get("/api/s/test")
                .with(user("test@test.com").password("password").roles("USER")));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @DisplayName("로그인 필요한 요청에 로그인 없이 접근하면 예외를 응답한다.")
    @Test
    void if_needed_login_request_not_login_throw_exception() throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(get("/api/s/test"));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("로그인 필요"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("관리자 권한이 필요한 요청에 권한이 없으면 예외를 응답한다.")
    @Test
    void if_needed_authority_request_not_have_authority_throw_exception() throws Exception {
        // given & when
        ResultActions resultActions = mvc.perform(get("/api/admin/test")
                .with(user("test@test.com").password("password").roles("USER")));

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("권한 없는 접근"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }
}