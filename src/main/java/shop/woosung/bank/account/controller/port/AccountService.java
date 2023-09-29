package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.service.dto.*;
import shop.woosung.bank.user.domain.User;

public interface AccountService {

    AccountRegisterResponseDto register(AccountRegisterRequestServiceDto accountRegisterRequestServiceDto, User user);

    AccountListResponseDto getAccountList(User user);

    void deleteAccount(Long fullNumber, User user);

    AccountDepositResponseDto deposit(AccountDepositRequestServiceDto accountDepositRequestServiceDto);

    AccountWithdrawResponseDto withdraw(AccountWithdrawRequestServiceDto accountWithdrawRequestServiceDto, User user);
}
