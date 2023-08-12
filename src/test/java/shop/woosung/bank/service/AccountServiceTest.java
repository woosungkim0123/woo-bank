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
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.infrastructure.UserJpaRepository;
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
    private UserJpaRepository userJpaRepository;

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
        UserEntity userEntity = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserRole.CUSTOMER);
        when(userJpaRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(accountRepository.findFirstByOrderByNumberDesc()).thenReturn(Optional.of(Account.builder().number(LAST_ACCOUNT_NUMBER).build()));

        Account testAccount = newMockAccount(1L, LAST_ACCOUNT_NUMBER + 1, 1234L, userEntity);
        when(accountRepository.save(any())).thenReturn(testAccount);

        // when
        AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDto, userEntity.getId());

        // then
        assertThat(accountRegisterResDto).isNotNull();
        assertThat(accountRegisterResDto.getNumber()).isEqualTo(19111111120L);
    }

    @Test
    public void 계좌목록보기_유저별_test() throws Exception {
        // given
        Long userId = 1L;

        // stub
        UserEntity userEntity = newMockUser(1L, "test1", "1234", "test@test.com", "test1", UserRole.CUSTOMER);
        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Account account1 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
        Account account2 = newMockAccount(2L, 11111111112L, 1000L, userEntity);
        List<Account> accountList = Arrays.asList(account1, account2);
        when(accountRepository.findByUserId(any())).thenReturn(accountList);

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


        UserEntity userEntity = newMockUser(1L, "test1", "1234", "test@test.com", "test1", UserRole.CUSTOMER);
        Account account = newMockAccount(1L, number, 1000L, userEntity);
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
        UserEntity userEntity = newMockUser(1L, "test", "1234", "test@teest.com", "test", UserRole.CUSTOMER);
        Account account1 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account1));

        // stub
        Account account2 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
        Transaction transaction = newMockDepositTransaction(1L, account2);
        when(transactionRepository.save(any())).thenReturn(transaction);
        // when
        AccountDepositResDto accountDepositResDto = accountService.depositAccount(accountDepositReqDto);

        // then
        assertThat(account1.getBalance()).isEqualTo(1100L);

    }

    @Test
    public void withdrawAccountTest() {
        // given
        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
        accountWithdrawReqDto.setNumber(11111111111L);
        accountWithdrawReqDto.setPassword(1234L);
        accountWithdrawReqDto.setAmount(100L);
        accountWithdrawReqDto.setType("WITHDRAW");

        UserEntity userEntity = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserRole.CUSTOMER);
        Account account = newMockAccount(1L, 11111111111L, 1000L, userEntity);

        // stub
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account));

        // stub
        Account account2 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
        Transaction transaction = newMockWithdrawTransaction(1L, account2);
        when(transactionRepository.save(any())).thenReturn(transaction);
        // when
        AccountWithdrawResDto accountWithdrawResDto = accountService.withdraw(accountWithdrawReqDto, 1L);
        // then
        assertThat(accountWithdrawResDto.getNumber()).isEqualTo(11111111111L);
        assertThat(accountWithdrawResDto.getBalance()).isEqualTo(900L);
    }


    @Test
    public void 계좌이체_test() {
        // given
        Long userId = 1L;
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(11111111111L);
        accountTransferReqDto.setDepositNumber(11111111112L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setType("TRANSFER");

        UserEntity userEntity = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserRole.CUSTOMER);
        Account withdrawAccount = newMockAccount(1L, 11111111111L, 1000L, userEntity);
        when(accountRepository.findByNumber(11111111111L)).thenReturn(Optional.of(withdrawAccount));

        Account depositAccount = newMockAccount(2L, 11111111112L, 1000L, userEntity);
        when(accountRepository.findByNumber(11111111112L)).thenReturn(Optional.of(depositAccount));

        Transaction transaction = newMockTransferTransaction(1L, withdrawAccount, depositAccount);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountTransferResDto transfer = accountService.transfer(accountTransferReqDto, userId);

        // then

        assertThat(transfer.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
        assertThat(transfer.getBalance()).isEqualTo(900L);
    }
}
