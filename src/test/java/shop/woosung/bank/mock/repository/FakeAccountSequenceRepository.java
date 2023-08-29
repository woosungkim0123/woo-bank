package shop.woosung.bank.mock.repository;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.infrastructure.AccountSequenceEntity;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeAccountSequenceRepository implements AccountSequenceRepository {

    private final List<AccountSequence> data = new ArrayList<>();

    @Override
    public long count() {
        return 0;
    }

    @Override
    public AccountSequence save(AccountSequence accountSequence) {
        return null;
    }

    @Override
    public Optional<AccountSequence> findById(String accountType) {
        return Optional.empty();
    }
}
