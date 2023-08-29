package shop.woosung.bank.account.service.port;

import shop.woosung.bank.account.infrastructure.AccountSequenceEntity;

import java.util.Optional;

public interface AccountSequenceRepository {

    long count();

    AccountSequenceEntity save(AccountSequenceEntity accountSequenceEntity);

    Optional<AccountSequenceEntity> findById(String sequenceName);
}
