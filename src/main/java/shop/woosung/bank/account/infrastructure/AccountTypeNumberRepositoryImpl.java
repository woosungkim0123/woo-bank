package shop.woosung.bank.account.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.infrastructure.entity.AccountTypeNumberEntity;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AccountTypeNumberRepositoryImpl implements AccountTypeNumberRepository {

    private final AccountTypeNumberJpaRepository accountTypeNumberJpaRepository;

    @Override
    public Optional<AccountTypeNumber> findById(String accountType) {
        return accountTypeNumberJpaRepository.findById(accountType).map(AccountTypeNumberEntity::toModel);
    }

    @Override
    public AccountTypeNumber save(AccountTypeNumber accountTypeNumber) {
        return accountTypeNumberJpaRepository.save(AccountTypeNumberEntity.fromModel(accountTypeNumber)).toModel();
    }
}
