package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;

public interface AccountLockService {

    Account depositAccountWithLock(Long fullNumber, Long amount);

    Account withdrawWithLock(AccountWithdrawLockServiceDto accountWithdrawLockServiceDto);
}
