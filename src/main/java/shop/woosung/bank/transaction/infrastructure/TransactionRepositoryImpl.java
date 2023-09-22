package shop.woosung.bank.transaction.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woosung.bank.transaction.domain.Transaction;
import shop.woosung.bank.transaction.domain.TransactionType;
import shop.woosung.bank.transaction.infrastructure.entity.TransactionEntity;
import shop.woosung.bank.transaction.service.port.TransactionRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionJpaRepository transactionJpaRepository;
    private final EntityManager em;

    @Override
    public List<Transaction> findTransactionList(Long accountId, String type, Integer page) {
        String sql = "select t from TransactionEntity t ";

        if (type.equals(TransactionType.WITHDRAW.name())) {
            sql += "join fetch t.withdrawAccount wa ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
        } else if (type.equals(TransactionType.DEPOSIT.name())) {
            sql += "join fetch t.depositAccount da ";
            sql += "where t.depositAccount.id = :depositAccountId ";
        } else {
            sql += "left join fetch t.withdrawAccount wa ";
            sql += "left join fetch t.depositAccount da ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
            sql += "or ";
            sql += "t.depositAccount.id = :depositAccountId ";
        }

        TypedQuery<TransactionEntity> query = em.createQuery(sql, TransactionEntity.class);

        if (type.equals(TransactionType.WITHDRAW.name())) {
            query  = query.setParameter("withdrawAccountId", accountId);
        } else if (type.equals(TransactionType.DEPOSIT.name())) {
            query  = query.setParameter("depositAccountId", accountId);
        } else {
            query  = query.setParameter("withdrawAccountId", accountId);
            query  = query.setParameter("depositAccountId", accountId);
        }
        query.setFirstResult(page * 5);
        query.setMaxResults(5);

        return query.getResultList().stream()
                .map(TransactionEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionJpaRepository.save(TransactionEntity.fromModel(transaction)).toModel();
    }
}