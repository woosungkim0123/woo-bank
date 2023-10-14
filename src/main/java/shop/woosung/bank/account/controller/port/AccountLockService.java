package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.service.dto.AccountTransferLockResponseDto;
import shop.woosung.bank.account.service.dto.AccountTransferLockServiceDto;
import shop.woosung.bank.account.service.dto.AccountWithdrawLockServiceDto;
import shop.woosung.bank.user.domain.User;

public interface AccountLockService {

    Account depositAccountWithLock(Long fullNumber, Long amount);

    Account withdrawWithLock(AccountWithdrawLockServiceDto accountWithdrawLockServiceDto);

    Long getNewAccountNumber (AccountType accountType);

    AccountTransferLockResponseDto transferWithLock(AccountTransferLockServiceDto accountTransferLockServiceDto, User user);
}
