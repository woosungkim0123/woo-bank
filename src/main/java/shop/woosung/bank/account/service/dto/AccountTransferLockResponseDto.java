package shop.woosung.bank.account.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountTransferLockResponseDto {
    private final Account withdrawAccount;
    private final Account depositAccount;

    public static AccountTransferLockResponseDto from(Account withdrawAccount, Account depositAccount) {
        return AccountTransferLockResponseDto.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .build();
    }
    // 여기 domain이 아니라 dto로 바꿔줘야할듯?

}
