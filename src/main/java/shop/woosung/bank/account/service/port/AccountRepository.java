package shop.woosung.bank.account.service.port;

import shop.woosung.bank.account.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findByUserId(Long userId);

    Account save(Account account);

    Optional<Account> findByFullnumber(Long fullnumber);

    void deleteById(Long id);

    Optional<Account> findByFullnumberWithPessimisticLock(Long fullnumber);

    void update(Account account);
}
