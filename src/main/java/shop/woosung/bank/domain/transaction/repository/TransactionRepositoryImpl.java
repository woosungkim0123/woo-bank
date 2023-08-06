package shop.woosung.bank.domain.transaction.repository;

import shop.woosung.bank.domain.transaction.Transaction;

import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepositoryDao {
    @Override
    public List<Transaction> findTransactionList(Long accountId, String type, Integer page) {
        // type을 가지고 동적 쿼리
        return null;
    }
}
