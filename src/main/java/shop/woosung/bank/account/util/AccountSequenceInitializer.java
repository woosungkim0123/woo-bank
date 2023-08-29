package shop.woosung.bank.account.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.infrastructure.AccountSequenceEntity;
import shop.woosung.bank.account.service.port.AccountSequenceRepository;

@RequiredArgsConstructor
@Component
@Profile({"dev", "test"})
public class AccountSequenceInitializer implements ApplicationRunner {

    private final AccountSequenceRepository accountSequenceRepository;

    @Override
    public void run(ApplicationArguments args) {
        if(accountSequenceRepository.count() == 0) {
            AccountSequenceEntity normalAccountSequence = AccountSequenceEntity.builder()
                    .sequenceName(AccountType.NORMAL.name())
                    .nextValue(1L)
                    .incrementBy(1L)
                    .build();

            AccountSequenceEntity savingAccountSequence = AccountSequenceEntity.builder()
                    .sequenceName(AccountType.SAVING.name())
                    .nextValue(1L)
                    .incrementBy(1L)
                    .build();

            accountSequenceRepository.save(normalAccountSequence);
            accountSequenceRepository.save(savingAccountSequence);
        }
    }
}
