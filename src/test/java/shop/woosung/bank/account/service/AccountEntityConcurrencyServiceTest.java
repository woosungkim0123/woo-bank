package shop.woosung.bank.account.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
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
class AccountEntityConcurrencyServiceTest {

    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserRepository userRepository;

    @DisplayName("100명이 동시에 계좌를 가입할 때 모두 다른 계좌번호가 부여된다")
    @Test
    public void 계좌생성_동시성_문제_락을_걸어_해결() throws Exception {
        int threadCount = 100;
        User sharedUser = userRepository.save(User.builder().email("test@test.com").password("1234").name("test").role(UserRole.CUSTOMER).build());
        
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        List<Long> accountFullNumbers = Collections.synchronizedList(new ArrayList<>());

        AccountRegisterRequestServiceDto normalAccountRegisterServiceDto = AccountRegisterRequestServiceDto.builder()
                .type(AccountType.NORMAL)
                .password("1111")
                .build();
        AccountRegisterRequestServiceDto savingAccountRegisterServiceDto = AccountRegisterRequestServiceDto.builder()
                .type(AccountType.SAVING)
                .password("1111")
                .build();

        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            executorService.submit(() -> {
                try {
                    AccountRegisterRequestServiceDto selectedDto = (threadIndex % 2 == 0) ? normalAccountRegisterServiceDto : savingAccountRegisterServiceDto;
                    AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(selectedDto, sharedUser);
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