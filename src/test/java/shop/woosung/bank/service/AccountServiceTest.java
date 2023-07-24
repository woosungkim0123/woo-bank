package shop.woosung.bank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.repository.TransactionRepository;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.handler.ex.CustomApiException;
import shop.woosung.bank.util.dummy.DummyUserObject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyUserObject {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void registerAccount() throws JsonProcessingException {
        // given
        Long LAST_ACCOUNT_NUMBER = 19111111119L;
        AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
        accountRegisterReqDto.setPassword(1234L);

        // stub
        User user = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserEnum.CUSTOMER);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(accountRepository.findFirstByOrderByNumberDesc()).thenReturn(Optional.of(Account.builder().number(LAST_ACCOUNT_NUMBER).build()));

        Account testAccount = newMockAccount(1L, LAST_ACCOUNT_NUMBER + 1, 1234L, user);
        when(accountRepository.save(any())).thenReturn(testAccount);

        // when
        AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDto, user.getId());

        // then
        assertThat(accountRegisterResDto).isNotNull();
        assertThat(accountRegisterResDto.getNumber()).isEqualTo(19111111120L);
    }

    @Test
    public void 계좌목록보기_유저별_test() throws Exception {
        // given
        Long userId = 1L;

        // stub
        User user = newMockUser(1L, "test1", "1234", "test@test.com", "test1", UserEnum.CUSTOMER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Account account1 = newMockAccount(1L, 11111111111L, 1000L, user);
        Account account2 = newMockAccount(2L, 11111111112L, 1000L, user);
        List<Account> accountList = Arrays.asList(account1, account2);
        when(accountRepository.findByUser_id(any())).thenReturn(accountList);

        // when
        AccountListResDto accountListRespDto = accountService.getAccountList(userId);

        // then
        assertThat(accountListRespDto.getFullname()).isEqualTo("test1");
        assertThat(accountListRespDto.getAccounts().size()).isEqualTo(2);
    }

    @Test
    public void deleteAccountFailNotUser() throws Exception {
        // given
        Long number = 11111111111L;
        Long userId = 2L;


        User user = newMockUser(1L, "test1", "1234", "test@test.com", "test1", UserEnum.CUSTOMER);
        Account account = newMockAccount(1L, number, 1000L, user);
        when(accountRepository.findByNumber(number)).thenReturn(Optional.of(account));

        // when
        // then
        Assertions.assertThatThrownBy(() -> accountService.deleteAccount(number, userId))
                .isInstanceOf(CustomApiException.class)
                .hasMessageContaining("계좌 소유자가 아닙니다.");

    }
    @Test
    public void depositAccountTest() {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(11111111111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setType("DEPOSIT");
        accountDepositReqDto.setTel("01012341234");

        // stub
        User user = newMockUser(1L, "test", "1234", "test@teest.com", "test", UserEnum.CUSTOMER);
        Account account1 = newMockAccount(1L, 11111111111L, 1000L, user);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account1));

        // stub
        Account account2 = newMockAccount(1L, 11111111111L, 1000L, user);
        Transaction transaction = newMockDepositTransaction(1L, account2);
        when(transactionRepository.save(any())).thenReturn(transaction);
        // when
        AccountDepositResDto accountDepositResDto = accountService.depositAccount(accountDepositReqDto);

        // then
        assertThat(accountDepositResDto.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
    }
}
