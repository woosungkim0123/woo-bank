package shop.woosung.bank.account.service.port;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountTypeNumber;

import java.util.List;
import java.util.Optional;

public interface AccountTypeNumberRepository {
    Optional<AccountTypeNumber> findById(String accountType);

    AccountTypeNumber save(AccountTypeNumber accountTypeNumber);

}
