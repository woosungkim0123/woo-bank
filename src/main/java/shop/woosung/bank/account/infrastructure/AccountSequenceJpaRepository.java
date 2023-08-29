package shop.woosung.bank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import shop.woosung.bank.account.domain.AccountType;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountSequenceJpaRepository extends JpaRepository<AccountSequenceEntity, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountSequenceEntity> findById(String accountType);
}
