package shop.woosung.bank.account.service.port;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.infrastructure.AccountEntity;

import javax.persistence.LockModeType;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> findByUserId(Long userId);

    Account save(Account account);

}
