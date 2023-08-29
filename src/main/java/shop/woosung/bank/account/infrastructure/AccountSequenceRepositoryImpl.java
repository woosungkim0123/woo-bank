package shop.woosung.bank.account.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AccountSequenceRepositoryImpl implements AccountSequenceRepository {

    private final AccountSequenceJpaRepository accountSequenceJpaRepository;

    @Override
    public long count() {
        return accountSequenceJpaRepository.count();
    }

    @Override
    public AccountSequenceEntity save(AccountSequenceEntity accountSequenceEntity) {
        return accountSequenceJpaRepository.save(accountSequenceEntity);
    }

    @Override
    public Optional<AccountSequenceEntity> findById(String sequenceName) {
        return accountSequenceJpaRepository.findById(sequenceName);
    }
}
