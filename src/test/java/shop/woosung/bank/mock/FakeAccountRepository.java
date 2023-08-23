package shop.woosung.bank.mock;

import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.port.AccountRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FakeAccountRepository implements AccountRepository {

    private Long autoIncrementId = 0L;
    private final List<Account> data = new ArrayList<>();
    private final LocalDateTime currentDateTime = LocalDateTime.of(2023, Month.AUGUST, 11, 15, 30, 0);

    @Override
    public List<Account> findByUserId(Long userId) {
        return data.stream().filter(item -> Objects.equals(item.getUser().getId(), userId)).collect(Collectors.toList());
    }

    @Override
    public Account save(Account account) {
        if(account.getId() == null || account.getId() == 0) {
            Account newAccount = Account.builder()
                    .id(++autoIncrementId)
                    .number(account.getNumber())
                    .password(account.getPassword())
                    .user(account.getUser())
                    .balance(account.getBalance())
                    .createdAt(currentDateTime)
                    .updatedAt(currentDateTime)
                    .build();
            data.add(newAccount);
            return newAccount;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), account.getId()));
            data.add(account);
            return account;
        }
    }

    public void deleteAll() {
        autoIncrementId = 0L;
        data.clear();
    }
}