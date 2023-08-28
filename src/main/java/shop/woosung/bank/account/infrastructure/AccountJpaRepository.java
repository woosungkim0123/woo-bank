package shop.woosung.bank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

    //@Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findTopByOrderByNumberDesc();

    List<AccountEntity> findByUserId(Long userId);

    Optional<AccountEntity> findByNumber(Long accountNumber);
}
