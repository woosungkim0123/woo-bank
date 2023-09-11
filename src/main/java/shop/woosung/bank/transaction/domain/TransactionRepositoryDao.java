package shop.woosung.bank.transaction.domain;

import org.springframework.data.repository.query.Param;
import shop.woosung.bank.transaction.infrastructure.entity.TransactionEntity;

import java.util.List;

public interface TransactionRepositoryDao {
    List<TransactionEntity> findTransactionList(@Param("accountId") Long accountId, @Param("type") String type, @Param("page") Integer page);
}
