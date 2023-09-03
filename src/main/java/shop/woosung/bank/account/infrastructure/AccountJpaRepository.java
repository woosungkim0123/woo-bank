package shop.woosung.bank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.account.infrastructure.entity.AccountEntity;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByUserId(Long userId);

    Optional<AccountEntity> findByNumber(Long accountNumber);

    Optional<AccountEntity> findByFullnumber(Long fullnumber);
}
