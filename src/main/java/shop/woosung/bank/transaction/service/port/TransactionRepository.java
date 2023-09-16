package shop.woosung.bank.transaction.service.port;

import shop.woosung.bank.transaction.domain.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> findTransactionList(Long accountId, String type, Integer page);
}
