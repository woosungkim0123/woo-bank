package shop.woosung.bank.domain.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.woosung.bank.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
