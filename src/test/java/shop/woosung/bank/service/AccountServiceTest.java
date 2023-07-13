package shop.woosung.bank.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;

import shop.woosung.bank.dto.account.AccountReqDto;
import shop.woosung.bank.dto.account.AccountResDto;
import shop.woosung.bank.util.dummy.DummyUserObject;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;


@SpringBootTest
class AccountServiceTest extends DummyUserObject {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    private long startTime;

    @BeforeEach
    public void setup() {
        // 테스트 시작 시간 기록
        startTime = System.currentTimeMillis();
    }
    @AfterEach
    public void tearDown() {
        // 테스트 종료 시간 기록
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("테스트 실행 시간: " + executionTime + "ms");
    }

    @Test
    public void testConcurrentAccountRegistration() throws InterruptedException {
        // 초기 상태 설정


        AccountRegisterReqDto accountRegisterReqDtoA = new AccountRegisterReqDto();
        accountRegisterReqDtoA.setPassword(1234L);
        AccountRegisterReqDto accountRegisterReqDtoB = new AccountRegisterReqDto();
        accountRegisterReqDtoB.setPassword(9876L);
        AccountRegisterReqDto accountRegisterReqDtoC = new AccountRegisterReqDto();
        accountRegisterReqDtoC.setPassword(3333L);
        AccountRegisterReqDto accountRegisterReqDtoD = new AccountRegisterReqDto();
        accountRegisterReqDtoD.setPassword(4444L);
        AccountRegisterReqDto accountRegisterReqDtoE = new AccountRegisterReqDto();
        accountRegisterReqDtoE.setPassword(5555L);

        User userA = newUser("testA", "1234", "testA@naver.com", "testA", UserEnum.CUSTOMER);
        User userB = newUser("testB", "1234", "testB@naver.com", "testB", UserEnum.CUSTOMER);
        User userC = newUser("testC", "1234", "testC@naver.com", "testC", UserEnum.CUSTOMER);
        User userD = newUser("testD", "1234", "testD@naver.com", "testD", UserEnum.CUSTOMER);
        User userE = newUser("testE", "1234", "testE@naver.com", "testE", UserEnum.CUSTOMER);
        userRepository.save(userA);
        userRepository.save(userB);
        userRepository.save(userC);
        userRepository.save(userD);
        userRepository.save(userE);


        final int threadCount = 5;
        final CountDownLatch latch = new CountDownLatch(threadCount);

        Thread threadA = new Thread(() -> {
            try {
                AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDtoA, userA.getId());
                System.out.println("accountRegisterResDto.getNumber() = " + accountRegisterResDto.getNumber());
            } finally {
                latch.countDown();
            }
        });

// B가 A보다 0.1초 후에 계좌 생성 시도

        Thread threadB = new Thread(() -> {
            try {
                AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDtoB, userB.getId());
                System.out.println("accountRegisterResDto.getNumber() = " + accountRegisterResDto.getNumber());
            } finally {
                latch.countDown();
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDtoC, userC.getId());
                System.out.println("accountRegisterResDto.getNumber() = " + accountRegisterResDto.getNumber());
            } finally {
                latch.countDown();
            }
        });
        Thread threadD = new Thread(() -> {
            try {
                AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDtoD, userD.getId());
                System.out.println("accountRegisterResDto.getNumber() = " + accountRegisterResDto.getNumber());
            } finally {
                latch.countDown();
            }
        });
        Thread threadE = new Thread(() -> {
            try {
                AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDtoE, userE.getId());
                System.out.println("accountRegisterResDto.getNumber() = " + accountRegisterResDto.getNumber());
            } finally {
                latch.countDown();
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
        threadE.start();
        System.out.println(" 테스트 진행중 ");
        latch.await();
        System.out.println(" 테스트 종료 ");

        // 테스트 결과 검증
//        Long accountA = accountRegisterResDtoA[0].getNumber();
//        Long accountB = accountRegisterResDtoA[1].getNumber();
//        Assertions.assertNotNull(accountA);
//        Assertions.assertNotNull(accountB);
//        Assertions.assertNotEquals(accountA, accountB);
    }
}