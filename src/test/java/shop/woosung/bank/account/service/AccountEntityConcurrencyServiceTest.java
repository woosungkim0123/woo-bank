package shop.woosung.bank.account.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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


import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountEntityConcurrencyServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    private User user;
    
    @Test
    public void 계좌생성_동시성_문제_락을_걸어_해결() throws Exception {
        // 5개가 완료될 때까지 기다림
        user = userRepository.save(User.builder().email("test@test.com").password("1234").name("test").role(UserRole.CUSTOMER).build());
        Account save = accountRepository.save(Account.builder().number(111111L).password(1234L).balance(1000L).user(user).build());

        Account account1 = accountRepository.findById(save.getId()).get();
        System.out.println("account1.getId() = " + account1.getId());


        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Account> byUserId = accountRepository.findByUserId(user.getId());
        System.out.println("byUserId.get(0) = " + byUserId.get(0).getNumber());
        List<Long> accountNumbers = new ArrayList<>();
        AccountRegisterRequestDto accountRegisterRequestDto = AccountRegisterRequestDto.builder()
                .password(1111L)
                .build();

        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                try {
                    AccountRegisterResponseDto accountRegisterResponseDto = accountService.register(accountRegisterRequestDto, user);
                    accountNumbers.add(accountRegisterResponseDto.getNumber());
                } finally {
                    latch.countDown();
                }


            });
        }
        latch.await();


        List<Account> account = accountRepository.findByUserId(user.getId());
        for (Long a : accountNumbers) {
            System.out.println("account1.getNumber() = " + a);
        }
    }
}