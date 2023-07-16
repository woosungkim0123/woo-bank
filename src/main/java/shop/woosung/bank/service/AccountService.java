package shop.woosung.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.handler.ex.CustomApiException;

import java.util.List;

import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    public AccountListResDto getAccountList(Long userId) {
        User userPS = findUser(userId);

        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListResDto(userPS, accountListPS);
    }

    /*
        TODO 영업점, 종류 추가
        영업점 - 계좌종류 - 보통 무작위로 추출(순서) - 검증번호(복잡하게 더하거나 곱해서 작성)
     */
    public AccountRegisterResDto registerAccount(AccountRegisterReqDto accountRegisterReqDto, Long userId) {
        User userPS = findUser(userId);

        return registerNewAccount(userPS, accountRegisterReqDto);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));
    }

    @Transactional
    public synchronized AccountRegisterResDto registerNewAccount(User user, AccountRegisterReqDto accountRegisterReqDto) {
        Long newAccountNumber = accountRepository.findFirstByOrderByNumberDesc()
                .map(account -> account.getNumber() + 1L)
                .orElse(11111111111L);

        Account newAccount = accountRepository.save(Account.builder()
                .number(newAccountNumber)
                .password(accountRegisterReqDto.getPassword())
                .balance(1000L)
                .user(user)
                .build());
        return new AccountRegisterResDto(newAccount.getId(), newAccountNumber, newAccount.getBalance());
    }



}
