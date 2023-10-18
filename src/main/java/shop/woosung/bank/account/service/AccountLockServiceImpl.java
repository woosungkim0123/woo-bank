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
import shop.woosung.bank.account.handler.exception.SameAccountTransferException;
import shop.woosung.bank.account.service.dto.AccountTransferLockResponseDto;
import shop.woosung.bank.account.service.dto.AccountTransferLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;
import shop.woosung.bank.common.service.port.PasswordEncoder;

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
    public AccountTransferLockResponseDto transferWithLock(AccountTransferLockServiceDto accountTransferLockServiceDto) {
        checkSameAccount(accountTransferLockServiceDto.getWithdrawFullNumber(), accountTransferLockServiceDto.getDepositFullNumber());

        /**
         * 데드락 방지 전략 - 순환 대기 방지
         * 항상 계좌 번호가 작은 순서대로 락을 획득하여 아래와 같은 데드락 상황을 방지한다.
         *
         * 데드락이 발생할 수 있는 예시:
         * 1. 스레드 A가 1111 계좌의 락을 획득.
         * 2. 스레드 B가 1112 계좌의 락을 획득.
         * 3. 스레드 A는 1112 계좌의 락을 대기.
         * 4. 스레드 B는 1111 계좌의 락을 대기.
         * 결과: 데드락 발생!
         */
        Account withdrawAccount;
        Account depositAccount;

        if(accountTransferLockServiceDto.getWithdrawFullNumber() < accountTransferLockServiceDto.getDepositFullNumber()) {
            withdrawAccount = getAccountWithLock(accountTransferLockServiceDto.getWithdrawFullNumber());
            depositAccount = getAccountWithLock(accountTransferLockServiceDto.getDepositFullNumber());
        } else {
            depositAccount = getAccountWithLock(accountTransferLockServiceDto.getDepositFullNumber());
            withdrawAccount = getAccountWithLock(accountTransferLockServiceDto.getWithdrawFullNumber());
        }

        withdrawAccount.checkOwner(accountTransferLockServiceDto.getUser().getId());
        withdrawAccount.checkPasswordMatch(String.valueOf(accountTransferLockServiceDto.getWithdrawPassword()), passwordEncoder);
        withdrawAccount.checkEnoughBalance(accountTransferLockServiceDto.getAmount());
        withdrawAccount.withdraw(accountTransferLockServiceDto.getAmount());

        depositAccount.deposit(accountTransferLockServiceDto.getAmount());

        accountRepository.update(withdrawAccount);
        accountRepository.update(depositAccount);

        return AccountTransferLockResponseDto.from(withdrawAccount, depositAccount);
    }

    private Account getAccountWithLock(Long fullNumber) {
        return accountRepository.findByFullNumberWithPessimisticLock(fullNumber)
                .orElseThrow(() -> new NotFoundAccountFullNumberException(fullNumber));
    }

    private void checkSameAccount(Long withdrawFullNumber, Long depositFullNumber) {
        if(withdrawFullNumber.equals(depositFullNumber)) {
            throw new SameAccountTransferException(withdrawFullNumber);
        }
    }
}
