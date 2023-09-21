package shop.woosung.bank.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.port.AccountLockService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.handler.exception.NotFoundAccountFullNumberException;
import shop.woosung.bank.account.service.port.AccountRepository;

@RequiredArgsConstructor
@Service
public class AccountLockServiceImpl implements AccountLockService {

    private final AccountRepository accountRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Account depositAccountWithLock(Long fullnumber, Long amount) {
        Account depositAccount = accountRepository.findByFullnumberWithPessimisticLock(fullnumber)
                .orElseThrow(() -> new NotFoundAccountFullNumberException(fullnumber));
        depositAccount.deposit(amount);
        accountRepository.update(depositAccount);
        return depositAccount;
    }
}