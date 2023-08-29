package shop.woosung.bank.account.service.port;

import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.infrastructure.AccountSequenceEntity;

import java.util.Optional;

public interface AccountSequenceRepository {

    long count();

    AccountSequence save(AccountSequence accountSequence);

    Optional<AccountSequence> findById(String accountType);
}
