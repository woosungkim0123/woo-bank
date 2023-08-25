package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.service.dto.AccountListResponseDto;
import shop.woosung.bank.user.domain.User;

public interface AccountService {

    AccountListResponseDto getAccountList(User user);
}
