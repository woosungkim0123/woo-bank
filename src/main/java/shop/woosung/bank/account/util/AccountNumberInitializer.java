package shop.woosung.bank.account.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.domain.AccountTypeNumber;
import shop.woosung.bank.account.service.port.AccountTypeNumberRepository;

@RequiredArgsConstructor
@Component
@Profile({"dev", "test"})
public class AccountNumberInitializer implements ApplicationRunner {

    private final AccountTypeNumberRepository accountTypeNumberRepository;

    @Override
    public void run(ApplicationArguments args) {
        AccountTypeNumber normalAccountTypeNumber = AccountTypeNumber.builder()
                .accountType(AccountType.NORMAL)
                .number(232L)
                .build();

        AccountTypeNumber savingAccountTypeNumber = AccountTypeNumber.builder()
                .accountType(AccountType.SAVING)
                .number(787L)
                .build();

        accountTypeNumberRepository.save(normalAccountTypeNumber);
        accountTypeNumberRepository.save(savingAccountTypeNumber);
    }
}
