package shop.woosung.bank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.domain.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
