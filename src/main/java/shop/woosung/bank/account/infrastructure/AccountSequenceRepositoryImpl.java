package shop.woosung.bank.account.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.infrastructure.entity.AccountSequenceEntity;
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
    public AccountSequence save(AccountSequence accountSequence) {
        return accountSequenceJpaRepository.save(AccountSequenceEntity.fromModel(accountSequence)).toModel();
    }

    @Override
    public Optional<AccountSequence> findById(String accountType) {
        return accountSequenceJpaRepository.findById(accountType).map(AccountSequenceEntity::toModel);
    }
}
