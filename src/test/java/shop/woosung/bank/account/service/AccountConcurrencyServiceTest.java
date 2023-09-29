package shop.woosung.bank.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.AccountType;
import shop.woosung.bank.account.service.dto.AccountRegisterRequestServiceDto;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.service.port.UserRepository;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class AccountConcurrencyServiceTest {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserRepository userRepository;

    @DisplayName("계좌 생성 요청을 100개를 동시에 보냈을 때 중복되는 계좌가 없어야 한다.")
    @Test
    void account_create_100_concurrent_requests_not_have_duplicate_accounts() throws Exception {
        int threadCount = 100;
        User user = userRepository.save(User.builder().email("test@test.com").password("1234").name("test").role(UserRole.CUSTOMER).build());
        ExecutorService executorService = Executors.newFixedThreadPool(25);
        List<Long> accountFullNumbers = Collections.synchronizedList(new ArrayList<>());
        AccountRegisterRequestServiceDto normalAccountRegisterServiceDto = AccountRegisterRequestServiceDto.builder()
                .type(AccountType.NORMAL)
                .password("1111")
                .build();

        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(normalAccountRegisterServiceDto, user);
                    accountFullNumbers.add(accountRegisterResponseDto.getFullNumber());
                } finally{
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        Set<Long> accountFullNemberSet = new HashSet<>(accountFullNumbers);

        assertThat(accountFullNumbers).hasSize(threadCount);
        assertThat(accountFullNemberSet).hasSize(threadCount);
    }
}