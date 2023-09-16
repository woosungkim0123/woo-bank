package shop.woosung.bank.mock.repository;

import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.service.port.TransactionRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakeTransactionRepository implements TransactionRepository {

    private Long autoIncrementId = 0L;
    private final List<Transaction> data = new ArrayList<>();
    private final LocalDateTime currentDateTime = LocalDateTime.of(2023, Month.AUGUST, 11, 15, 30, 0);

    public Transaction save(Transaction transaction) {
        if(transaction.getId() == null || transaction.getId() == 0) {
            Transaction newTransaction = Transaction.builder()
                    .id(++autoIncrementId)
                    .withdrawAccount(transaction.getWithdrawAccount())
                    .depositAccount(transaction.getDepositAccount())
                    .amount(transaction.getAmount())
                    .withdrawAccountBalance(transaction.getWithdrawAccountBalance())
                    .depositAccountBalance(transaction.getDepositAccountBalance())
                    .type(transaction.getType())
                    .sender(transaction.getSender())
                    .receiver(transaction.getReceiver())
                    .tel(transaction.getTel())
                    .createdAt(currentDateTime)
                    .updatedAt(currentDateTime)
                    .build();
            data.add(newTransaction);
            return newTransaction;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), transaction.getId()));
            data.add(transaction);
            return transaction;
        }
    }

    public void deleteAll() {
        autoIncrementId = 0L;
        data.clear();
    }

    @Override
    public List<Transaction> findTransactionList(Long accountId, String type, Integer page) {
        return null;
    }
}
