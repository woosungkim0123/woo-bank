package shop.woosung.bank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.woosung.bank.dto.user.UserReqDto.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest extends DummyUserObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        userRepository.save(newUser("test1", "1234", "test1@naver.com", "테스터일", UserEnum.CUSTOMER));
    }

    @Test
    public void join_success_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test2");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test2@naver.com");
        joinReqDto.setFullname("테스트이");
        String requestBody = om.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void join_fail_test() throws Exception {
        // given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test1");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test2@naver.com");
        joinReqDto.setFullname("테스트일");
        String requestBody = om.writeValueAsString(joinReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/join")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

}