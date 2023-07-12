package shop.woosung.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.woosung.bank.domain.account.Account;
import shop.woosung.bank.domain.account.repository.AccountRepository;
import shop.woosung.bank.domain.user.User;
import shop.woosung.bank.domain.user.repository.UserRepository;
import shop.woosung.bank.handler.ex.CustomApiException;

import static shop.woosung.bank.dto.account.AccountReqDto.*;
import static shop.woosung.bank.dto.account.AccountResDto.*;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountRegisterResDto registerAccount(AccountRegisterReqDto accountRegisterReqDto, Long userId) {

        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

        Account accountPS = getNewAccountNumber(userPS, accountRegisterReqDto);
        // 계좌 등록
        // dto 응답
        return new AccountRegisterResDto(accountPS);
    }
//
//    // 영업점 - 계좌종류 - 보통 무작위로 추출(순서) - 검증번호(복잡하게 더하거나 곱해서 작성)

//    232- 13- 044897
    @Transactional
    // 최신 계좌번호를 찾고 그 번호에 + 1 후 저장 후 리턴
    public synchronized Account getNewAccountNumber(User user, AccountRegisterReqDto accountRegisterReqDto) {
        Long newAccountNumber = accountRepository.findFirstByOrderByNumberDesc()
                .map(account -> account.getNumber() + 1L)
                .orElse(11111111111L);

        Account accountPS = accountRepository.save(Account.builder()
                        .number(newAccountNumber)
                        .password(accountRegisterReqDto.getPassword())
                        .balance(1000L)
                        .user(user)
                .build());
        return accountPS;
    }

}
