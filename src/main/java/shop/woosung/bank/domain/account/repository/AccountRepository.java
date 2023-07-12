package shop.woosung.bank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.domain.account.Account;

import java.util.Optional;
import java.util.stream.Stream;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByNumberDesc();
}
