package shop.woosung.bank.account.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.port.AccountLockService;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountTypeNumberException;

import shop.woosung.bank.account.service.dto.*;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.transaction.service.port.TransactionRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.List;

import static shop.woosung.bank.account.util.AccountServiceToDomainConverter.*;
import static shop.woosung.bank.account.util.AccountServiceToServiceConverter.accountTransferLockServiceDtoConvert;
import static shop.woosung.bank.account.util.AccountServiceToServiceConverter.accountWithdrawLockServiceDtoConvert;

@Builder
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountLockService accountLockService;
    private final AccountRepository accountRepository;
    private final AccountSequenceRepository accountSequenceRepository;
    private final AccountTypeNumberRepository accountTypeNumberRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AccountListResponseDto getAccountList(User user) {
        List<Account> userAccounts = accountRepository.findByUserId(user.getId());

        return AccountListResponseDto.from(user, userAccounts);
    }

    @Transactional
    public AccountRegisterResponseDto register(AccountRegisterRequestServiceDto accountRegisterRequestServiceDto, User user) {
        Long typeNumber = getTypeNumber(accountRegisterRequestServiceDto.getType());
        Long newNumber = accountLockService.getNewAccountNumber(accountRegisterRequestServiceDto.getType());

        Account account = Account.register(accountRegisterConvert(accountRegisterRequestServiceDto, typeNumber, newNumber, user), passwordEncoder);
        Account newAccount = accountRepository.save(account);

        return AccountRegisterResponseDto.from(newAccount);
    }

    @Override
    public void deleteAccount(Long fullNumber, User user) {
        Account account = findAccountByFullNumber(fullNumber);

        account.checkOwner(user.getId());

        accountRepository.deleteById(account.getId());
    }

    @Transactional
    public AccountDepositResponseDto deposit(AccountDepositRequestServiceDto accountDepositRequestServiceDto) {
        // TODO dto로 변경
        Account depositAccount = accountLockService.depositAccountWithLock(accountDepositRequestServiceDto.getFullNumber(), accountDepositRequestServiceDto.getAmount());

        Transaction depositTransaction = transactionRepository.save(
                Transaction.createDepositTransaction(depositTransactionCreateConvert(accountDepositRequestServiceDto, depositAccount))
        );

        return AccountDepositResponseDto.from(depositAccount, depositTransaction);
    }

    @Transactional
    public AccountWithdrawResponseDto withdraw(AccountWithdrawRequestServiceDto accountWithdrawRequestServiceDto, User user) {
        // TODO dto로 변경
        Account withdrawAccount = accountLockService.withdrawWithLock(accountWithdrawLockServiceDtoConvert(accountWithdrawRequestServiceDto, user));

        // TODO 추후 트랜잭션 서비스로 이동 고려
        Transaction transaction = Transaction.createWithdrawTransaction(withdrawTransactionCreateConvert(accountWithdrawRequestServiceDto, withdrawAccount, "ATM"));

        Transaction withdrawTransaction = transactionRepository.save(transaction);

        return AccountWithdrawResponseDto.from(withdrawAccount, withdrawTransaction);
    }

    @Transactional
    public AccountTransferResponseDto transfer(AccountTransferRequestServiceDto accountTransferRequestServiceDto, User user) {
        AccountTransferLockResponseDto accountTransferLockResponseDto = accountLockService.transferWithLock(accountTransferLockServiceDtoConvert(accountTransferRequestServiceDto, user));

        Transaction transaction = Transaction.builder()
                .withdrawAccount(accountTransferLockResponseDto.getWithdrawAccountDto().toDomain())
                .depositAccount(accountTransferLockResponseDto.getDepositAccountDto().toDomain())
                .withdrawAccountBalance(accountTransferLockResponseDto.getWithdrawAccountDto().getBalance())
                .depositAccountBalance(accountTransferLockResponseDto.getDepositAccountDto().getBalance())
                .amount(accountTransferRequestServiceDto.getAmount())
                .type(TransactionType.TRANSFER)
                .sender(accountTransferLockResponseDto.getWithdrawAccountDto().getFullNumber() + "")
                .receiver(accountTransferLockResponseDto.getDepositAccountDto().getFullNumber() + "")
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return AccountTransferResponseDto.from(accountTransferLockResponseDto, savedTransaction);
    }

    @Transactional(readOnly = true)
    public AccountDto checkAccountOwner(Long fullNumber, User user) {
        Account account = findAccountByFullNumber(fullNumber);
        account.checkOwner(user.getId());
        return AccountDto.from(account);
    }


    private Account findAccountByFullNumber(Long fullNumber) {
        return accountRepository.findByFullNumber(fullNumber)
                .orElseThrow(() -> new NotFoundAccountFullNumberException(fullNumber));
    }

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

    private Long getTypeNumber(AccountType accountType) {
        AccountTypeNumber accountTypeNumber = accountTypeNumberRepository.findById(accountType.name())
                .orElseThrow(() -> new NotFoundAccountTypeNumberException(accountType));
        return accountTypeNumber.getNumber();
    }
}
