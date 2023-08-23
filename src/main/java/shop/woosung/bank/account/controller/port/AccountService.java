package shop.woosung.bank.account.controller.port;

import shop.woosung.bank.account.service.dto.AccountListResponseDto;

public interface AccountService {

    AccountListResponseDto getAccountList(Long userId);
}
