package shop.woosung.bank.mock.repository;

import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeAccountTypeNumberRepository implements AccountTypeNumberRepository {

    private final List<AccountTypeNumber> data = new ArrayList<>();

    @Override
    public Optional<AccountTypeNumber> findById(String accountType) {
        return data.stream()
                .filter(accountTypeNumber -> accountTypeNumber.getAccountType().equals(AccountType.valueOf(accountType)))
                .findFirst();
    }

    @Override
    public AccountTypeNumber save(AccountTypeNumber accountTypeNumber) {
        data.remove(accountTypeNumber);

        data.add(accountTypeNumber);
        return accountTypeNumber;
    }

    public void deleteAll() {
        data.clear();
    }
}
