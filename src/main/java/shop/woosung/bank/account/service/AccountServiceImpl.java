package shop.woosung.bank.account.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountSequenceException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountTypeNumberException;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.transaction.domain.TransactionRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.List;

import static shop.woosung.bank.account.util.AccountServiceToDomainConverter.accountRegisterConvert;

@Builder
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountSequenceRepository accountSequenceRepository;
    private final AccountTypeNumberRepository accountTypeNumberRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AccountRegisterResponseDto register(AccountRegisterRequestServiceDto accountRegisterRequestServiceDto, User user) {
        Long typeNumber = getTypeNumber(accountRegisterRequestServiceDto.getType());
        Long newNumber = getNewNumber(accountRegisterRequestServiceDto.getType());

        Account account = Account.register(accountRegisterConvert(accountRegisterRequestServiceDto, typeNumber, newNumber, user), passwordEncoder);
        Account newAccount = accountRepository.save(account);

        return AccountRegisterResponseDto.from(newAccount);
    }

    @Transactional(readOnly = true)
    public AccountListResponseDto getAccountList(User user) {
        List<Account> userAccounts = accountRepository.findByUserId(user.getId());

        return AccountListResponseDto.from(user, userAccounts);
    }

    @Transactional
    public void deleteAccount(Long accountFullnumber, Long userId) {
        Account account = accountRepository.findByFullnumber(accountFullnumber)
                .orElseThrow(() -> new NotFoundAccountFullNumberException(accountFullnumber));

        account.checkOwner(userId);

        accountRepository.deleteById(account.getId());
    }


//
//    @Transactional
//    public AccountDepositResDto depositAccount(AccountDepositReqDto accountDepositReqDto) { // ATM -> 누군가의 계좌
//        AccountEntity depositAccountPSEntity = accountJpaRepository.findByNumber(accountDepositReqDto.getNumber())
//                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));
//
//        depositAccountPSEntity.deposit(accountDepositReqDto.getAmount());
//
//        TransactionEntity transaction = TransactionEntity.builder()
//                        .depositAccount(depositAccountPSEntity)
//                        .withdrawAccount(null)
//                        .depositAccountBalance(depositAccountPSEntity.getBalance())
//                        .withdrawAccountBalance(null)
//                        .amount(accountDepositReqDto.getAmount())
//                        .gubun(TransactionEnum.DEPOSIT)
//                        .sender("ATM")
//                        .receiver(accountDepositReqDto.getNumber() + "")
//                        .tel(accountDepositReqDto.getTel())
//                        .build();
//
//        TransactionEntity transactionPS = transactionRepository.save(transaction);
//
//        return new AccountDepositResDto(depositAccountPSEntity, transactionPS);
//    }
//
//
//
//

//
//    @Transactional
//    public AccountWithdrawResDto withdraw(AccountWithdrawReqDto accountWithdrawReqDto, Long userId) {
//        AccountEntity withdrawAccountEntity = accountJpaRepository.findByNumber(accountWithdrawReqDto.getNumber())
//                .orElseThrow(
//                        () -> new CustomApiException("계좌를 찾을 수 없습니다.")
//                );
//
//        // 출금 소유자 확인
//        withdrawAccountEntity.checkOwner(userId);
//
//        // 비밀번호 확인
//        withdrawAccountEntity.checkSamePassword(accountWithdrawReqDto.getPassword());
//
//        // 잔액 확인
//        withdrawAccountEntity.checkBalance(accountWithdrawReqDto.getAmount());
//
//        // 출금하기
//        withdrawAccountEntity.withdraw(accountWithdrawReqDto.getAmount());
//
//
//        // 거래내역 남기기
//        // 내 계좌 -> ATM 출금
//        TransactionEntity transaction = TransactionEntity.builder()
//                .withdrawAccount(withdrawAccountEntity)
//                .withdrawAccountBalance(withdrawAccountEntity.getBalance())
//                .amount(accountWithdrawReqDto.getAmount())
//                .gubun(TransactionEnum.WITHDRAW)
//                .sender(accountWithdrawReqDto.getNumber() + "")
//                .receiver("ATM")
//                .build();
//
//        TransactionEntity savedTransaction = transactionRepository.save(transaction);
//
//
//        // DTO 응답
//        return new AccountWithdrawResDto(withdrawAccountEntity, savedTransaction);
//    }
//
//
//    @Transactional
//    public AccountTransferResDto transfer(AccountTransferReqDto accountTransferReqDto, Long userId) {
//
//        // 출금 계좌와 입금계좌가 동일하면 안됨
//        if(accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
//            throw new CustomApiException("입출금계좌가 동일할 수 없습니다.");
//        }
//
//        AccountEntity withdrawAccountEntity = accountJpaRepository.findByNumber(accountTransferReqDto.getWithdrawNumber())
//                .orElseThrow(
//                        () -> new CustomApiException("출금계좌를 찾을 수 없습니다."));
//
//        AccountEntity depositAccountEntity = accountJpaRepository.findByNumber(accountTransferReqDto.getDepositNumber())
//                .orElseThrow(
//                        () -> new CustomApiException("출금계좌를 찾을 수 없습니다."));
//
//        // 출금 소유자 확인
//        withdrawAccountEntity.checkOwner(userId);
//
//        // 비밀번호 확인
//        withdrawAccountEntity.checkSamePassword(accountTransferReqDto.getWithdrawPassword());
//
//        // 잔액 확인
//        withdrawAccountEntity.checkBalance(accountTransferReqDto.getAmount());
//
//        // 이체하기
//        withdrawAccountEntity.withdraw(accountTransferReqDto.getAmount());
//        depositAccountEntity.deposit(accountTransferReqDto.getAmount());
//
//        // 거래내역 남기기
//        TransactionEntity transaction = TransactionEntity.builder()
//                .withdrawAccount(withdrawAccountEntity)
//                .depositAccount(depositAccountEntity)
//                .withdrawAccountBalance(withdrawAccountEntity.getBalance())
//                .depositAccountBalance(depositAccountEntity.getBalance())
//                .amount(accountTransferReqDto.getAmount())
//                .gubun(TransactionEnum.TRANSFER)
//                .sender(accountTransferReqDto.getWithdrawNumber() + "")
//                .receiver(accountTransferReqDto.getDepositNumber() + "")
//                .build();
//
//        TransactionEntity savedTransaction = transactionRepository.save(transaction);
//
//        // DTO 응답
//        return new AccountTransferResDto(withdrawAccountEntity, savedTransaction);
//    }
//
//    @Transactional(readOnly = true)
//    public AccountDetailResDto getAccountDetail(Long number, Long userId, Integer page) {
//        String type = "ALL";
//
//        AccountEntity accountEntityPS = accountJpaRepository.findByNumber(number)
//                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));
//
//        accountEntityPS.checkOwner(userId);
//
//        List<TransactionEntity> transactionList = transactionRepository.findTransactionList(accountEntityPS.getId(), type, page);
//
//        // DTO 응답
//        return new AccountDetailResDto(accountEntityPS, transactionList);
//    }
//    private Long getNewNumber() {
//        return accountRepository.findLastNumberWithPessimisticLock()
//                .map(account -> account.getNumber() + 1L)
//                .orElse(11111111111L);
//    }

    private Long getTypeNumber(AccountType accountType) {
        AccountTypeNumber accountTypeNumber = accountTypeNumberRepository.findById(accountType.name())
                .orElseThrow(() -> new NotFoundAccountTypeNumberException(accountType));
        return accountTypeNumber.getNumber();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long getNewNumber (AccountType accountType) {
        AccountSequence accountSequence = accountSequenceRepository.findById(accountType.name())
                .orElseThrow(() -> new NotFoundAccountSequenceException(accountType));

        Long nextValue = accountSequence.getNextValue();
        accountSequence.incrementNextValue();
        accountSequenceRepository.save(accountSequence);
        return nextValue;
    }
}
