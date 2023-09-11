//package shop.woosung.bank.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import shop.woosung.bank.account.service.AccountServiceImpl;
//import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
//import shop.woosung.bank.account.infrastructure.AccountJpaRepository;
//import shop.woosung.bank.transaction.infrastructure.entity.TransactionEntity;
//import shop.woosung.bank.transaction.domain.repository.TransactionRepository;
//import shop.woosung.bank.user.infrastructure.UserEntity;
//import shop.woosung.bank.user.domain.UserRole;
//import shop.woosung.bank.user.infrastructure.UserJpaRepository;
//import shop.woosung.bank.handler.ex.CustomApiException;
//import shop.woosung.bank.util.dummy.DummyUserObject;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static shop.woosung.bank.account.AccountReqDto.*;
//import static shop.woosung.bank.account.AccountResDto.*;
//
//@ExtendWith(MockitoExtension.class)
//public class AccountEntityServiceTest extends DummyUserObject {
//
//    @InjectMocks
//    private AccountServiceImpl accountService;
//
//    @Mock
//    private UserJpaRepository userJpaRepository;
//
//    @Mock
//    private AccountJpaRepository accountJpaRepository;
//    @Mock
//    private TransactionRepository transactionRepository;
//
//    @Test
//    public void registerAccount() throws JsonProcessingException {
//        // given
//        Long LAST_ACCOUNT_NUMBER = 19111111119L;
//        AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
//        accountRegisterReqDto.setPassword(1234L);
//
//        // stub
//        UserEntity userEntity = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserRole.CUSTOMER);
//        when(userJpaRepository.findById(any())).thenReturn(Optional.of(userEntity));
//        when(accountJpaRepository.findFirstByOrderByNumberDesc()).thenReturn(Optional.of(AccountEntity.builder().number(LAST_ACCOUNT_NUMBER).build()));
//
//        AccountEntity testAccountEntity = newMockAccount(1L, LAST_ACCOUNT_NUMBER + 1, 1234L, userEntity);
//        when(accountJpaRepository.save(any())).thenReturn(testAccountEntity);
//
//        // when
//        AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDto, userEntity.getId());
//
//        // then
//        assertThat(accountRegisterResDto).isNotNull();
//        assertThat(accountRegisterResDto.getNumber()).isEqualTo(19111111120L);
//    }
//
//    @Test
//    public void 계좌목록보기_유저별_test() throws Exception {
//        // given
//        Long userId = 1L;
//
//        // stub
//        UserEntity userEntity = newMockUser(1L, "test1", "1234", "test@test.com", "test1", UserRole.CUSTOMER);
//        when(userJpaRepository.findById(userId)).thenReturn(Optional.of(userEntity));
//
//        AccountEntity accountEntity1 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
//        AccountEntity accountEntity2 = newMockAccount(2L, 11111111112L, 1000L, userEntity);
//        List<AccountEntity> accountEntityList = Arrays.asList(accountEntity1, accountEntity2);
//        when(accountJpaRepository.findByUserId(any())).thenReturn(accountEntityList);
//
//        // when
//        AccountListResDto accountListRespDto = accountService.getAccountList(userId);
//
//        // then
//        assertThat(accountListRespDto.getFullname()).isEqualTo("test1");
//        assertThat(accountListRespDto.getAccounts().size()).isEqualTo(2);
//    }
//
//    @Test
//    public void deleteAccountFailNotUser() throws Exception {
//        // given
//        Long number = 11111111111L;
//        Long userId = 2L;
//
//
//        UserEntity userEntity = newMockUser(1L, "test1", "1234", "test@test.com", "test1", UserRole.CUSTOMER);
//        AccountEntity accountEntity = newMockAccount(1L, number, 1000L, userEntity);
//        when(accountJpaRepository.findByNumber(number)).thenReturn(Optional.of(accountEntity));
//
//        // when
//        // then
//        Assertions.assertThatThrownBy(() -> accountService.deleteAccount(number, userId))
//                .isInstanceOf(CustomApiException.class)
//                .hasMessageContaining("계좌 소유자가 아닙니다.");
//
//    }
//    @Test
//    public void depositAccountTest() {
//        // given
//        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
//        accountDepositReqDto.setNumber(11111111111L);
//        accountDepositReqDto.setAmount(100L);
//        accountDepositReqDto.setType("DEPOSIT");
//        accountDepositReqDto.setTel("01012341234");
//
//        // stub
//        UserEntity userEntity = newMockUser(1L, "test", "1234", "test@teest.com", "test", UserRole.CUSTOMER);
//        AccountEntity accountEntity1 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
//        when(accountJpaRepository.findByNumber(any())).thenReturn(Optional.of(accountEntity1));
//
//        // stub
//        AccountEntity accountEntity2 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
//        TransactionEntity transaction = newMockDepositTransaction(1L, accountEntity2);
//        when(transactionRepository.save(any())).thenReturn(transaction);
//        // when
//        AccountDepositResDto accountDepositResDto = accountService.depositAccount(accountDepositReqDto);
//
//        // then
//        assertThat(accountEntity1.getBalance()).isEqualTo(1100L);
//
//    }
//
//    @Test
//    public void withdrawAccountTest() {
//        // given
//        AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
//        accountWithdrawReqDto.setNumber(11111111111L);
//        accountWithdrawReqDto.setPassword(1234L);
//        accountWithdrawReqDto.setAmount(100L);
//        accountWithdrawReqDto.setType("WITHDRAW");
//
//        UserEntity userEntity = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserRole.CUSTOMER);
//        AccountEntity accountEntity = newMockAccount(1L, 11111111111L, 1000L, userEntity);
//
//        // stub
//        when(accountJpaRepository.findByNumber(any())).thenReturn(Optional.of(accountEntity));
//
//        // stub
//        AccountEntity accountEntity2 = newMockAccount(1L, 11111111111L, 1000L, userEntity);
//        TransactionEntity transaction = newMockWithdrawTransaction(1L, accountEntity2);
//        when(transactionRepository.save(any())).thenReturn(transaction);
//        // when
//        AccountWithdrawResDto accountWithdrawResDto = accountService.withdraw(accountWithdrawReqDto, 1L);
//        // then
//        assertThat(accountWithdrawResDto.getNumber()).isEqualTo(11111111111L);
//        assertThat(accountWithdrawResDto.getBalance()).isEqualTo(900L);
//    }
//
//
//    @Test
//    public void 계좌이체_test() {
//        // given
//        Long userId = 1L;
//        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
//        accountTransferReqDto.setWithdrawNumber(11111111111L);
//        accountTransferReqDto.setDepositNumber(11111111112L);
//        accountTransferReqDto.setAmount(100L);
//        accountTransferReqDto.setWithdrawPassword(1234L);
//        accountTransferReqDto.setType("TRANSFER");
//
//        UserEntity userEntity = newMockUser(1L, "test1", "1234", "sibu2005@naver.com", "테스터", UserRole.CUSTOMER);
//        AccountEntity withdrawAccountEntity = newMockAccount(1L, 11111111111L, 1000L, userEntity);
//        when(accountJpaRepository.findByNumber(11111111111L)).thenReturn(Optional.of(withdrawAccountEntity));
//
//        AccountEntity depositAccountEntity = newMockAccount(2L, 11111111112L, 1000L, userEntity);
//        when(accountJpaRepository.findByNumber(11111111112L)).thenReturn(Optional.of(depositAccountEntity));
//
//        TransactionEntity transaction = newMockTransferTransaction(1L, withdrawAccountEntity, depositAccountEntity);
//        when(transactionRepository.save(any())).thenReturn(transaction);
//
//        // when
//        AccountTransferResDto transfer = accountService.transfer(accountTransferReqDto, userId);
//
//        // then
//
//        assertThat(transfer.getTransaction().getDepositAccountBalance()).isEqualTo(1100L);
//        assertThat(transfer.getBalance()).isEqualTo(900L);
//    }
//}
