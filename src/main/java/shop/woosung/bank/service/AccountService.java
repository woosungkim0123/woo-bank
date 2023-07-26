package shop.woosung.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.transaction.Transaction;
import shop.woosung.bank.domain.transaction.TransactionEnum;
import shop.woosung.bank.domain.transaction.repository.TransactionRepository;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.handler.ex.CustomApiException;

import java.util.List;

import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;


    public AccountListResDto getAccountList(Long userId) {
        User userPS = findUser(userId);

        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListResDto(userPS, accountListPS);
    }

    /*
        TODO 영업점, 종류 추가
        영업점 - 계좌종류 - 보통 무작위로 추출(순서) - 검증번호(복잡하게 더하거나 곱해서 작성)
     */
    public AccountRegisterResDto registerAccount(AccountRegisterReqDto accountRegisterReqDto, Long userId) {
        User userPS = findUser(userId);

        return registerNewAccount(userPS, accountRegisterReqDto);
    }

    @Transactional
    public void deleteAccount(Long accountNumber, Long userId) {
        Account accountPS = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        accountPS.checkOwner(userId);

        accountRepository.deleteById(accountPS.getId());
    }

    @Transactional
    public AccountDepositResDto depositAccount(AccountDepositReqDto accountDepositReqDto) { // ATM -> 누군가의 계좌
        Account depositAccountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
                .orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

        depositAccountPS.deposit(accountDepositReqDto.getAmount());

        Transaction transaction = Transaction.builder()
                        .depositAccount(depositAccountPS)
                        .withdrawAccount(null)
                        .depositAccountBalance(depositAccountPS.getBalance())
                        .withdrawAccountBalance(null)
                        .amount(accountDepositReqDto.getAmount())
                        .gubun(TransactionEnum.DEPOSIT)
                        .sender("ATM")
                        .receiver(accountDepositReqDto.getNumber() + "")
                        .tel(accountDepositReqDto.getTel())
                        .build();

        Transaction transactionPS = transactionRepository.save(transaction);

        return new AccountDepositResDto(depositAccountPS, transactionPS);
    }


    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public synchronized AccountRegisterResDto registerNewAccount(User user, AccountRegisterReqDto accountRegisterReqDto) {
        Long newAccountNumber = accountRepository.findFirstByOrderByNumberDesc()
                .map(account -> account.getNumber() + 1L)
                .orElse(11111111111L);

        Account newAccount = accountRepository.save(Account.builder()
                .number(newAccountNumber)
                .password(accountRegisterReqDto.getPassword())
                .balance(1000L)
                .user(user)
                .build());
        return new AccountRegisterResDto(newAccount.getId(), newAccountNumber, newAccount.getBalance());
    }

    @Transactional
    public AccountWithdrawResDto withdraw(AccountWithdrawReqDto accountWithdrawReqDto, Long userId) {
        Account withdrawAccount = accountRepository.findByNumber(accountWithdrawReqDto.getNumber())
                .orElseThrow(
                        () -> new CustomApiException("계좌를 찾을 수 없습니다.")
                );

        // 출금 소유자 확인
        withdrawAccount.checkOwner(userId);

        // 비밀번호 확인
        withdrawAccount.checkSamePassword(accountWithdrawReqDto.getPassword());

        // 잔액 확인
        withdrawAccount.checkBalance(accountWithdrawReqDto.getAmount());

        // 출금하기
        withdrawAccount.withdraw(accountWithdrawReqDto.getAmount());


        // 거래내역 남기기
        // 내 계좌 -> ATM 출금
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .amount(accountWithdrawReqDto.getAmount())
                .gubun(TransactionEnum.WITHDRAW)
                .sender(accountWithdrawReqDto.getNumber() + "")
                .receiver("ATM")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);


        // DTO 응답
        return new AccountWithdrawResDto(withdrawAccount, savedTransaction);
    }


}
