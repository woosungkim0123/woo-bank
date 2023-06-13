package shop.woosung.bank.config;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("/api/s/** 로그인 없이 요청시 401 리턴 테스트")
    @Test
    public void authenticationTest() throws Exception {
        // given
        // when
        ResultActions resultAction = mvc.perform(get("/api/s/test"));
        int httpStatus = resultAction.andReturn().getResponse().getStatus();

        // then
        assertThat(httpStatus).isEqualTo(401);
    }

    @DisplayName("/api/admin/** 로그인 없이 요청시 401 리턴 테스트")
    @Test
    public void authorizationTest() throws Exception {
        // given
        // when
        ResultActions resultAction = mvc.perform(get("/api/admin/test"));
        String responseBody = resultAction.andReturn().getResponse().getContentAsString();
        int httpStatus = resultAction.andReturn().getResponse().getStatus();
        // then
        assertThat(httpStatus).isEqualTo(401);
    }

}