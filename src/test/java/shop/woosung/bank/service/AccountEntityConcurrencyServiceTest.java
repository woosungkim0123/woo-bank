package shop.woosung.bank.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.account.service.AccountService;
import shop.woosung.bank.user.infrastructure.UserEntity;
import shop.woosung.bank.user.domain.UserRole;
import shop.woosung.bank.user.infrastructure.UserJpaRepository;

import shop.woosung.bank.util.dummy.DummyUserObject;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static shop.woosung.bank.account.AccountReqDto.*;
import static shop.woosung.bank.account.AccountResDto.*;


@Transactional
@SpringBootTest
class AccountEntityConcurrencyServiceTest extends DummyUserObject {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    public void testConcurrentAccountRegistration() throws InterruptedException {
        List<AccountRegisterReqDto> accountRegisterReqDtos = new ArrayList<>();
        List<UserEntity> userEntities = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
            accountRegisterReqDto.setPassword(1111L + i);
            accountRegisterReqDtos.add(accountRegisterReqDto);
            UserEntity userEntity = newUser("test" + i, "1234", "test" + i + "@naver.com",  UserRole.CUSTOMER);
            userEntities.add(userEntity);
        }
        userJpaRepository.saveAll(userEntities);

        final CountDownLatch latch = new CountDownLatch(accountRegisterReqDtos.size());
        List<Long> accountNumbers = Collections.synchronizedList(new ArrayList<>());

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < accountRegisterReqDtos.size(); i++) {
            AccountRegisterReqDto accountRegisterReqDto = accountRegisterReqDtos.get(i);
            UserEntity userEntity = userEntities.get(i);

            Thread thread = new Thread(() -> {
                try {
                    AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDto, userEntity.getId());
                    accountNumbers.add(accountRegisterResDto.getNumber());
                } finally {
                    latch.countDown();
                }
            });
            threads.add(thread);
            thread.start();
        }
        latch.await();

        Set<Long> uniqueAccountNumbers = new HashSet<>(accountNumbers);

        Assertions.assertEquals(accountNumbers.size(), uniqueAccountNumbers.size());
    }
}