package shop.woosung.bank.account.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.controller.port.AccountService;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.user.domain.User;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.service.port.UserRepository;


import java.util.*;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountEntityConcurrencyServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init() {

    }

    @Test
    public void 계좌생성_동시성_문제_락을_걸어_해결() throws InterruptedException {
        // 5개가 완료될 때까지 기다림
        User user = userRepository.save(User.builder().email("test@test.com").password("1234").name("test").role(UserRole.CUSTOMER).build());
        Thread.sleep(10000);
        List<Long> accountNumbers = new ArrayList<>();

        final CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                try {
                    AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                            .password(1111L)
                            .build();
                    AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestDto, user);
                    System.out.println("하이444");
                    accountNumbers.add(accountRegisterResponseDto.getNumber());
                    System.out.println("accountRegisterResponseDto.getNumber() = " + accountRegisterResponseDto.getNumber());
                } finally {
                    latch.countDown();
                }
            });
            thread.start();
        }
        latch.await();

        Set<Long> uniqueAccountNumbers = new HashSet<>(accountNumbers);

        assertThat(uniqueAccountNumbers.size()).isEqualTo(5);
    }
}