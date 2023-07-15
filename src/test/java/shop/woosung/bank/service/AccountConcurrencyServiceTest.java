package shop.woosung.bank.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.UserEnum;
import shop.woosung.bank.domain.user.repository.UserRepository;

import shop.woosung.bank.util.dummy.DummyUserObject;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;


@Transactional
@SpringBootTest
class AccountConcurrencyServiceTest extends DummyUserObject {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testConcurrentAccountRegistration() throws InterruptedException {
        List<AccountRegisterReqDto> accountRegisterReqDtos = new ArrayList<>();
        List<User> users = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
            accountRegisterReqDto.setPassword(1111L + i);
            accountRegisterReqDtos.add(accountRegisterReqDto);
            User user = newUser("test" + i, "1234", "test" + i + "@naver.com", "test" + i, UserEnum.CUSTOMER);
            users.add(user);
        }
        userRepository.saveAll(users);

        final CountDownLatch latch = new CountDownLatch(accountRegisterReqDtos.size());
        List<Long> accountNumbers = Collections.synchronizedList(new ArrayList<>());

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < accountRegisterReqDtos.size(); i++) {
            AccountRegisterReqDto accountRegisterReqDto = accountRegisterReqDtos.get(i);
            User user = users.get(i);

            Thread thread = new Thread(() -> {
                try {
                    AccountRegisterResDto accountRegisterResDto = accountService.registerAccount(accountRegisterReqDto, user.getId());
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