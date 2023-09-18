package shop.woosung.bank.account.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.infrastructure.entity.AccountEntity;
import shop.woosung.bank.account.service.port.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public List<Account> findByUserId(Long userId) {
        return accountJpaRepository.findByUserId(userId).stream()
                .map(AccountEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Account save(Account account) {
        return accountJpaRepository.save(AccountEntity.fromModel(account)).toModel();
    }

    @Override
    public Optional<Account> findByFullnumber(Long fullnumber) {
        return accountJpaRepository.findByFullnumber(fullnumber).map(AccountEntity::toModel);
    }

    @Override
    public void deleteById(Long id) {
        accountJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Account> findByFullnumberWithPessimisticLock(Long fullnumber) {
        return accountJpaRepository.findByFullnumberWithPessimisticLock(fullnumber).map(AccountEntity::toModel);
    }
}
