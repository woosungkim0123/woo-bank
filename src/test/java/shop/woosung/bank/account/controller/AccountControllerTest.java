package shop.woosung.bank.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.woosung.bank.account.controller.dto.AccountDepositRequestDto;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.dto.AccountWithdrawRequestDto;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.handler.AccountControllerAdvice;
import shop.woosung.bank.account.handler.exception.*;
import shop.woosung.bank.account.service.dto.AccountDepositResponseDto;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawResponseDto;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest {
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(accountController).setControllerAdvice(new AccountControllerAdvice()).build();
    }

    @DisplayName("자신의 모든 계좌를 조회할 수 있다.")
    @Test
    public void get_own_account_list_success_response() throws Exception {
        // given
        List<Account> accounts = new ArrayList<>();
        accounts.add(Account.builder().number(1111111111L).fullNumber(2321111111111L).balance(1000L).type(AccountType.NORMAL).user(User.builder().id(1L).build()).build());
        accounts.add(Account.builder().number(1111111112L).fullNumber(3431111111112L).balance(2000L).type(AccountType.SAVING).user(User.builder().id(1L).build()).build());
        AccountListResponseDto accountListResponseDto = AccountListResponseDto.from(User.builder().name("test1").build(), accounts);

        // stub
        when(accountService.getAccountList(any())).thenReturn(accountListResponseDto);

        // when
        ResultActions resultActions = mvc.perform(
                get("/api/s/accounts"));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("요청 성공"));
        resultActions.andExpect(jsonPath("$.data.username").value("test1"));
        resultActions.andExpect(jsonPath("$.data.accounts.length()").value(2));
        resultActions.andExpect(jsonPath("$.data.accounts[0].fullNumber").value(2321111111111L));
        resultActions.andExpect(jsonPath("$.data.accounts[0].balance").value(1000L));
        resultActions.andExpect(jsonPath("$.data.accounts[0].type").value("NORMAL"));
        resultActions.andExpect(jsonPath("$.data.accounts[1].fullNumber").value(3431111111112L));
        resultActions.andExpect(jsonPath("$.data.accounts[1].balance").value(2000L));
        resultActions.andExpect(jsonPath("$.data.accounts[1].type").value("SAVING"));
    }

    @DisplayName("계좌 생성 성공시 정상적으로 응답한다.")
    @Test
    void register_account_success_response() throws Exception {
        // given
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password("1234")
                .type(AccountType.NORMAL)
                .build();
        String requestBody = om.writeValueAsString(accountRegisterRequestDto);
        Account account = Account.builder().id(1L).fullNumber(2321111111111L).number(1111111111L).balance(0L).type(AccountType.NORMAL).user(User.builder().id(1L).build()).build();

        // stub
        when(accountService.register(any(), any())).thenReturn(AccountRegisterResponseDto.from(account));

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
        resultActions.andExpect(jsonPath("$.data.fullNumber").value(2321111111111L));
        resultActions.andExpect(jsonPath("$.data.number").doesNotExist());
        resultActions.andExpect(jsonPath("$.data.balance").value(0L));
    }

    @DisplayName("계좌 생성시 계좌 종류에 맞는 계좌 type 번호가 없으면 에러를 응답한다.")
    @Test
    void if_not_found_account_type_when_register_account_return_error() throws Exception {
        // given
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password("1234")
                .type(AccountType.NORMAL)
                .build();
        String requestBody = om.writeValueAsString(accountRegisterRequestDto);

        // stub
        when(accountService.register(any(), any())).thenThrow(NotFoundAccountTypeNumberException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 계좌 종류"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 생성시 계좌 종류에 맞는 계좌 sequence 번호가 없으면 에러를 응답한다.")
    @Test
    void if_not_found_account_sequence_when_register_account_return_error() throws Exception {
        // given
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password("1234")
                .type(AccountType.NORMAL)
                .build();
        String requestBody = om.writeValueAsString(accountRegisterRequestDto);

        // stub
        when(accountService.register(any(), any())).thenThrow(NotFoundAccountSequenceException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 계좌 종류"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 삭제 성공시 정상적으로 응답한다.")
    @Test
    void delete_account_success_response() throws Exception {
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
    @Test
    void not_delete_others_account() throws Exception {
        // stub
        doThrow(NotAccountOwnerException.class).when(accountService).deleteAccount(any(), any());

        // when
        ResultActions resultActions = mvc.perform(
                delete("/api/s/account/" + 2321111111111L));

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("계좌 소유자 불일치"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 입금 성공시 정상적으로 응답한다.")
    @Test
    void deposit_account_success_response() throws Exception {
        // given
        Account account = Account.builder().id(1L).fullNumber(2321111111111L).build();
        Transaction transaction = Transaction.builder().id(1L).type(TransactionType.DEPOSIT).sender("ATM").receiver("2321111111111").amount(1000L).tel("01012341234")
                .createdAt(LocalDateTime.of(2023, 8, 11, 15, 30)).build();

        AccountDepositRequestDto accountDepositRequestDto = AccountDepositRequestDto.builder().amount(1000L).fullNumber(2321111111111L).transactionType(TransactionType.DEPOSIT).sender("ATM").tel("01012341234").build();
        String requestBody = om.writeValueAsString(accountDepositRequestDto);

        // stub
        when(accountService.deposit(any())).thenReturn(AccountDepositResponseDto.from(account, transaction));

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("계좌 입금 완료"));
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.fullNumber").value(2321111111111L));
        resultActions.andExpect(jsonPath("$.data.transaction.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.transaction.type").value("DEPOSIT"));
        resultActions.andExpect(jsonPath("$.data.transaction.sender").value("ATM"));
        resultActions.andExpect(jsonPath("$.data.transaction.receiver").value("2321111111111"));
        resultActions.andExpect(jsonPath("$.data.transaction.amount").value(1000L));
        resultActions.andExpect(jsonPath("$.data.transaction.tel").value("01012341234"));
        resultActions.andExpect(jsonPath("$.data.transaction.createdAt").value("2023-08-11T15:30"));
    }

    @DisplayName("계좌 입금시 계좌번호를 찾을 수 없다면 에러를 응답한다.")
    @Test
    void if_not_found_account_number_when_deposit_account_return_error() throws Exception {
        // given
        AccountDepositRequestDto accountDepositRequestDto = AccountDepositRequestDto
                .builder().amount(1000L).fullNumber(2321111111112L)
                .transactionType(TransactionType.DEPOSIT).sender("ATM").tel("01012341234").build();

        String requestBody = om.writeValueAsString(accountDepositRequestDto);

        when(accountService.deposit(any())).thenThrow(NotFoundAccountFullNumberException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/account/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 계좌 번호"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 출금 성공시 정상적으로 응답한다.")
    @Test
    void withdraw_account_success_response() throws Exception {
        // given
        Account account = Account.builder().id(1L).fullNumber(2321111111111L).balance(9000L).build();
        Transaction transaction = Transaction.builder().id(1L).type(TransactionType.WITHDRAW).sender("2321111111111").receiver("ATM").amount(1000L)
                .createdAt(LocalDateTime.of(2023, 8, 11, 15, 30)).build();
        AccountWithdrawRequestDto accountWithdrawRequestDto = AccountWithdrawRequestDto.builder().fullNumber(2321111111111L).password("1234").amount(1000L).transactionType(TransactionType.WITHDRAW).build();
        String requestBody = om.writeValueAsString(accountWithdrawRequestDto);

        // stub
        when(accountService.withdraw(any(), any())).thenReturn(AccountWithdrawResponseDto.from(account, transaction));

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("계좌 출금 완료"));
        resultActions.andExpect(jsonPath("$.data.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.fullNumber").value(2321111111111L));
        resultActions.andExpect(jsonPath("$.data.balance").value(9000L));
        resultActions.andExpect(jsonPath("$.data.transaction.id").value(1L));
        resultActions.andExpect(jsonPath("$.data.transaction.type").value("WITHDRAW"));
        resultActions.andExpect(jsonPath("$.data.transaction.sender").value("2321111111111"));
        resultActions.andExpect(jsonPath("$.data.transaction.receiver").value("ATM"));
        resultActions.andExpect(jsonPath("$.data.transaction.amount").value(1000L));
        resultActions.andExpect(jsonPath("$.data.transaction.createdAt").value("2023-08-11T15:30"));
    }

    @DisplayName("계좌 출금시 계좌번호를 찾을 수 없다면 에러를 응답한다.")
    @Test
    void if_not_found_account_number_when_withdraw_account_return_error() throws Exception {
        // given
        AccountWithdrawRequestDto accountWithdrawRequestDto = AccountWithdrawRequestDto.builder().fullNumber(2321111111111L).password("1234").amount(1000L).transactionType(TransactionType.WITHDRAW).build();
        String requestBody = om.writeValueAsString(accountWithdrawRequestDto);

        // stub
        when(accountService.withdraw(any(), any())).thenThrow(NotFoundAccountFullNumberException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잘못된 계좌 번호"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 출금시 계좌의 주인이 아니라면 에러를 응답한다.")
    @Test
    void if_not_account_owner_when_withdraw_account_return_error() throws Exception {
        // given
        AccountWithdrawRequestDto accountWithdrawRequestDto = AccountWithdrawRequestDto.builder().fullNumber(2321111111111L).password("1234").amount(1000L).transactionType(TransactionType.WITHDRAW).build();
        String requestBody = om.writeValueAsString(accountWithdrawRequestDto);

        // stub
        when(accountService.withdraw(any(), any())).thenThrow(NotAccountOwnerException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isForbidden());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("계좌 소유자 불일치"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 출금시 계좌 비밀번호가 일치하지않으면 에러를 응답한다.")
    @Test
    void if_not_match_account_password_when_withdraw_account_return_error() throws Exception {
        // given
        AccountWithdrawRequestDto accountWithdrawRequestDto = AccountWithdrawRequestDto.builder().fullNumber(2321111111111L).password("1234").amount(1000L).transactionType(TransactionType.WITHDRAW).build();
        String requestBody = om.writeValueAsString(accountWithdrawRequestDto);

        // stub
        when(accountService.withdraw(any(), any())).thenThrow(NotMatchAccountPasswordException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isUnauthorized());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("계좌 비밀번호 불일치"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }

    @DisplayName("계좌 출금시 출금 금액이 부족하면 에러를 응답한다.")
    @Test
    void if_not_enough_account_amount_when_withdraw_account_return_error() throws Exception {
        // given
        AccountWithdrawRequestDto accountWithdrawRequestDto = AccountWithdrawRequestDto.builder().fullNumber(2321111111111L).password("1234").amount(1000L).transactionType(TransactionType.WITHDRAW).build();
        String requestBody = om.writeValueAsString(accountWithdrawRequestDto);

        // stub
        when(accountService.withdraw(any(), any())).thenThrow(NotEnoughBalanceException.class);

        // when
        ResultActions resultActions = mvc.perform(
                post("/api/s/account/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        // then
        resultActions.andExpect(status().isUnprocessableEntity());
        resultActions.andExpect(jsonPath("$.status").value("error"));
        resultActions.andExpect(jsonPath("$.message").value("잔액 부족"));
        resultActions.andExpect(jsonPath("$.data").isEmpty());
    }
}