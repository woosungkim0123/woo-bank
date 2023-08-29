package shop.woosung.bank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findTopByOrderByNumberDesc();

    List<AccountEntity> findByUserId(Long userId);

    Optional<AccountEntity> findByNumber(Long accountNumber);

    @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "5000") })
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM AccountEntity a ORDER BY a.number DESC")
    List<AccountEntity> findHighestAccountNumberWithLock(Pageable pageable);
}
