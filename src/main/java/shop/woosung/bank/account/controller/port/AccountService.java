package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.controller.dto.AccountRegisterRequestDto;
import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.account.service.dto.AccountRegisterResponseDto;
import shop.woosung.bank.user.domain.User;

public interface AccountService {

    AccountRegisterResponseDto register(AccountRegisterRequestDto accountRegisterRequestDto, User user);
    AccountListResponseDto getAccountList(User user);
}
