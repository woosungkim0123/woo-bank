package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.domain.Account;

public interface AccountLockService {

    Account depositAccountWithLock(Long fullNumber, Long amount);
}
