package shop.woosung.bank.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.domain.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByNumberDesc();

    List<Account> findByUserId(Long userId);

    Optional<Account> findByNumber(Long accountNumber);
}
