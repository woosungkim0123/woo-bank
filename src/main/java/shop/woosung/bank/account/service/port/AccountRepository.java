package shop.woosung.bank.account.service.port;

import shop.woosung.bank.account.domain.Account;

import java.util.List;

public interface AccountRepository {
    List<Account> findByUserId(Long userId);

    Account save(Account account);
}
