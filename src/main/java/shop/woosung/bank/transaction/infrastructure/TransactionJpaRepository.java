package shop.woosung.bank.transaction.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.transaction.infrastructure.entity.TransactionEntity;

public interface TransactionJpaRepository extends JpaRepository<TransactionEntity, Long> {
}
