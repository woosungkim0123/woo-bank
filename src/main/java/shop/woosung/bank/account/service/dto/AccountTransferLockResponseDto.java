package shop.woosung.bank.account.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import shop.woosung.bank.account.domain.Account;
import shop.woosung.bank.user.domain.User;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class AccountTransferLockResponseDto {
    private final AccountDto withdrawAccountDto;
    private final AccountDto depositAccountDto;

    public static AccountTransferLockResponseDto from(Account withdrawAccount, Account depositAccount) {
        return AccountTransferLockResponseDto.builder()
                .withdrawAccountDto(AccountDto.from(withdrawAccount))
                .depositAccountDto(AccountDto.from(depositAccount))
                .build();
    }

    @Getter
    @Builder(access = AccessLevel.PRIVATE)
    public static class AccountDto {
        private final Long id;
        private final Long fullNumber;
        private final Long balance;
        private final User user;


        public static AccountDto from(Account account) {
            return AccountDto.builder()
                    .id(account.getId())
                    .fullNumber(account.getFullNumber())
                    .balance(account.getBalance())
                    .user(account.getUser())
                    .build();
        }

        public Account toDomain() {
            return Account.builder()
                    .id(this.id)
                    .fullNumber(this.fullNumber)
                    .balance(this.balance)
                    .user(user)
                    .build();
        }
    }
}