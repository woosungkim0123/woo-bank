package shop.woosung.bank.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.mock.repository.FakeAccountRepository;
import shop.woosung.bank.mock.repository.FakeUserRepository;
import shop.woosung.bank.mock.config.FakeRepositoryConfiguration;
import shop.woosung.bank.user.domain.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import({ FakeRepositoryConfiguration.class })
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FakeUserRepository userRepository;

    @Autowired
    private FakeAccountRepository accountRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.save(User.builder().email("test1@test.com").name("test1").build());
        userRepository.save(User.builder().email("test2@test.com").name("test2").build());
    }

    @DisplayName("로그인 한 사용자는 자신의 계좌 리스트를 볼 수 있다.")
    @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void get_account_list_test_success() throws Exception {
        // given
        User user1 = userRepository.findByEmail("test1@test.com").get();
        User user2 = userRepository.findByEmail("test2@test.com").get();
        accountRepository.save(Account.builder().number(1111111111L).balance(1000L).type(AccountType.NORMAL).user(user1).build());
        accountRepository.save(Account.builder().number(1111111112L).balance(2000L).type(AccountType.SAVING).user(user1).build());
        accountRepository.save(Account.builder().number(1111111113L).balance(3000L).type(AccountType.SAVING).user(user2).build());

        // when
        ResultActions resultActions = mvc.perform(
                get("/api/s/accounts"));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("요청 성공"));
        resultActions.andExpect(jsonPath("$.data.accounts.length()").value(2));
        resultActions.andExpect(jsonPath("$.data.accounts[0].number").value(1111111111L));
        resultActions.andExpect(jsonPath("$.data.accounts[0].balance").value(1000L));
        resultActions.andExpect(jsonPath("$.data.accounts[0].type").value("NORMAL"));
        resultActions.andExpect(jsonPath("$.data.accounts[1].number").value(1111111112L));
        resultActions.andExpect(jsonPath("$.data.accounts[1].balance").value(2000L));
        resultActions.andExpect(jsonPath("$.data.accounts[1].type").value("SAVING"));
    }

    @DisplayName("로그인 한 사용자는 계좌를 생성할 수 있다.")
    @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void register_account_test_success() throws Exception {
        // given
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password("1234")
                .type(AccountType.NORMAL)
                .build();
        String requestBody = om.writeValueAsString(accountRegisterRequestDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("계좌등록 성공"));
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.fullnumber").value(2321111111111L));
        resultActions.andExpect(jsonPath("$.data.number").doesNotExist());
        resultActions.andExpect(jsonPath("$.data.balance").value(0L));
    }

    @DisplayName("자신의 계좌를 삭제할 수 있다.")
    @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void delete_account_test_success() throws Exception {
        // given
        User user1 = userRepository.findByEmail("test1@test.com").get();
        accountRepository.save(Account.builder().fullnumber(2321111111111L).number(1111111111L).balance(1000L).type(AccountType.NORMAL).user(user1).build());

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/s/account/" + 2321111111111L));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("계좌 삭제 성공"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("타인의 계좌를 삭제할 수 없다.")
    @WithUserDetails(value = "test1@test.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void delete_account_test_fail() throws Exception {
        // given
        User user2 = userRepository.findByEmail("test2@test.com").get();
        accountRepository.save(Account.builder().fullnumber(2321111111111L).number(1111111111L).balance(1000L).type(AccountType.NORMAL).user(user2).build());

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/s/account/" + 2321111111111L));

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 계좌 번호"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }
}