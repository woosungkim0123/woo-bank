package shop.woosung.bank.transaction.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.transaction.infrastructure.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>, TransactionRepositoryDao {
}
