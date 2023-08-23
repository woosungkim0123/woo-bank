package shop.woosung.bank.account.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.infrastructure.AccountEntity;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.common.exception.NotFoundUserException;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.TransactionEnum;
import shop.woosung.bank.domain.transaction.repository.TransactionRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.handler.ex.CustomApiException;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.List;

import static shop.woosung.bank.account.AccountReqDto.*;
import static shop.woosung.bank.account.AccountResDto.*;

@Builder
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
//
//    @Transactional
//    public AccountRegisterResDto registerAccount(AccountRegisterReqDto accountRegisterReqDto, Long userId) {
//        User user = findUser(userId);
//
//        return registerNewAccount(user, accountRegisterReqDto);
//    }


    @Transactional(readOnly = true)
    public AccountListResponseDto getAccountList(Long userId) {
        User user = findUser(userId);
        List<Account> userAccounts = accountRepository.findByUserId(userId);

        return AccountListResponseDto.from(user, userAccounts);
    }
//
//    /*
//        TODO 영업점, 종류 추가
//        영업점 - 계좌종류 - 보통 무작위로 추출(순서) - 검증번호(복잡하게 더하거나 곱해서 작성)
//     */
//
//    @Transactional
//    public void deleteAccount(Long accountNumber, Long userId) {
//        AccountEntity accountEntityPS = accountJpaRepository.findByNumber(accountNumber)
//                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));
//
//        accountEntityPS.checkOwner(userId);
//
//        accountJpaRepository.deleteById(accountEntityPS.getId());
//    }
//
//    @Transactional
//    public AccountDepositResDto depositAccount(AccountDepositReqDto accountDepositReqDto) { // ATM -> 누군가의 계좌
//        AccountEntity depositAccountPSEntity = accountJpaRepository.findByNumber(accountDepositReqDto.getNumber())
//                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));
//
//        depositAccountPSEntity.deposit(accountDepositReqDto.getAmount());
//
//        Transaction transaction = Transaction.builder()
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
//        Transaction transactionPS = transactionRepository.save(transaction);
//
//        return new AccountDepositResDto(depositAccountPSEntity, transactionPS);
//    }
//
//
//
//
//    @Transactional
//    public synchronized AccountRegisterResDto registerNewAccount(User user, AccountRegisterReqDto accountRegisterReqDto) {
//        Long newAccountNumber = accountJpaRepository.findFirstByOrderByNumberDesc()
//                .map(account -> account.getNumber() + 1L)
//                .orElse(11111111111L);
//
//        AccountEntity newAccountEntity = accountJpaRepository.save(AccountEntity.builder()
//                .number(newAccountNumber)
//                .password(accountRegisterReqDto.getPassword())
//                .balance(1000L)
//                .user(UserEntity.fromModel(user))
//                .build());
//        return new AccountRegisterResDto(newAccountEntity.getId(), newAccountNumber, newAccountEntity.getBalance());
//    }
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
//        Transaction transaction = Transaction.builder()
//                .withdrawAccount(withdrawAccountEntity)
//                .withdrawAccountBalance(withdrawAccountEntity.getBalance())
//                .amount(accountWithdrawReqDto.getAmount())
//                .gubun(TransactionEnum.WITHDRAW)
//                .sender(accountWithdrawReqDto.getNumber() + "")
//                .receiver("ATM")
//                .build();
//
//        Transaction savedTransaction = transactionRepository.save(transaction);
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
//        Transaction transaction = Transaction.builder()
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
//        Transaction savedTransaction = transactionRepository.save(transaction);
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
//        List<Transaction> transactionList = transactionRepository.findTransactionList(accountEntityPS.getId(), type, page);
//
//        // DTO 응답
//        return new AccountDetailResDto(accountEntityPS, transactionList);
//    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException("유저를 찾을 수 없습니다."));
    }
}
