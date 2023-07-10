package shop.woosung.bank.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.woosung.bank.dto.user.UserReqDto.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyUserObject {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
       userRepository.save(newUser("test", "1234", "test@naver.com", "test", UserEnum.CUSTOMER));
    }

    @DisplayName("로그인 성공")
    @Test
    public void successfulAuthentication() throws Exception {
        // given
        LoginReqDto loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("test");
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);

        // then
        assertThat(jwtToken).startsWith(JwtVO.TOKEN_PREFIX);
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(jsonPath("$.message").value("로그인 성공"));
        resultActions.andExpect(jsonPath("$.data.username").value("test"));
    }



}