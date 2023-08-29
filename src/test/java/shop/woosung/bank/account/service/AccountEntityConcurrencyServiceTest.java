package shop.woosung.bank.account.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.account.service.port.AccountRepository;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.infrastructure.UserEntity;
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

    @Test
    public void 계좌생성_동시성_문제_락을_걸어_해결() throws Exception {
        int threadCount = 100;
        User sharedUser = userRepository.save(User.builder().email("test@test.com").password("1234").name("test").role(UserRole.CUSTOMER).build());
        
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        List<Long> accountNumbers = Collections.synchronizedList(new ArrayList<>());
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password(1111L)
                .build();

        CountDownLatch latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestDto, sharedUser);
                    accountNumbers.add(accountRegisterResponseDto.getNumber());
                } catch (Exception e) {
                    System.out.println("e = " + e);
                } finally{
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        Set<Long> accountNumberSet = new HashSet<>(accountNumbers);
        for (Long l : accountNumberSet) {
            System.out.println("l = " + l);
        }

        assertThat(accountNumbers).hasSize(threadCount);
        assertThat(accountNumberSet).hasSize(threadCount);
    }

}