package shop.woosung.bank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findFirstByOrderByNumberDesc();

    List<AccountEntity> findByUserId(Long userId);

    Optional<AccountEntity> findByNumber(Long accountNumber);
}
