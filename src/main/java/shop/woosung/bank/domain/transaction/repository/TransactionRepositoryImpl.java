package shop.woosung.bank.domain.transaction.repository;

import lombok.RequiredArgsConstructor;
import shop.woosung.bank.domain.transaction.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryDao {

    private final EntityManager em;

    @Override
    public List<Transaction> findTransactionList(Long accountId, String type, Integer page) {
        // type을 가지고 동적 쿼리
        String sql = "";
        sql += "select t from Transaction t ";

        // 입금, 출금 내역을 조회할 때는 inner join
        // 입,출금 내역을 할때는 withdraw가 null이면 deposit이 값이 있어도 못찾음 그래서 left outer join
        if (type.equals("WITHDRAW")) {
            sql += "join fetch t.withdrawAccount wa ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
        } else if (type.equals("DEPOSIT")) {
            sql += "join fetch t.depositAccount da ";
            sql += "where t.depositAccount.id = :depositAccountId ";
        } else {
            sql += "left join fetch t.withdrawAccount wa ";
            sql += "left join fetch t.depositAccount da ";
            sql += "where t.withdrawAccount.id = :withdrawAccountId ";
            sql += "or ";
            sql += "t.depositAccount.id = :depositAccountId ";
        }
        // createNativeQuery : 일반적으로 쓰는 쿼리
        // createQuery : JPQL 쿼리
        TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);

        if (type.equals("WITHDRAW")) {
            query  = query.setParameter("withdrawAccountId", accountId);
        } else if (type.equals("DEPOSIT")) {
            query  = query.setParameter("depositAccountId", accountId);
        } else {
            query  = query.setParameter("withdrawAccountId", accountId);
            query  = query.setParameter("depositAccountId", accountId);
        }
        query.setFirstResult(page * 5); // 5, 10, 15
        query.setMaxResults(5);

        return query.getResultList();
    }
}
