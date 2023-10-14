package shop.woosung.bank.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.port.AccountLockService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.handler.exception.NotFoundAccountSequenceException;
import shop.woosung.bank.account.service.dto.AccountTransferLockResponseDto;
import shop.woosung.bank.account.service.dto.AccountTransferLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountTransferResponseDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;
import shop.woosung.bank.user.domain.User;

@RequiredArgsConstructor
@Service
public class AccountLockServiceImpl implements AccountLockService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountSequenceRepository accountSequenceRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Long getNewAccountNumber(AccountType accountType) {
        AccountSequence accountSequence = accountSequenceRepository.findById(accountType.name())
                .orElseThrow(() -> new NotFoundAccountSequenceException(accountType));

        Long nextValue = accountSequence.getNextValue();
        accountSequence.incrementNextValue();
        accountSequenceRepository.save(accountSequence);
        return nextValue;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account depositAccountWithLock(Long fullNumber, Long amount) {
        Account depositAccount = getAccountWithLock(fullNumber);
        depositAccount.deposit(amount);
        accountRepository.save(depositAccount);
        return depositAccount;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account withdrawWithLock(AccountWithdrawLockServiceDto accountWithdrawLockServiceDto) {
        Account withdrawAccount = getAccountWithLock(accountWithdrawLockServiceDto.getFullNumber());

        withdrawAccount.checkOwner(accountWithdrawLockServiceDto.getUser().getId());

        withdrawAccount.checkPasswordMatch(accountWithdrawLockServiceDto.getPassword(), passwordEncoder);
        
        withdrawAccount.checkEnoughBalance(accountWithdrawLockServiceDto.getAmount());

        withdrawAccount.withdraw(accountWithdrawLockServiceDto.getAmount());

        accountRepository.update(withdrawAccount);

        return withdrawAccount;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountTransferLockResponseDto transferWithLock(AccountTransferLockServiceDto accountTransferLockServiceDto, User user) {
        Account withdrawAccount = getAccountWithLock(accountTransferLockServiceDto.getWithdrawFullNumber());
        Account depositAccount = getAccountWithLock(accountTransferLockServiceDto.getDepositFullNumber());

        withdrawAccount.checkOwner(user.getId());
        withdrawAccount.checkPasswordMatch(String.valueOf(accountTransferLockServiceDto.getWithdrawPassword()), passwordEncoder);

        withdrawAccount.checkEnoughBalance(accountTransferLockServiceDto.getAmount());
        withdrawAccount.withdraw(accountTransferLockServiceDto.getAmount());

        depositAccount.deposit(accountTransferLockServiceDto.getAmount());

        return AccountTransferLockResponseDto.from(withdrawAccount, depositAccount);
    }

    private Account getAccountWithLock(Long fullNumber) {
        return accountRepository.findByFullNumberWithPessimisticLock(fullNumber)
                .orElseThrow(() -> new NotFoundAccountFullNumberException(fullNumber));
    }
}
