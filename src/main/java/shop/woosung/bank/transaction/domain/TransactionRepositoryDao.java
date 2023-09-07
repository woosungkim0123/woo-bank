package shop.woosung.bank.transaction.domain;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepositoryDao {
    List<Transaction> findTransactionList(@Param("accountId") Long accountId, @Param("type") String type, @Param("page") Integer page);
}
