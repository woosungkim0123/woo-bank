package shop.woosung.bank.transaction.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.dto.AccountDto;
import shop.woosung.bank.transaction.controller.port.TransactionService;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest {
    private MockMvc mvc;
    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;

    @BeforeEach
    public void init() {
        this.mvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @DisplayName("계좌번호에 따른 계좌 내역 리스트를 가져온다")
    @Test
    public void get_account_transaction_list_by_account_full_number() throws Exception {
        // given
        String accountFullNumber = "2321111111111";
        String type = "ALL";
        Integer page = 0;

        // stub
        AccountDto accountDto = AccountDto.from(Account.builder().id(1L).fullNumber(2321111111111L).build());
        Account account1 = Account.builder().id(1L).fullNumber(2321111111111L).balance(2000L).build();
        Account account2 = Account.builder().id(2L).fullNumber(3431111111112L).balance(3000L).build();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(Transaction.builder().id(1L).type(TransactionType.TRANSFER).withdrawAccount(account1).withdrawAccountBalance(2000L).depositAccount(account2).depositAccountBalance(1000L).amount(1000L).sender(account1.getFullNumber().toString()).receiver(account2.getFullNumber().toString()).createdAt(LocalDateTime.of(2023, 10, 22, 10, 10, 10)).build());
        transactionList.add(Transaction.builder().id(2L).type(TransactionType.DEPOSIT).depositAccount(account1).depositAccountBalance(3000L).amount(2000L).sender("ATM").receiver(account1.getFullNumber() + "").tel("010-1234-5678").createdAt(LocalDateTime.of(2023, 10, 22, 10, 10, 10)).build());
        TransactionResponseListDto transactionResponseListDto = TransactionResponseListDto.from(accountDto, transactionList);

        when(transactionService.getTransactionList(anyLong(), anyString(), anyInt(), any())).thenReturn(transactionResponseListDto);

        // when
        ResultActions resultActions = mvc.perform(
                get("/api/s/account/" + accountFullNumber + "/transaction")
                        .param("type", type)
                        .param("page", page.toString())
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.status").value("success"));
        resultActions.andExpect(jsonPath("$.message").value("입출금 목록보기 성공"));
        resultActions.andExpect(jsonPath("$.data.transactions.length()").value(2));
        resultActions.andExpect(jsonPath("$.data.transactions[0].id").value(1L));
        resultActions.andExpect(jsonPath("$.data.transactions[0].type").value("이체"));
        resultActions.andExpect(jsonPath("$.data.transactions[0].amount").value(1000L));
        resultActions.andExpect(jsonPath("$.data.transactions[0].sender").value("2321111111111"));
        resultActions.andExpect(jsonPath("$.data.transactions[0].receiver").value("3431111111112"));
        resultActions.andExpect(jsonPath("$.data.transactions[0].tel").value("없음"));
        resultActions.andExpect(jsonPath("$.data.transactions[0].createdAt").value("2023-10-22 10:10:10"));
        resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(2000L));
        resultActions.andExpect(jsonPath("$.data.transactions[1].id").value(2L));
        resultActions.andExpect(jsonPath("$.data.transactions[1].type").value("입금"));
        resultActions.andExpect(jsonPath("$.data.transactions[1].amount").value(2000L));
        resultActions.andExpect(jsonPath("$.data.transactions[1].sender").value("ATM"));
        resultActions.andExpect(jsonPath("$.data.transactions[1].receiver").value("2321111111111"));
        resultActions.andExpect(jsonPath("$.data.transactions[1].tel").value("010-1234-5678"));
        resultActions.andExpect(jsonPath("$.data.transactions[1].createdAt").value("2023-10-22 10:10:10"));
        resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(3000L));
    }
}