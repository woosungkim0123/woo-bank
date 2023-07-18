package shop.woosung.bank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.util.dummy.DummyUserObject;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.woosung.bank.dto.account.AccountReqDto.*;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyUserObject {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        User test1 = userRepository.save(newUser("test1", "1234", "test1@naver.com", "테스터일", UserEnum.CUSTOMER));
        User test2 = userRepository.save(newUser("test2", "1234", "test2@naver.com", "테스터이", UserEnum.CUSTOMER));
        accountRepository.save(newAccount(11111111111L, test1));
        accountRepository.save(newAccount(11111111112L, test2));
        em.clear();
    }

    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void saveAccount_test() throws Exception {
        // given
        AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
        accountRegisterReqDto.setPassword(1234L);
        String requestBody = om.writeValueAsString(accountRegisterReqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findUserAccount_test() throws Exception {
        // given
        // when
        ResultActions resultActions = mvc.perform(get("/api/s/accounts"));

        // then
        resultActions.andExpect(status().isOk());
    }
    /*
        junit 테스트에서는 delete 쿼리 다음에 쿼리가 없으면 sql이 안찍힘
     */
    @WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void deleteAccountTest() throws Exception {
        // given
        Long accountNumber = 11111111111L;

        // when
        mvc.perform(delete("/api/s/account/" + accountNumber));

        // then
        assertThat(accountRepository.findByNumber(accountNumber)).isEmpty();
    }
}