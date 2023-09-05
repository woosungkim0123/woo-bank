package shop.woosung.bank.mock.repository;

import shop.woosung.bank.account.domain.AccountSequence;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeAccountSequenceRepository implements AccountSequenceRepository {

    private final List<AccountSequence> data = new ArrayList<>();

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public AccountSequence save(AccountSequence accountSequence) {
        data.remove(accountSequence);
        data.add(accountSequence);
        return accountSequence;
    }

    @Override
    public Optional<AccountSequence> findById(String accountType) {
        return data.stream()
                .filter(accountSequence -> accountSequence.getSequenceName().equals(AccountType.valueOf(accountType)))
                .findFirst();
    }

    public void deleteAll() {
        data.clear();
    }
}
