package shop.woosung.bank.domain.transaction.repository;

import org.springframework.data.repository.query.Param;
import shop.woosung.bank.domain.transaction.Transaction;

import java.util.List;

public interface TransactionRepositoryDao {
    List<Transaction> findTransactionList(@Param("accountId") Long accountId, @Param("type") String type, @Param("page") Integer page);
}
