package shop.woosung.bank.transaction.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.AccountServiceImpl;
import shop.woosung.bank.account.service.dto.AccountDto;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.transaction.service.dto.TransactionResponseListDto;
import shop.woosung.bank.transaction.service.port.TransactionRepository;
import shop.woosung.bank.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountServiceImpl accountService;

    @DisplayName("계좌번호에 맞는 계좌 내역 리스트를 가져온다")
    @Test
    void get_transaction_list_by_account_full_number() {
        // given
        Long accountFullNumber = 2321111111111L;
        String type = "ALL";
        int page = 0;
        User user = User.builder().id(1L).build();

        // stub
        AccountDto accountDto = AccountDto.from(Account.builder().id(1L).fullNumber(2321111111111L).build());

        when(accountService.checkAccountOwner(anyLong(), any())).thenReturn(accountDto);

        List<Transaction> transactionList = new ArrayList<>();
        Account account1 = Account.builder().id(1L).fullNumber(2321111111111L).balance(2000L).build();
        Account account2 = Account.builder().id(2L).fullNumber(3431111111112L).balance(3000L).build();
        transactionList.add(Transaction.builder().id(1L).type(TransactionType.TRANSFER).withdrawAccount(account1).withdrawAccountBalance(2000L).depositAccount(account2).depositAccountBalance(1000L).amount(1000L).sender(account1.getFullNumber().toString()).receiver(account2.getFullNumber().toString()).createdAt(LocalDateTime.of(2023, 10, 22, 10, 10, 10)).build());
        transactionList.add(Transaction.builder().id(2L).type(TransactionType.DEPOSIT).depositAccount(account1).depositAccountBalance(2000L).amount(1000L).sender("ATM").receiver(account1.getFullNumber().toString()).createdAt(LocalDateTime.of(2023, 10, 22, 10, 10, 10)).build());

        when(transactionRepository.findTransactionList(anyLong(), anyString(), anyInt())).thenReturn(transactionList);

        // when
        TransactionResponseListDto result = transactionService.getTransactionList(accountFullNumber, type, page, user);

        // then
        assertThat(result.getTransactions().get(0).getId()).isEqualTo(1L);
        assertThat(result.getTransactions().get(0).getType()).isEqualTo("이체");
        assertThat(result.getTransactions().get(0).getAmount()).isEqualTo(1000L);
        assertThat(result.getTransactions().get(0).getSender()).isEqualTo(account1.getFullNumber().toString());
        assertThat(result.getTransactions().get(0).getReceiver()).isEqualTo(account2.getFullNumber().toString());
        assertThat(result.getTransactions().get(0).getCreatedAt()).isEqualTo("2023-10-22 10:10:10");
        assertThat(result.getTransactions().get(1).getId()).isEqualTo(2L);
        assertThat(result.getTransactions().get(1).getType()).isEqualTo("입금");
        assertThat(result.getTransactions().get(1).getAmount()).isEqualTo(1000L);
        assertThat(result.getTransactions().get(1).getSender()).isEqualTo("ATM");
        assertThat(result.getTransactions().get(1).getReceiver()).isEqualTo(account1.getFullNumber().toString());
        assertThat(result.getTransactions().get(1).getCreatedAt()).isEqualTo("2023-10-22 10:10:10");
    }
}